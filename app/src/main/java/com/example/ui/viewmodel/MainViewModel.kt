package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppConfig
import com.example.data.LocalDataStore
import com.example.data.MarketDataService
import com.example.data.SupabaseClient
import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SignalCategory
import com.example.model.SignalStatus
import com.example.model.SubmissionMethod
import com.example.model.TradingSignal
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppTab(val title: String) {
    SIGNAL("Signal"),
    MT5_COPY("MT5 Copy"),
    ACCOUNT_MGMT("Account Mgmt"),
    PROFILE("Profile")
}

data class SignalUiState(
    val currentTab: AppTab = AppTab.SIGNAL,
    val selectedSignalCategory: SignalCategory = SignalCategory.COMMODITIES,
    val signals: List<TradingSignal> = emptyList(),
    val liveCryptoPrices: Map<String, Double> = emptyMap(),
    val isLoadingSignals: Boolean = false,
    val signalError: String? = null,
    val statusFilter: SignalStatus? = null,
    val vipOnlyFilter: Boolean? = null,

    // MT5 Copy Form
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
    // Method 1
    val mgmtBrokerName: String = "",
    val mgmtPlatform: String = "MT5",
    val mgmtBrokerServer: String = "",
    val mgmtLoginId: String = "",
    val mgmtTradingPassword: String = "",
    val mgmtAccountBalance: String = "",
    // Method 2
    val mgmtBrokerWebsiteOrApp: String = "",
    val mgmtBrokerEmail: String = "",
    val mgmtBrokerPassword: String = "",
    // General
    val mgmtNotes: String = "",
    val mgmtScreenshotUri: String? = null,
    val isSubmittingMgmt: Boolean = false,
    val mgmtError: String? = null,
    val mgmtSuccessRequest: AccountManagementRequest? = null,
    val submittedMgmtRequests: List<AccountManagementRequest> = emptyList(),

    // Profile & Auth
    val currentUser: UserProfile? = null,
    val isSignInMode: Boolean = true,
    val authName: String = "",
    val authEmail: String = "",
    val authPassword: String = "",
    val authConfirmPassword: String = "",
    val isAuthLoading: Boolean = false,
    val authError: String? = null,
    val authMessage: String? = null,

    // Dialogs / Drawer Sheets
    val showAboutDialog: Boolean = false,
    val showSupportDialog: Boolean = false,
    val showPrivacyDialog: Boolean = false,
    val showTermsDialog: Boolean = false,
    val showBackendConfigDialog: Boolean = false,
    val showForgotPasswordDialog: Boolean = false,
    val showMarketingDialog: Boolean = false,
    val showSplashScreen: Boolean = false,
    val vipUnlockedByAd: Boolean = false,

    // Backend status
    val isSupabaseConfigured: Boolean = false,
    val supabaseStatusText: String = "Checking..."
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val supabase = SupabaseClient(application)
    private val localDataStore = LocalDataStore(application)

    private val _uiState = MutableStateFlow(SignalUiState())
    val uiState: StateFlow<SignalUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val user = localDataStore.getUserProfile()
        val copyRequests = localDataStore.getCopyRequests()
        val mgmtRequests = localDataStore.getAccountManagementRequests()
        val configured = supabase.isConfigured

        _uiState.update {
            it.copy(
                currentUser = user,
                submittedCopyRequests = copyRequests,
                submittedMgmtRequests = mgmtRequests,
                isSupabaseConfigured = configured,
                supabaseStatusText = if (configured) "Supabase Configured" else "Supabase Not Configured"
            )
        }

        refreshSignals()
        refreshLiveMarketPrices()
        testSupabaseConnection()
        com.example.data.AdMobManager.initialize(getApplication())
        com.example.data.AdMobManager.loadRewardedInterstitial(getApplication())
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectSignalCategory(category: SignalCategory) {
        _uiState.update { it.copy(selectedSignalCategory = category) }
        refreshSignals()
    }

    fun setStatusFilter(status: SignalStatus?) {
        _uiState.update { it.copy(statusFilter = status) }
    }

    fun setVipFilter(vipOnly: Boolean?) {
        _uiState.update { it.copy(vipOnlyFilter = vipOnly) }
    }

    fun refreshSignals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSignals = true, signalError = null) }
            val cat = _uiState.value.selectedSignalCategory
            val result = supabase.fetchSignals(cat)
            result.onSuccess { signalList ->
                _uiState.update {
                    it.copy(
                        signals = signalList,
                        isLoadingSignals = false,
                        signalError = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        signals = emptyList(),
                        isLoadingSignals = false,
                        signalError = err.localizedMessage
                    )
                }
            }
        }
    }

    fun refreshLiveMarketPrices() {
        viewModelScope.launch {
            val prices = MarketDataService.fetchLiveCryptoPrices()
            if (prices.isNotEmpty()) {
                _uiState.update { it.copy(liveCryptoPrices = prices) }
            }
        }
    }

    fun seedStarterSignals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSignals = true) }
            val result = supabase.seedSampleSignalsToSupabase()
            result.onSuccess {
                refreshSignals()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(isLoadingSignals = false, signalError = err.localizedMessage)
                }
            }
        }
    }

    // ==========================================
    // MT5 COPY TRADING
    // ==========================================
    fun onCopyFullNameChange(v: String) = _uiState.update { it.copy(copyFullName = v, copyError = null) }
    fun onCopyTelegramChange(v: String) = _uiState.update { it.copy(copyTelegram = v, copyError = null) }
    fun onCopyWhatsappChange(v: String) = _uiState.update { it.copy(copyWhatsapp = v, copyError = null) }
    fun onCopyPlatformChange(v: String) = _uiState.update { it.copy(copyPlatform = v) }
    fun onCopyLoginIdChange(v: String) = _uiState.update { it.copy(copyLoginId = v, copyError = null) }
    fun onCopyBrokerNameChange(v: String) = _uiState.update { it.copy(copyBrokerName = v, copyError = null) }
    fun onCopyBrokerServerChange(v: String) = _uiState.update { it.copy(copyBrokerServer = v, copyError = null) }
    fun onCopyPasswordChange(v: String) = _uiState.update { it.copy(copyPassword = v, copyError = null) }
    fun onCopyNotesChange(v: String) = _uiState.update { it.copy(copyNotes = v) }
    fun onCopyScreenshotPicked(uri: String?) = _uiState.update { it.copy(copyScreenshotUri = uri) }

    fun submitMt5CopyRequest() {
        val s = _uiState.value
        // Validation
        if (s.copyFullName.trim().length < 2) {
            _uiState.update { it.copy(copyError = "Please enter your full name.") }
            return
        }
        if (s.copyTelegram.trim().isEmpty() || !s.copyTelegram.contains("@")) {
            _uiState.update { it.copy(copyError = "Please enter a valid Telegram username (e.g. @your_username).") }
            return
        }
        if (s.copyWhatsapp.trim().length < 7 || !s.copyWhatsapp.startsWith("+")) {
            _uiState.update { it.copy(copyError = "Please enter your WhatsApp number with international country code (e.g. +1...).") }
            return
        }
        if (s.copyLoginId.trim().isEmpty() || s.copyLoginId.trim().length < 3) {
            _uiState.update { it.copy(copyError = "Please enter a valid MT4/MT5 Trading Login ID.") }
            return
        }
        if (s.copyBrokerName.trim().isEmpty()) {
            _uiState.update { it.copy(copyError = "Please specify your Broker Name (e.g. IC Markets, Exness).") }
            return
        }
        if (s.copyBrokerServer.trim().isEmpty()) {
            _uiState.update { it.copy(copyError = "Please specify your Broker Server (e.g. Real-01).") }
            return
        }
        if (s.copyPassword.trim().isEmpty()) {
            _uiState.update { it.copy(copyError = "Please enter your trading account password.") }
            return
        }

        // Prevent duplicate check
        val isDuplicate = s.submittedCopyRequests.any {
            it.loginId == s.copyLoginId.trim() && it.platform == s.copyPlatform && it.status == "Pending"
        }
        if (isDuplicate) {
            _uiState.update { it.copy(copyError = "A pending copy-trading request for Login ID ${s.copyLoginId} already exists.") }
            return
        }

        val request = CopyTradingRequest(
            id = "copy_" + UUID.randomUUID().toString().take(8),
            fullName = s.copyFullName.trim(),
            telegramUsername = s.copyTelegram.trim(),
            whatsappNumber = s.copyWhatsapp.trim(),
            platform = s.copyPlatform,
            loginId = s.copyLoginId.trim(),
            brokerName = s.copyBrokerName.trim(),
            brokerServer = s.copyBrokerServer.trim(),
            passwordProtected = s.copyPassword,
            additionalNotes = s.copyNotes.trim(),
            screenshotUri = s.copyScreenshotUri,
            submissionDate = System.currentTimeMillis(),
            status = "Pending"
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingCopy = true, copyError = null) }
            val result = supabase.submitCopyRequest(request)
            result.onSuccess {
                localDataStore.saveCopyRequest(request)
                val updatedList = localDataStore.getCopyRequests()
                _uiState.update {
                    it.copy(
                        isSubmittingCopy = false,
                        copySuccessRequest = request,
                        submittedCopyRequests = updatedList,
                        copyFullName = "",
                        copyTelegram = "",
                        copyWhatsapp = "",
                        copyLoginId = "",
                        copyBrokerName = "",
                        copyBrokerServer = "",
                        copyPassword = "",
                        copyNotes = "",
                        copyScreenshotUri = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isSubmittingCopy = false,
                        copyError = "Submission failed: ${err.localizedMessage}"
                    )
                }
            }
        }
    }

    fun dismissCopySuccessDialog() {
        _uiState.update { it.copy(copySuccessRequest = null) }
    }

    // ==========================================
    // ACCOUNT MANAGEMENT
    // ==========================================
    fun selectServiceOption(opt: ServiceOption) = _uiState.update { it.copy(selectedServiceOption = opt, mgmtError = null) }
    fun selectSubmissionMethod(m: SubmissionMethod) = _uiState.update { it.copy(selectedSubmissionMethod = m, mgmtError = null) }

    fun onMgmtFullNameChange(v: String) = _uiState.update { it.copy(mgmtFullName = v, mgmtError = null) }
    fun onMgmtWhatsappChange(v: String) = _uiState.update { it.copy(mgmtWhatsapp = v, mgmtError = null) }
    fun onMgmtTelegramChange(v: String) = _uiState.update { it.copy(mgmtTelegram = v, mgmtError = null) }

    // Method 1
    fun onMgmtBrokerNameChange(v: String) = _uiState.update { it.copy(mgmtBrokerName = v, mgmtError = null) }
    fun onMgmtPlatformChange(v: String) = _uiState.update { it.copy(mgmtPlatform = v) }
    fun onMgmtBrokerServerChange(v: String) = _uiState.update { it.copy(mgmtBrokerServer = v, mgmtError = null) }
    fun onMgmtLoginIdChange(v: String) = _uiState.update { it.copy(mgmtLoginId = v, mgmtError = null) }
    fun onMgmtTradingPasswordChange(v: String) = _uiState.update { it.copy(mgmtTradingPassword = v, mgmtError = null) }
    fun onMgmtAccountBalanceChange(v: String) = _uiState.update { it.copy(mgmtAccountBalance = v, mgmtError = null) }

    // Method 2
    fun onMgmtBrokerWebsiteOrAppChange(v: String) = _uiState.update { it.copy(mgmtBrokerWebsiteOrApp = v, mgmtError = null) }
    fun onMgmtBrokerEmailChange(v: String) = _uiState.update { it.copy(mgmtBrokerEmail = v, mgmtError = null) }
    fun onMgmtBrokerPasswordChange(v: String) = _uiState.update { it.copy(mgmtBrokerPassword = v, mgmtError = null) }

    // General
    fun onMgmtNotesChange(v: String) = _uiState.update { it.copy(mgmtNotes = v) }
    fun onMgmtScreenshotPicked(uri: String?) = _uiState.update { it.copy(mgmtScreenshotUri = uri) }

    fun submitAccountManagementRequest() {
        val s = _uiState.value

        // Validate Contact
        if (s.mgmtFullName.trim().length < 2) {
            _uiState.update { it.copy(mgmtError = "Please enter your full name.") }
            return
        }
        if (s.mgmtWhatsapp.trim().length < 7 || !s.mgmtWhatsapp.startsWith("+")) {
            _uiState.update { it.copy(mgmtError = "Please enter your WhatsApp number with international code (e.g. +1...).") }
            return
        }
        if (s.mgmtTelegram.trim().isEmpty() || !s.mgmtTelegram.contains("@")) {
            _uiState.update { it.copy(mgmtError = "Please enter a valid Telegram username (e.g. @your_username).") }
            return
        }

        // Validate Method-specific
        if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) {
            if (s.mgmtBrokerName.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter broker name.") }
                return
            }
            if (s.mgmtBrokerServer.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter broker server.") }
                return
            }
            if (s.mgmtLoginId.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter trading login ID.") }
                return
            }
            if (s.mgmtTradingPassword.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter trading password.") }
                return
            }
            if (s.mgmtAccountBalance.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please specify account balance / size.") }
                return
            }
        } else {
            if (s.mgmtBrokerWebsiteOrApp.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter broker website or app name.") }
                return
            }
            if (s.mgmtBrokerEmail.trim().isEmpty() || !s.mgmtBrokerEmail.contains("@")) {
                _uiState.update { it.copy(mgmtError = "Please enter valid broker login email.") }
                return
            }
            if (s.mgmtBrokerPassword.trim().isEmpty()) {
                _uiState.update { it.copy(mgmtError = "Please enter broker portal password.") }
                return
            }
        }

        val request = AccountManagementRequest(
            id = "mgmt_" + UUID.randomUUID().toString().take(8),
            serviceOption = s.selectedServiceOption,
            submissionMethod = s.selectedSubmissionMethod,
            fullName = s.mgmtFullName.trim(),
            whatsappNumber = s.mgmtWhatsapp.trim(),
            telegramUsername = s.mgmtTelegram.trim(),
            brokerName = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtBrokerName.trim() else null,
            platform = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtPlatform else null,
            brokerServer = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtBrokerServer.trim() else null,
            loginId = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtLoginId.trim() else null,
            tradingPasswordProtected = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtTradingPassword else null,
            accountBalance = if (s.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) s.mgmtAccountBalance.trim() else null,
            brokerWebsiteOrApp = if (s.selectedSubmissionMethod == SubmissionMethod.BROKER_LOGIN) s.mgmtBrokerWebsiteOrApp.trim() else null,
            brokerEmail = if (s.selectedSubmissionMethod == SubmissionMethod.BROKER_LOGIN) s.mgmtBrokerEmail.trim() else null,
            brokerPasswordProtected = if (s.selectedSubmissionMethod == SubmissionMethod.BROKER_LOGIN) s.mgmtBrokerPassword else null,
            additionalNotes = s.mgmtNotes.trim(),
            screenshotUri = s.mgmtScreenshotUri,
            submissionDate = System.currentTimeMillis(),
            status = "Pending"
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingMgmt = true, mgmtError = null) }
            val result = supabase.submitAccountManagementRequest(request)
            result.onSuccess {
                localDataStore.saveAccountManagementRequest(request)
                val updatedList = localDataStore.getAccountManagementRequests()
                _uiState.update {
                    it.copy(
                        isSubmittingMgmt = false,
                        mgmtSuccessRequest = request,
                        submittedMgmtRequests = updatedList,
                        mgmtFullName = "",
                        mgmtWhatsapp = "",
                        mgmtTelegram = "",
                        mgmtBrokerName = "",
                        mgmtBrokerServer = "",
                        mgmtLoginId = "",
                        mgmtTradingPassword = "",
                        mgmtAccountBalance = "",
                        mgmtBrokerWebsiteOrApp = "",
                        mgmtBrokerEmail = "",
                        mgmtBrokerPassword = "",
                        mgmtNotes = "",
                        mgmtScreenshotUri = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isSubmittingMgmt = false,
                        mgmtError = "Submission failed: ${err.localizedMessage}"
                    )
                }
            }
        }
    }

    fun dismissMgmtSuccessDialog() {
        _uiState.update { it.copy(mgmtSuccessRequest = null) }
    }

    // ==========================================
    // AUTHENTICATION & PROFILE
    // ==========================================
    fun setSignInMode(signIn: Boolean) = _uiState.update { it.copy(isSignInMode = signIn, authError = null, authMessage = null) }
    fun onAuthNameChange(v: String) = _uiState.update { it.copy(authName = v, authError = null) }
    fun onAuthEmailChange(v: String) = _uiState.update { it.copy(authEmail = v, authError = null) }
    fun onAuthPasswordChange(v: String) = _uiState.update { it.copy(authPassword = v, authError = null) }
    fun onAuthConfirmPasswordChange(v: String) = _uiState.update { it.copy(authConfirmPassword = v, authError = null) }

    fun submitAuth() {
        val s = _uiState.value
        if (s.authEmail.trim().isEmpty() || !s.authEmail.contains("@")) {
            _uiState.update { it.copy(authError = "Please enter a valid email address.") }
            return
        }
        if (s.authPassword.length < 6) {
            _uiState.update { it.copy(authError = "Password must be at least 6 characters.") }
            return
        }

        if (!s.isSignInMode) {
            if (s.authName.trim().length < 2) {
                _uiState.update { it.copy(authError = "Please enter your full name.") }
                return
            }
            if (s.authPassword != s.authConfirmPassword) {
                _uiState.update { it.copy(authError = "Passwords do not match.") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authError = null, authMessage = null) }

            if (supabase.isConfigured) {
                if (s.isSignInMode) {
                    val res = supabase.signIn(s.authEmail, s.authPassword)
                    res.onSuccess { profile ->
                        localDataStore.saveUserProfile(profile)
                        _uiState.update {
                            it.copy(
                                currentUser = profile,
                                isAuthLoading = false,
                                authEmail = "",
                                authPassword = "",
                                authMessage = "Signed in successfully."
                            )
                        }
                    }.onFailure { err ->
                        _uiState.update {
                            it.copy(isAuthLoading = false, authError = err.localizedMessage)
                        }
                    }
                } else {
                    val res = supabase.signUp(s.authName, s.authEmail, s.authPassword)
                    res.onSuccess { profile ->
                        localDataStore.saveUserProfile(profile)
                        _uiState.update {
                            it.copy(
                                currentUser = profile,
                                isAuthLoading = false,
                                authName = "",
                                authEmail = "",
                                authPassword = "",
                                authConfirmPassword = "",
                                authMessage = "Account registered successfully."
                            )
                        }
                    }.onFailure { err ->
                        _uiState.update {
                            it.copy(isAuthLoading = false, authError = err.localizedMessage)
                        }
                    }
                }
            } else {
                // Secure local auth mode when Supabase credentials have not yet been configured
                val profile = UserProfile(
                    id = "local_" + UUID.randomUUID().toString().take(8),
                    email = s.authEmail.trim(),
                    fullName = if (s.isSignInMode) s.authEmail.substringBefore("@").replaceFirstChar { it.uppercase() } else s.authName.trim(),
                    accountStatus = "Active",
                    tier = "VIP Member"
                )
                localDataStore.saveUserProfile(profile)
                _uiState.update {
                    it.copy(
                        currentUser = profile,
                        isAuthLoading = false,
                        authEmail = "",
                        authPassword = "",
                        authName = "",
                        authConfirmPassword = "",
                        authMessage = "Signed in locally (Configure Supabase for cloud sync)."
                    )
                }
            }
        }
    }

    fun submitForgotPassword(email: String) {
        if (email.trim().isEmpty() || !email.contains("@")) {
            _uiState.update { it.copy(authError = "Please enter a valid email address.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true) }
            val res = supabase.resetPassword(email)
            res.onSuccess { msg ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        showForgotPasswordDialog = false,
                        authMessage = msg
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        authError = err.localizedMessage
                    )
                }
            }
        }
    }

    fun signOut() {
        localDataStore.saveUserProfile(null)
        _uiState.update { it.copy(currentUser = null, authMessage = "Signed out successfully.") }
    }

    // ==========================================
    // BACKEND SETTINGS & DRAWER DIALOGS
    // ==========================================
    fun setAboutDialog(show: Boolean) = _uiState.update { it.copy(showAboutDialog = show) }
    fun setSupportDialog(show: Boolean) = _uiState.update { it.copy(showSupportDialog = show) }
    fun setPrivacyDialog(show: Boolean) = _uiState.update { it.copy(showPrivacyDialog = show) }
    fun setTermsDialog(show: Boolean) = _uiState.update { it.copy(showTermsDialog = show) }
    fun setBackendConfigDialog(show: Boolean) = _uiState.update { it.copy(showBackendConfigDialog = show) }
    fun setForgotPasswordDialog(show: Boolean) = _uiState.update { it.copy(showForgotPasswordDialog = show) }

    fun updateSupabaseConfig(url: String, key: String) {
        AppConfig.setCustomSupabaseConfig(getApplication(), url, key)
        val configured = supabase.isConfigured
        _uiState.update {
            it.copy(
                isSupabaseConfigured = configured,
                supabaseStatusText = if (configured) "Updated & Testing..." else "Not Configured"
            )
        }
        testSupabaseConnection()
        refreshSignals()
    }

    fun testSupabaseConnection() {
        viewModelScope.launch {
            if (!supabase.isConfigured) {
                _uiState.update {
                    it.copy(
                        isSupabaseConfigured = false,
                        supabaseStatusText = "Not Configured (.env / Settings)"
                    )
                }
                return@launch
            }
            _uiState.update { it.copy(supabaseStatusText = "Testing connection...") }
            val (ok, message) = supabase.testConnection()
            _uiState.update {
                it.copy(
                    isSupabaseConfigured = ok,
                    supabaseStatusText = if (ok) "Connected to Supabase" else "Degraded: $message"
                )
            }
        }
    }

    fun setMarketingDialog(show: Boolean) = _uiState.update { it.copy(showMarketingDialog = show) }
    fun dismissSplashScreen() = _uiState.update { it.copy(showSplashScreen = false) }
    fun openSplashScreen() = _uiState.update { it.copy(showSplashScreen = true) }

    fun unlockVipByRewardedAd() {
        _uiState.update {
            it.copy(
                vipUnlockedByAd = true,
                authMessage = "VIP Trading Signals Unlocked by Rewarded Ad!"
            )
        }
    }
}
