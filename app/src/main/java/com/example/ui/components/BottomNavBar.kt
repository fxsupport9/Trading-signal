package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AppTab

@Composable
fun SignalVerseBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = SurfaceWhite,
        tonalElevation = 8.dp,
        modifier = modifier
            .testTag("bottom_nav_bar")
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Tab 1: Signal
        val isSignal = currentTab == AppTab.SIGNAL
        NavigationBarItem(
            selected = isSignal,
            onClick = { onTabSelected(AppTab.SIGNAL) },
            icon = {
                Icon(
                    imageVector = if (isSignal) Icons.Filled.TrendingUp else Icons.Outlined.TrendingUp,
                    contentDescription = "Signal Tab"
                )
            },
            label = {
                Text(
                    text = "Signal",
                    fontSize = 12.sp,
                    fontWeight = if (isSignal) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldDark,
                indicatorColor = EmeraldLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_signal")
        )

        // Tab 2: MT5 Copy
        val isMt5 = currentTab == AppTab.MT5_COPY
        NavigationBarItem(
            selected = isMt5,
            onClick = { onTabSelected(AppTab.MT5_COPY) },
            icon = {
                Icon(
                    imageVector = if (isMt5) Icons.Filled.ContentCopy else Icons.Outlined.ContentCopy,
                    contentDescription = "MT5 Copy Tab"
                )
            },
            label = {
                Text(
                    text = "MT5 Copy",
                    fontSize = 12.sp,
                    fontWeight = if (isMt5) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldDark,
                indicatorColor = EmeraldLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_mt5_copy")
        )

        // Tab 3: Account Management
        val isAcct = currentTab == AppTab.ACCOUNT_MGMT
        NavigationBarItem(
            selected = isAcct,
            onClick = { onTabSelected(AppTab.ACCOUNT_MGMT) },
            icon = {
                Icon(
                    imageVector = if (isAcct) Icons.Filled.AccountBalance else Icons.Outlined.AccountBalance,
                    contentDescription = "Account Management Tab"
                )
            },
            label = {
                Text(
                    text = "Account Mgmt",
                    fontSize = 12.sp,
                    fontWeight = if (isAcct) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldDark,
                indicatorColor = EmeraldLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_account_mgmt")
        )

        // Tab 4: Profile
        val isProfile = currentTab == AppTab.PROFILE
        NavigationBarItem(
            selected = isProfile,
            onClick = { onTabSelected(AppTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile Tab"
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 12.sp,
                    fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldDark,
                selectedTextColor = EmeraldDark,
                indicatorColor = EmeraldLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_profile")
        )
    }
}
