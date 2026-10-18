package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.model.SignalCategory
import com.example.model.SignalStatus
import com.example.model.TradeDirection
import com.example.model.TradingSignal
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.FreeBadge
import com.example.ui.theme.FreeBadgeBg
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.StatusClosed
import com.example.ui.theme.StatusClosedBg
import com.example.ui.theme.StatusOpen
import com.example.ui.theme.StatusOpenBg
import com.example.ui.theme.StatusSLHit
import com.example.ui.theme.StatusSLHitBg
import com.example.ui.theme.StatusTPHit
import com.example.ui.theme.StatusTPHitBg
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradeBuy
import com.example.ui.theme.TradeBuyBg
import com.example.ui.theme.TradeSell
import com.example.ui.theme.TradeSellBg
import com.example.ui.theme.VipBadge
import com.example.ui.theme.VipBadgeBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SignalCategoryHeader(
    selectedCategory: SignalCategory,
    onCategorySelected: (SignalCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            // Commodities Tab
            val isCommodities = selectedCategory == SignalCategory.COMMODITIES
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCommodities) EmeraldPrimary else Color.Transparent)
                    .clickable { onCategorySelected(SignalCategory.COMMODITIES) }
                    .padding(vertical = 10.dp)
                    .testTag("category_commodities_tab"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Commodities",
                        tint = if (isCommodities) Color.White else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Commodities",
                        fontWeight = if (isCommodities) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isCommodities) Color.White else TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            // Crypto Tab
            val isCrypto = selectedCategory == SignalCategory.CRYPTO
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCrypto) EmeraldPrimary else Color.Transparent)
                    .clickable { onCategorySelected(SignalCategory.CRYPTO) }
                    .padding(vertical = 10.dp)
                    .testTag("category_crypto_tab"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CurrencyBitcoin,
                        contentDescription = "Crypto",
                        tint = if (isCrypto) Color.White else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Crypto",
                        fontWeight = if (isCrypto) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isCrypto) Color.White else TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatusFilterChipsRow(
    selectedStatus: SignalStatus?,
    onStatusSelected: (SignalStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = { Text("All Signals", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = EmeraldLight,
                    selectedLabelColor = EmeraldDark
                )
            )
        }
        items(SignalStatus.values()) { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = {
                    if (selectedStatus == status) onStatusSelected(null) else onStatusSelected(status)
                },
                label = { Text(status.label, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = EmeraldLight,
                    selectedLabelColor = EmeraldDark
                )
            )
        }
    }
}

@Composable
fun SignalCard(
    signal: TradingSignal,
    livePrice: Double?,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(signal.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("signal_card_${signal.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Symbol, VIP Badge, Direction
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = signal.symbol,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (signal.isVip) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VipBadgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "VIP",
                                    tint = VipBadge,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "VIP",
                                    color = VipBadge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(FreeBadgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FREE",
                                color = FreeBadge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Buy / Sell Direction Badge
                val isBuy = signal.direction == TradeDirection.BUY
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isBuy) TradeBuyBg else TradeSellBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isBuy) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = signal.direction.name,
                            tint = if (isBuy) TradeBuy else TradeSell,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = signal.direction.name,
                            color = if (isBuy) TradeBuy else TradeSell,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing Grid: Entry Price & Stop Loss
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceVariantLight)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Entry Price",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatPrice(signal.entryPrice),
                        fontSize = 15.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Stop Loss",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatPrice(signal.stopLoss),
                        fontSize = 15.sp,
                        color = TradeSell,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (livePrice != null) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Live Market",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$" + formatPrice(livePrice),
                            fontSize = 15.sp,
                            color = EmeraldDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Take Profit Levels: TP1, TP2, TP3
            if (signal.tp1 != null || signal.tp2 != null || signal.tp3 != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (signal.tp1 != null) {
                        TpBox(label = "TP 1", price = signal.tp1, modifier = Modifier.weight(1f))
                    }
                    if (signal.tp2 != null) {
                        TpBox(label = "TP 2", price = signal.tp2, modifier = Modifier.weight(1f))
                    }
                    if (signal.tp3 != null) {
                        TpBox(label = "TP 3", price = signal.tp3, modifier = Modifier.weight(1f))
                    }
                }
            }

            if (!signal.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = signal.notes,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Date and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = TextMuted
                )

                // Status Badge
                val (statusColor, statusBg) = when (signal.status) {
                    SignalStatus.OPEN -> StatusOpen to StatusOpenBg
                    SignalStatus.TP_HIT -> StatusTPHit to StatusTPHitBg
                    SignalStatus.SL_HIT -> StatusSLHit to StatusSLHitBg
                    SignalStatus.CLOSED -> StatusClosed to StatusClosedBg
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = signal.status.label,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TpBox(
    label: String,
    price: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatPrice(price),
                fontSize = 12.sp,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    return if (price >= 1000) {
        String.format(Locale.US, "%,.2f", price)
    } else if (price >= 1.0) {
        String.format(Locale.US, "%.4f", price)
    } else {
        String.format(Locale.US, "%.6f", price)
    }
}

@Composable
fun SignalsEmptyState(
    category: SignalCategory,
    isSupabaseConfigured: Boolean,
    onRefresh: () -> Unit,
    onSeedData: () -> Unit,
    onOpenBackendConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("signals_empty_state")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(EmeraldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No ${category.displayName} Signals Found",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isSupabaseConfigured) {
                    "Your PostgreSQL database is connected. When new signals are posted by analysts or automated strategies, they will appear in real time."
                } else {
                    "No signals loaded yet. Configure your Supabase backend or seed initial signals to verify database operations."
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRefresh,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = EmeraldDark
                    ),
                    modifier = Modifier.testTag("empty_refresh_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Refresh")
                }

                if (isSupabaseConfigured) {
                    Button(
                        onClick = onSeedData,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary
                        ),
                        modifier = Modifier.testTag("seed_signals_button")
                    ) {
                        Text("Seed Database")
                    }
                } else {
                    Button(
                        onClick = onOpenBackendConfig,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary
                        ),
                        modifier = Modifier.testTag("open_backend_config_button")
                    ) {
                        Text("Setup Backend")
                    }
                }
            }
        }
    }
}
