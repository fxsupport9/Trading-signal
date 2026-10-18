package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.GoldPrimary
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
    val categories = SignalCategory.entries
    val selectedIndex = categories.indexOf(selectedCategory)

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = SurfaceWhite,
        contentColor = EmeraldDark,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = EmeraldPrimary,
                    height = 3.dp
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Tab(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                text = {
                    Text(
                        text = category.displayName.uppercase(),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isSelected) EmeraldDark else TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                },
                modifier = Modifier.testTag("category_tab_${category.name.lowercase()}")
            )
        }
    }
}

@Composable
fun StatusFilterChipsRow(
    selectedStatus: SignalStatus?,
    onStatusSelected: (SignalStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("All Statuses", fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EmeraldLight,
                selectedLabelColor = EmeraldDark
            ),
            modifier = Modifier.testTag("filter_all")
        )

        SignalStatus.entries.forEach { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(if (selectedStatus == status) null else status) },
                label = { Text(status.label, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = EmeraldLight,
                    selectedLabelColor = EmeraldDark
                ),
                modifier = Modifier.testTag("filter_${status.name.lowercase()}")
            )
        }
    }
}

@Composable
fun SignalCard(
    signal: TradingSignal,
    livePrice: Double? = null,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(signal.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("signal_card_${signal.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Symbol, VIP badge, Direction Badge, Status Badge
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
                    if (signal.isVip) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VipBadgeBg)
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "VIP",
                                    tint = VipBadge,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "VIP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VipBadge
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Direction Badge (BUY/SELL)
                    val dirColor = if (signal.direction == TradeDirection.BUY) TradeBuy else TradeSell
                    val dirBg = if (signal.direction == TradeDirection.BUY) TradeBuyBg else TradeSellBg
                    val dirIcon = if (signal.direction == TradeDirection.BUY) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(dirBg)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = dirIcon,
                                contentDescription = null,
                                tint = dirColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = signal.direction.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = dirColor
                            )
                        }
                    }

                    // Status Badge
                    val (statusColor, statusBg) = when (signal.status) {
                        SignalStatus.OPEN -> Pair(StatusOpen, StatusOpenBg)
                        SignalStatus.TP1_HIT, SignalStatus.TP2_HIT, SignalStatus.TP3_HIT -> Pair(StatusTPHit, StatusTPHitBg)
                        SignalStatus.SL_HIT -> Pair(StatusSLHit, StatusSLHitBg)
                        SignalStatus.CLOSED -> Pair(StatusClosed, StatusClosedBg)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = signal.status.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderSubtle.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Pricing Grid: Entry, SL, TPs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "ENTRY", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                    Text(
                        text = formatPrice(signal.entryPrice),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }

                Column {
                    Text(text = "STOP LOSS", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                    Text(
                        text = formatPrice(signal.stopLoss),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TradeSell
                    )
                }

                if (signal.tp1 != null) {
                    Column {
                        Text(text = "TP 1", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatPrice(signal.tp1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TradeBuy
                        )
                    }
                }

                if (signal.tp2 != null) {
                    Column {
                        Text(text = "TP 2", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatPrice(signal.tp2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TradeBuy
                        )
                    }
                }
            }

            if (signal.tp3 != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "TP 3: ${formatPrice(signal.tp3)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TradeBuy
                    )
                }
            }

            // Live market price banner if available
            val currentLivePrice = livePrice ?: signal.livePrice
            if (currentLivePrice != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceVariantLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE MARKET PRICE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = "$${formatPrice(currentLivePrice)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                    }
                }
            }

            // Notes and Timestamp
            if (!signal.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = signal.notes,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
fun SignalsEmptyState(
    category: SignalCategory,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
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
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(EmeraldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "No ${category.displayName} Signals Right Now",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Our institutional analysts monitor markets 24/7 for high-probability setups. Tap Refresh to fetch the latest server orders.",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Refresh Signals")
            }
        }
    }
}

private fun formatPrice(price: Double): String {
    return if (price >= 1000) {
        String.format(Locale.US, "%,.2f", price)
    } else if (price >= 1) {
        String.format(Locale.US, "%.4f", price)
    } else {
        String.format(Locale.US, "%.5f", price)
    }
}
