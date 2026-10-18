package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldDeepBackground
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
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
    onOpenBackendConfig: () -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        drawerContainerColor = SurfaceWhite,
        drawerTonalElevation = 4.dp,
        modifier = modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Banner with gradient emerald background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(EmeraldDeepBackground, EmeraldDark)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Trading Signals",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Institutional Trading Hub",
                                color = Color(0xFFA7F3D0),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentUser != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = currentUser.fullName,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = currentUser.email,
                                    color = Color(0xFFD1FAE5),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Guest Mode • Sign in for VIP Signals",
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Navigation Links
            DrawerItem(
                label = "Home / Signals",
                icon = Icons.Default.TrendingUp,
                selected = currentTab == AppTab.SIGNAL,
                onClick = {
                    onNavigateTab(AppTab.SIGNAL)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_signals"
            )

            DrawerItem(
                label = "MT5 Copy",
                icon = Icons.Default.ContentCopy,
                selected = currentTab == AppTab.MT5_COPY,
                onClick = {
                    onNavigateTab(AppTab.MT5_COPY)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_mt5"
            )

            DrawerItem(
                label = "Account Management",
                icon = Icons.Default.AccountBalance,
                selected = currentTab == AppTab.ACCOUNT_MGMT,
                onClick = {
                    onNavigateTab(AppTab.ACCOUNT_MGMT)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_account"
            )

            DrawerItem(
                label = "Profile",
                icon = Icons.Default.Person,
                selected = currentTab == AppTab.PROFILE,
                onClick = {
                    onNavigateTab(AppTab.PROFILE)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_profile"
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = Color(0xFFE2E8F0),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Informational & Support Links
            DrawerItem(
                label = "About Us",
                icon = Icons.Default.Info,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenAbout()
                },
                testTag = "drawer_nav_about"
            )

            DrawerItem(
                label = "Contact Support",
                icon = Icons.Default.ContactSupport,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenSupport()
                },
                testTag = "drawer_nav_support"
            )

            DrawerItem(
                label = "Privacy Policy",
                icon = Icons.Default.PrivacyTip,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenPrivacy()
                },
                testTag = "drawer_nav_privacy"
            )

            DrawerItem(
                label = "Terms and Conditions",
                icon = Icons.Default.Description,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenTerms()
                },
                testTag = "drawer_nav_terms"
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = Color(0xFFE2E8F0),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            DrawerItem(
                label = "App Graphics & Splash Assets",
                icon = Icons.Default.Description,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenMarketingAssets()
                },
                testTag = "drawer_nav_marketing"
            )

            // Backend & Database Config
            DrawerItem(
                label = "Backend & Supabase Setup",
                icon = Icons.Default.Cloud,
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenBackendConfig()
                },
                testTag = "drawer_nav_backend"
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
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) EmeraldDark else TextPrimary
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) EmeraldPrimary else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = EmeraldLight,
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .testTag(testTag)
    )
}
