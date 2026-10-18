package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.data.AdMobManager
import com.example.ui.components.SignalVerseBottomBar
import com.example.ui.components.SignalVerseDrawerSheet
import com.example.ui.components.SignalVerseHeader
import com.example.ui.screens.AboutUsDialog
import com.example.ui.screens.AccountManagementScreen
import com.example.ui.screens.BackendSettingsDialog
import com.example.ui.screens.ContactSupportDialog
import com.example.ui.screens.MarketingAssetsDialog
import com.example.ui.screens.Mt5CopyScreen
import com.example.ui.screens.PrivacyPolicyDialog
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SignalsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TermsAndConditionsDialog
import com.example.ui.theme.SignalVerseTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SignalVerseTheme {
                SignalVerseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SignalVerseApp(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity

    // Handle back button: close drawer first, or return to Signal tab
    BackHandler(enabled = drawerState.isOpen || uiState.currentTab != AppTab.SIGNAL) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (uiState.currentTab != AppTab.SIGNAL) {
            viewModel.selectTab(AppTab.SIGNAL)
        }
    }

    // Fullscreen Splash Screen if visible
    if (uiState.showSplashScreen) {
        SplashScreen(
            onDismissSplash = { viewModel.dismissSplashScreen() }
        )
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SignalVerseDrawerSheet(
                currentTab = uiState.currentTab,
                currentUser = uiState.currentUser,
                onNavigateTab = { tab -> viewModel.selectTab(tab) },
                onOpenAbout = { viewModel.setAboutDialog(true) },
                onOpenSupport = { viewModel.setSupportDialog(true) },
                onOpenPrivacy = { viewModel.setPrivacyDialog(true) },
                onOpenTerms = { viewModel.setTermsDialog(true) },
                onOpenMarketingAssets = { viewModel.setMarketingDialog(true) },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SignalVerseHeader(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                )
            },
            bottomBar = {
                SignalVerseBottomBar(
                    currentTab = uiState.currentTab,
                    onTabSelected = { tab -> viewModel.selectTab(tab) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Crossfade(targetState = uiState.currentTab, label = "tab_crossfade") { tab ->
                    when (tab) {
                        AppTab.SIGNAL -> {
                            SignalsScreen(
                                state = uiState,
                                onCategorySelected = { cat -> viewModel.selectSignalCategory(cat) },
                                onRefreshSignals = { viewModel.refreshSignals() },
                                onWatchRewardedAd = {
                                    if (activity != null) {
                                        AdMobManager.showRewardedInterstitial(activity) {
                                            viewModel.unlockVipByRewardedAd()
                                        }
                                    } else {
                                        viewModel.unlockVipByRewardedAd()
                                    }
                                }
                            )
                        }

                        AppTab.MT5_COPY -> {
                            Mt5CopyScreen(
                                state = uiState,
                                onFullNameChange = { viewModel.onCopyFullNameChange(it) },
                                onTelegramChange = { viewModel.onCopyTelegramChange(it) },
                                onWhatsappChange = { viewModel.onCopyWhatsappChange(it) },
                                onPlatformChange = { viewModel.onCopyPlatformChange(it) },
                                onLoginIdChange = { viewModel.onCopyLoginIdChange(it) },
                                onBrokerNameChange = { viewModel.onCopyBrokerNameChange(it) },
                                onBrokerServerChange = { viewModel.onCopyBrokerServerChange(it) },
                                onPasswordChange = { viewModel.onCopyPasswordChange(it) },
                                onNotesChange = { viewModel.onCopyNotesChange(it) },
                                onScreenshotPicked = { viewModel.onCopyScreenshotPicked(it) },
                                onSubmitRequest = { viewModel.submitMt5CopyRequest() },
                                onDismissSuccessDialog = { viewModel.dismissCopySuccessDialog() }
                            )
                        }

                        AppTab.ACCOUNT_MGMT -> {
                            AccountManagementScreen(
                                state = uiState,
                                onSelectServiceOption = { viewModel.selectServiceOption(it) },
                                onSelectSubmissionMethod = { viewModel.selectSubmissionMethod(it) },
                                onFullNameChange = { viewModel.onMgmtFullNameChange(it) },
                                onWhatsappChange = { viewModel.onMgmtWhatsappChange(it) },
                                onTelegramChange = { viewModel.onMgmtTelegramChange(it) },
                                onBrokerNameChange = { viewModel.onMgmtBrokerNameChange(it) },
                                onPlatformChange = { viewModel.onMgmtPlatformChange(it) },
                                onBrokerServerChange = { viewModel.onMgmtBrokerServerChange(it) },
                                onLoginIdChange = { viewModel.onMgmtLoginIdChange(it) },
                                onTradingPasswordChange = { viewModel.onMgmtTradingPasswordChange(it) },
                                onAccountBalanceChange = { viewModel.onMgmtAccountBalanceChange(it) },
                                onBrokerWebsiteOrAppChange = { viewModel.onMgmtBrokerWebsiteOrAppChange(it) },
                                onBrokerEmailChange = { viewModel.onMgmtBrokerEmailChange(it) },
                                onBrokerPasswordChange = { viewModel.onMgmtBrokerPasswordChange(it) },
                                onNotesChange = { viewModel.onMgmtNotesChange(it) },
                                onScreenshotPicked = { viewModel.onMgmtScreenshotPicked(it) },
                                onSubmitRequest = { viewModel.submitAccountManagementRequest() },
                                onDismissSuccessDialog = { viewModel.dismissMgmtSuccessDialog() }
                            )
                        }

                        AppTab.PROFILE -> {
                            ProfileScreen(
                                state = uiState,
                                onSetSignInMode = { viewModel.setSignInMode(it) },
                                onAuthNameChange = { viewModel.onAuthNameChange(it) },
                                onAuthEmailChange = { viewModel.onAuthEmailChange(it) },
                                onAuthPasswordChange = { viewModel.onAuthPasswordChange(it) },
                                onAuthConfirmPasswordChange = { viewModel.onAuthConfirmPasswordChange(it) },
                                onSubmitAuth = { viewModel.submitAuth() },
                                onSignOut = { viewModel.signOut() },
                                onOpenForgotPassword = { viewModel.setForgotPasswordDialog(true) },
                                onSubmitForgotPassword = { viewModel.submitForgotPassword(it) },
                                onDismissForgotPassword = { viewModel.setForgotPasswordDialog(false) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Informational & Support Dialogs
    if (uiState.showAboutDialog) {
        AboutUsDialog(onDismiss = { viewModel.setAboutDialog(false) })
    }

    if (uiState.showSupportDialog) {
        ContactSupportDialog(onDismiss = { viewModel.setSupportDialog(false) })
    }

    if (uiState.showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { viewModel.setPrivacyDialog(false) })
    }

    if (uiState.showTermsDialog) {
        TermsAndConditionsDialog(onDismiss = { viewModel.setTermsDialog(false) })
    }

    if (uiState.showMarketingDialog) {
        MarketingAssetsDialog(onDismiss = { viewModel.setMarketingDialog(false) })
    }
}
