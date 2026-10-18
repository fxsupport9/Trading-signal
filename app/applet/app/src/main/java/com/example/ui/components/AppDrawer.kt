package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldDeepBackground
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab

@Composable
fun SignalVerseDrawerSheet(
    currentTab: AppTab,
    currentUser: UserProfile?,
    onNavigateTab: (AppTab) -> Unit,
    onOpenAbout: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenMarketingAssets: () -> Unit = {},
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(310.dp)
            .fillMaxHeight(),
        drawerContainerColor = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldDeepBackground)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = "Logo",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "TRADING SIGNALS",
                                color = SurfaceWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Institutional Execution",
                                color = EmeraldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldDark.copy(alpha = 0.6f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = EmeraldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = currentUser.fullName,
                                    color = SurfaceWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${currentUser.tier} · ${currentUser.email}",
                                    color = EmeraldLight.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldDark.copy(alpha = 0.4f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Free Guest Mode · Sign in for VIP",
                                color = SurfaceWhite.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main navigation items
            Text(
                text = "NAVIGATE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            DrawerItem(
                label = "Trading Signals",
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                selected = currentTab == AppTab.SIGNALS,
                onClick = {
                    onNavigateTab(AppTab.SIGNALS)
                    onCloseDrawer()
                },
                testTag = "drawer_item_signals"
            )

            DrawerItem(
                label = "MT5 Copy Trading",
                icon = Icons.Default.ContentCopy,
                selected = currentTab == AppTab.MT5_COPY,
                onClick = {
                    onNavigateTab(AppTab.MT5_COPY)
                    onCloseDrawer()
                },
                testTag = "drawer_item_copy"
            )

            DrawerItem(
                label = "Account Management",
                icon = Icons.Default.AccountBalance,
                selected = currentTab == AppTab.ACCOUNT_MANAGEMENT,
                onClick = {
                    onNavigateTab(AppTab.ACCOUNT_MANAGEMENT)
                    onCloseDrawer()
                },
                testTag = "drawer_item_mgmt"
            )

            DrawerItem(
                label = "Trader Profile",
                icon = Icons.Default.Person,
                selected = currentTab == AppTab.PROFILE,
                onClick = {
                    onNavigateTab(AppTab.PROFILE)
                    onCloseDrawer()
                },
                testTag = "drawer_item_profile"
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = BorderSubtle
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Help & Legal
            Text(
                text = "SUPPORT & LEGAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            DrawerItem(
                label = "About SignalVerse",
                icon = Icons.Default.Info,
                selected = false,
                onClick = {
                    onOpenAbout()
                    onCloseDrawer()
                },
                testTag = "drawer_item_about"
            )

            DrawerItem(
                label = "Institutional Support",
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                selected = false,
                onClick = {
                    onOpenSupport()
                    onCloseDrawer()
                },
                testTag = "drawer_item_support"
            )

            DrawerItem(
                label = "Marketing & Brand",
                icon = Icons.Default.Share,
                selected = false,
                onClick = {
                    onOpenMarketingAssets()
                    onCloseDrawer()
                },
                testTag = "drawer_item_marketing"
            )

            DrawerItem(
                label = "Privacy Policy",
                icon = Icons.Default.PrivacyTip,
                selected = false,
                onClick = {
                    onOpenPrivacy()
                    onCloseDrawer()
                },
                testTag = "drawer_item_privacy"
            )

            DrawerItem(
                label = "Terms of Service",
                icon = Icons.Default.Description,
                selected = false,
                onClick = {
                    onOpenTerms()
                    onCloseDrawer()
                },
                testTag = "drawer_item_terms"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = EmeraldLight,
            selectedIconColor = EmeraldDark,
            selectedTextColor = EmeraldDark,
            unselectedContainerColor = SurfaceWhite,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .testTag(testTag)
    )
}
