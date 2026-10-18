package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdMobBannerView
import com.example.data.AdMobNativeCard
import com.example.model.SignalCategory
import com.example.model.TradingSignal
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
import com.example.ui.theme.VipBadge
import com.example.ui.theme.VipBadgeBg
import com.example.ui.viewmodel.SignalUiState

@Composable
fun SignalsScreen(
    state: SignalUiState,
    onCategorySelected: (SignalCategory) -> Unit,
    onRefreshSignals: () -> Unit,
    onWatchRewardedAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredSignals = state.signals.filter { signal ->
        val matchesCategory = signal.category == state.selectedSignalCategory
        val matchesStatus = state.statusFilter == null || signal.status == state.statusFilter
        val matchesVip = state.vipOnlyFilter == null || (if (state.vipOnlyFilter) signal.isVip else true)
        matchesCategory && matchesStatus && matchesVip
    }

    val isVipUnlocked = state.vipUnlockedByAd || (state.currentUser?.tier?.contains("VIP", ignoreCase = true) == true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Tab Header: Commodities / Crypto
        SignalCategoryHeader(
            selectedCategory = state.selectedSignalCategory,
            onCategorySelected = onCategorySelected
        )

        // Status Filter Chips
        StatusFilterChipsRow(
            selectedStatus = state.statusFilter,
            onStatusSelected = { /* handled in viewModel or local filter */ }
        )

        // Signals List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("signals_list"),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Top Sponsor Banner
            item {
                AdMobBannerView()
            }

            // VIP Ad Reward Unlocker Card
            if (!isVipUnlocked) {
                item {
                    VipAdUnlockBanner(
                        onWatchAd = onWatchRewardedAd
                    )
                }
            } else {
                item {
                    VipUnlockedBadge()
                }
            }

            // Loading indicator
            if (state.isLoadingSignals && filteredSignals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = EmeraldPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Updating institutional orders...",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // Signals or Empty State
            if (filteredSignals.isEmpty() && !state.isLoadingSignals) {
                item {
                    SignalsEmptyState(
                        category = state.selectedSignalCategory,
                        onRefresh = onRefreshSignals
                    )
                }
            } else {
                items(filteredSignals) { signal ->
                    val livePrice = state.liveCryptoPrices[signal.symbol]
                    if (signal.isVip && !isVipUnlocked) {
                        LockedVipSignalCard(signal = signal, onUnlockClick = onWatchRewardedAd)
                    } else {
                        SignalCard(
                            signal = signal,
                            livePrice = livePrice
                        )
                    }
                }
            }

            // In-Feed Native Sponsor Card
            item {
                AdMobNativeCard()
            }
        }
    }
}

@Composable
private fun VipAdUnlockBanner(
    onWatchAd: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VipBadgeBg),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("vip_unlock_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "VIP Locked",
                        tint = SurfaceWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Unlock Institutional VIP Signals",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Watch 1 quick ad for free temporary VIP access",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onWatchAd,
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("watch_vip_ad_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Watch",
                    tint = SurfaceWhite,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Unlock",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun VipUnlockedBadge() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldLight),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LockOpen,
                contentDescription = "Unlocked",
                tint = EmeraldDark,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "VIP Signals Unlocked · Full Institutional Access Active",
                color = EmeraldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LockedVipSignalCard(
    signal: TradingSignal,
    onUnlockClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = signal.symbol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(VipBadgeBg)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = VipBadge, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("VIP EXCLUSIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VipBadge)
                        }
                    }
                }

                Button(
                    onClick = onUnlockClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Unlock Signal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Entry, Take-Profit targets, and Stop Loss are restricted to VIP traders. Watch a short sponsor ad or sign in to reveal.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}
