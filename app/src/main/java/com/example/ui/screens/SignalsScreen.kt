package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdMobBannerView
import com.example.data.AdMobNativeCard
import com.example.model.SignalCategory
import com.example.model.SignalSymbols
import com.example.ui.components.SignalCard
import com.example.ui.components.SignalCategoryHeader
import com.example.ui.components.SignalsEmptyState
import com.example.ui.components.StatusFilterChipsRow
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradeSell
import com.example.ui.viewmodel.SignalUiState

@Composable
fun SignalsScreen(
    state: SignalUiState,
    onCategorySelected: (SignalCategory) -> Unit,
    onRefreshSignals: () -> Unit,
    onSeedSignals: () -> Unit,
    onOpenBackendConfig: () -> Unit,
    onWatchRewardedAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSymbols = if (state.selectedSignalCategory == SignalCategory.COMMODITIES) {
        SignalSymbols.COMMODITIES_SYMBOLS
    } else {
        SignalSymbols.CRYPTO_SYMBOLS
    }

    // Filter signals by category and status
    val filteredSignals = state.signals.filter { sig ->
        sig.category == state.selectedSignalCategory &&
                (state.statusFilter == null || sig.status == state.statusFilter)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("signals_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top Category Toggle: Commodities vs Crypto
        item {
            SignalCategoryHeader(
                selectedCategory = state.selectedSignalCategory,
                onCategorySelected = onCategorySelected
            )
        }

        // Ticker Carousel of supported symbols in this category
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "SUPPORTED PAIRS IN ${state.selectedSignalCategory.displayName.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(activeSymbols) { sym ->
                        val liveP = state.liveCryptoPrices[sym]
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceWhite,
                            border = BorderStroke(1.dp, BorderSubtle),
                            shadowElevation = 0.5.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = sym,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                if (liveP != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$${String.format(java.util.Locale.US, "%,.2f", liveP)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Google AdMob Banner Ad Placement
        item {
            AdMobBannerView()
        }

        // Rewarded Interstitial Video Ad Hook: Unlock VIP Trading Signals
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, GoldLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "VIP",
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Unlock VIP Signals",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Watch a brief sponsored video to unlock VIP entry alerts",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = onWatchRewardedAd,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("watch_rewarded_ad_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.OndemandVideo,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Watch", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Status Filter Chips
        item {
            StatusFilterChipsRow(
                selectedStatus = state.statusFilter,
                onStatusSelected = { /* handled in viewModel */ }
            )
        }

        // Section Title & Refresh Action
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Live Signals",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${filteredSignals.size})",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }

                if (state.isLoadingSignals) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = EmeraldPrimary
                    )
                } else {
                    IconButton(
                        onClick = onRefreshSignals,
                        modifier = Modifier.testTag("refresh_signals_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Signals",
                            tint = EmeraldPrimary
                        )
                    }
                }
            }
        }

        // Error message if any
        if (state.signalError != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEE2E2),
                    border = BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = TradeSell,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.signalError,
                            fontSize = 12.sp,
                            color = TradeSell
                        )
                    }
                }
            }
        }

        // Signal Cards or Empty State
        if (filteredSignals.isEmpty() && !state.isLoadingSignals) {
            item {
                SignalsEmptyState(
                    category = state.selectedSignalCategory,
                    isSupabaseConfigured = state.isSupabaseConfigured,
                    onRefresh = onRefreshSignals,
                    onSeedData = onSeedSignals,
                    onOpenBackendConfig = onOpenBackendConfig
                )
            }
            item {
                AdMobNativeCard()
            }
        } else {
            itemsIndexed(filteredSignals, key = { _, sig -> sig.id }) { index, sig ->
                val liveP = state.liveCryptoPrices[sig.symbol]
                SignalCard(
                    signal = sig,
                    livePrice = liveP
                )
                // Insert sponsored native ad card after 2nd item
                if (index == 1) {
                    AdMobNativeCard()
                }
            }
        }
    }
}
