package com.example.ui.viewmodel

import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SignalCategory
import com.example.model.SignalStatus
import com.example.model.SubmissionMethod
import com.example.model.TradingSignal
import com.example.model.UserProfile

enum class AppTab(val title: String) {
    SIGNALS("Signals"),
    MT5_COPY("MT5 Copy"),
    ACCOUNT_MANAGEMENT("Management"),
    PROFILE("Profile")
}

data class SignalUiState(
    val currentTab: AppTab = AppTab.SIGNALS,
    val selectedSignalCategory: SignalCategory = SignalCategory.COMMODITIES,
    val signals: List<TradingSignal> = emptyList(),
    val liveCryptoPrices: Map<String, Double> = emptyMap(),
    val isLoadingSignals: Boolean = false,
    val signalError: String? = null,
    val statusFilter: SignalStatus? = null,
    val vipOnlyFilter: Boolean? = null,

    // Copy Trading Form
    val copyFullName: String = "",
    val copyTelegram: String = "",
    val copyWhatsapp: String = "",
    val copyPlatform: String = "MT5",
    val copyLoginId: String = "",
    val copyBrokerName: String = "",
    val copyBrokerServer: String = "",
    val copyPassword: String = "",
    val copyNotes: String = "",
    val copyScreenshotUri: String? = null,
    val isSubmittingCopy: Boolean = false,
    val copyError: String? = null,
    val copySuccessRequest: CopyTradingRequest? = null,
    val submittedCopyRequests: List<CopyTradingRequest> = emptyList(),

    // Account Management Form
    val selectedServiceOption: ServiceOption = ServiceOption.ACCOUNT_MANAGEMENT,
    val selectedSubmissionMethod: SubmissionMethod = SubmissionMethod.TRADING_ACCOUNT,
    val mgmtFullName: String = "",
    val mgmtWhatsapp: String = "",
    val mgmtTelegram: String = "",
    val mgmtBrokerName: String = "",
    val mgmtPlatform: String = "MT5",
    val mgmtBrokerServer: String = "",
    val mgmtLoginId: String = "",
    val mgmtTradingPassword: String = "",
    val mgmtAccountBalance: String = "",
    val mgmtBrokerWebsiteOrApp: String = "",
    val mgmtBrokerEmail: String = "",
    val mgmtBrokerPassword: String = "",
    val mgmtNotes: String = "",
    val mgmtScreenshotUri: String? = null,
    val isSubmittingMgmt: Boolean = false,
    val mgmtError: String? = null,
    val mgmtSuccessRequest: AccountManagementRequest? = null,
    val submittedMgmtRequests: List<AccountManagementRequest> = emptyList(),

    // Auth & Profile
    val currentUser: UserProfile? = null,
    val isSignInMode: Boolean = true,
    val authName: String = "",
    val authEmail: String = "",
    val authPassword: String = "",
    val authConfirmPassword: String = "",
    val isAuthLoading: Boolean = false,
    val authError: String? = null,
    val authMessage: String? = null,

    // Dialogs & Navigation
    val showAboutDialog: Boolean = false,
    val showSupportDialog: Boolean = false,
    val showPrivacyDialog: Boolean = false,
    val showTermsDialog: Boolean = false,
    val showBackendConfigDialog: Boolean = false,
    val showForgotPasswordDialog: Boolean = false,
    val showMarketingDialog: Boolean = false,
    val showSplashScreen: Boolean = true,
    val vipUnlockedByAd: Boolean = false,

    // Supabase Status
    val isSupabaseConfigured: Boolean = false,
    val supabaseStatusText: String? = null
)
