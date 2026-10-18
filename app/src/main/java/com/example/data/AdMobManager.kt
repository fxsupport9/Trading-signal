package com.example.data

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

object AdMobManager {
    private const val TAG = "AdMobManager"

    // User's Real AdMob IDs from screenshots
    const val APP_ID = "ca-app-pub-1895906484640218~9597991631"
    const val BANNER_AD_UNIT_ID = "ca-app-pub-1895906484640218/1337367399"
    const val REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-1895906484640218/5401480348"
    const val NATIVE_AD_UNIT_ID = "ca-app-pub-1895906484640218/9773251458"

    // Official Google AdMob Test IDs for seamless testing before review completion
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/5354046379"

    private var isInitialized = false
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    var isRewardedAdReady by mutableStateOf(false)
        private set

    fun initialize(context: Context, onReady: (() -> Unit)? = null) {
        if (isInitialized) {
            onReady?.invoke()
            return
        }
        try {
            MobileAds.initialize(context) { initializationStatus ->
                Log.d(TAG, "AdMob Initialized: $initializationStatus")
                isInitialized = true
                onReady?.invoke()
            }
        } catch (e: Exception) {
            Log.e(TAG, "AdMob initialization error", e)
        }
    }

    fun loadRewardedInterstitial(context: Context, useTestAd: Boolean = false) {
        val unitId = if (useTestAd) TEST_REWARDED_INTERSTITIAL_AD_UNIT_ID else REWARDED_INTERSTITIAL_AD_UNIT_ID
        val adRequest = AdRequest.Builder().build()

        RewardedInterstitialAd.load(
            context,
            unitId,
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Rewarded interstitial ad loaded successfully.")
                    rewardedInterstitialAd = ad
                    isRewardedAdReady = true

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            rewardedInterstitialAd = null
                            isRewardedAdReady = false
                            // Preload next ad
                            loadRewardedInterstitial(context, useTestAd)
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.e(TAG, "Failed to show rewarded interstitial: ${error.message}")
                            rewardedInterstitialAd = null
                            isRewardedAdReady = false
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Failed to load rewarded interstitial with unitId ($unitId): ${error.message}")
                    rewardedInterstitialAd = null
                    isRewardedAdReady = false
                    // If production ad failed (e.g. pending review/no-fill), fallback to test ad
                    if (!useTestAd) {
                        Log.d(TAG, "Retrying with official Google Test Rewarded Interstitial...")
                        loadRewardedInterstitial(context, useTestAd = true)
                    }
                }
            }
        )
    }

    fun showRewardedInterstitial(activity: Activity, onEarnedReward: () -> Unit) {
        val ad = rewardedInterstitialAd
        if (ad != null) {
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onEarnedReward()
            }
        } else {
            Log.w(TAG, "Rewarded ad was not ready to show.")
            // Allow reward if ad is still loading to not block user
            onEarnedReward()
        }
    }
}

@Composable
fun AdMobBannerView(
    modifier: Modifier = Modifier,
    useTestAdIfFailed: Boolean = true
) {
    var adLoaded by remember { mutableStateOf(false) }
    var adFailed by remember { mutableStateOf(false) }
    var currentUnitId by remember { mutableStateOf(AdMobManager.BANNER_AD_UNIT_ID) }

    Surface(
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("admob_banner_container")
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ad badge complying with Google AdMob & Play policy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "AD",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sponsored Market Partner",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            key(currentUnitId) {
                var adViewInstance by remember { mutableStateOf<AdView?>(null) }

                DisposableEffect(currentUnitId) {
                    onDispose {
                        try {
                            adViewInstance?.destroy()
                        } catch (e: Exception) {
                            Log.w("AdMobBanner", "Cleanup error", e)
                        }
                    }
                }

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    factory = { context ->
                        AdView(context).apply {
                            adViewInstance = this
                            setAdSize(AdSize.BANNER)
                            // Ad unit ID can ONLY be set once on an AdView instance
                            adUnitId = currentUnitId
                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    adLoaded = true
                                    adFailed = false
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Log.w("AdMobBanner", "Banner failed ($currentUnitId): ${error.message} (code: ${error.code})")
                                    if (useTestAdIfFailed && currentUnitId != AdMobManager.TEST_BANNER_AD_UNIT_ID) {
                                        // Switch state so key(currentUnitId) recreates a brand new AdView with official test unit ID
                                        currentUnitId = AdMobManager.TEST_BANNER_AD_UNIT_ID
                                    } else {
                                        adFailed = true
                                    }
                                }
                            }
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = {
                        // Crucial: NEVER call adView.adUnitId = ... here!
                        // AdMob throws java.lang.IllegalStateException: The ad unit ID can only be set once on AdView.
                    }
                )
            }
        }
    }
}

@Composable
fun AdMobNativeCard(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("admob_native_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SPONSORED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AdMob Native Partner",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Regulated Multi-Asset Brokerage with 0.0 Pip Spreads",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Connect Trading Signals directly via low-latency MT5 bridge with institutional liquidity and zero swap on major crypto pairs.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
