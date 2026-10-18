package com.example.data

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SurfaceVariantLight
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

    const val APP_ID = "ca-app-pub-1895906484640218~9597991631"
    const val BANNER_AD_UNIT_ID = "ca-app-pub-1895906484640218/1337367399"
    const val REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-1895906484640218/5401480348"
    const val NATIVE_AD_UNIT_ID = "ca-app-pub-1895906484640218/9773251458"

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
            MobileAds.initialize(context) { status ->
                Log.d(TAG, "AdMob Initialized: $status")
                isInitialized = true
                loadRewardedInterstitial(context)
                onReady?.invoke()
            }
        } catch (e: Exception) {
            Log.e(TAG, "AdMob initialization error", e)
        }
    }

    fun loadRewardedInterstitial(context: Context, useTestAd: Boolean = false) {
        val adUnitId = if (useTestAd) TEST_REWARDED_INTERSTITIAL_AD_UNIT_ID else REWARDED_INTERSTITIAL_AD_UNIT_ID
        val adRequest = AdRequest.Builder().build()

        RewardedInterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Rewarded interstitial loaded ($adUnitId)")
                    rewardedInterstitialAd = ad
                    isRewardedAdReady = true

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            rewardedInterstitialAd = null
                            isRewardedAdReady = false
                            loadRewardedInterstitial(context, useTestAd)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.w(TAG, "Failed to show rewarded ad: ${adError.message}")
                            rewardedInterstitialAd = null
                            isRewardedAdReady = false
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "Rewarded ad load failed ($adUnitId): ${loadAdError.message}")
                    rewardedInterstitialAd = null
                    isRewardedAdReady = false
                    if (!useTestAd) {
                        loadRewardedInterstitial(context, useTestAd = true)
                    }
                }
            }
        )
    }

    fun showRewardedInterstitial(activity: Activity, onEarnedReward: () -> Unit) {
        val ad = rewardedInterstitialAd
        if (ad != null && isRewardedAdReady) {
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onEarnedReward()
            }
        } else {
            // If ad not ready, grant reward anyway so user is never blocked
            Log.d(TAG, "Rewarded ad not ready, granting fallback VIP access")
            onEarnedReward()
            loadRewardedInterstitial(activity)
        }
    }
}

@Composable
fun AdMobBannerView(
    modifier: Modifier = Modifier,
    useTestAdIfFailed: Boolean = true
) {
    LocalContext.current
    var adLoaded by remember { mutableStateOf(false) }
    var adFailed by remember { mutableStateOf(false) }
    var currentUnitId by remember { mutableStateOf(AdMobManager.BANNER_AD_UNIT_ID) }
    var adViewInstance by remember { mutableStateOf<AdView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            try {
                adViewInstance?.destroy()
            } catch (e: Exception) {
                Log.w("AdMobBanner", "Cleanup error", e)
            }
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("admob_banner_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SPONSORED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (adLoaded) "Ad Active" else if (adFailed) "Ad Loading" else "Google AdMob",
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            adViewInstance = this
                            setAdSize(AdSize.BANNER)
                            // Set adUnitId ONLY ONCE in factory - NEVER in update block!
                            setAdUnitId(currentUnitId)
                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    adLoaded = true
                                    adFailed = false
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Log.w("AdMobBanner", "Banner failed ($currentUnitId): ${error.message} (code: ${error.code})")
                                    if (useTestAdIfFailed && currentUnitId != AdMobManager.TEST_BANNER_AD_UNIT_ID) {
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
                        // Do NOT call setAdUnitId here! It causes IllegalStateException!
                    }
                )

                if (!adLoaded && !adFailed) {
                    Text(
                        text = "Loading sponsor ad...",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Ad",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Institutional Trading Tools",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Ad · Regulated Broker Liquidity",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceVariantLight)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SPONSORED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Zero spread accounts, 1:500 leverage, and ultra-fast MT5 order routing for commodities and crypto trading.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FilledTonalButton(
                    onClick = { /* External sponsor link */ },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = EmeraldLight,
                        contentColor = EmeraldDark
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Explore Deals", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
