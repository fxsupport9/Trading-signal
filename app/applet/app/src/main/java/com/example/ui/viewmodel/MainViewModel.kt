package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LocalDataStore
import com.example.data.MarketDataService
import com.example.data.SupabaseClient
import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SignalCategory
import com.example.model.SignalStatus
import com.example.model.SubmissionMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                supabaseStatusText = if (configured) "Cloud Sync Active" else "Local & Cloud Ready"
            )
        }
        refreshSignals()
        refreshLiveMarketPrices()
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
            result.onSuccess { list ->
                _uiState.update { it.copy(signals = list, isLoadingSignals = false) }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        signals = SupabaseClient.getStarterSignals().filter { s -> s.category == cat },
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
            supabase.seedSampleSignalsToSupabase()
            refreshSignals()
        }
    }

    // Copy Trading Form Handlers
    fun onCopyFullNameChange(v: String) = _uiState.update { it.copy(copyFullName = v) }
    fun onCopyTelegramChange(v: String) = _uiState.update { it.copy(copyTelegram = v) }
    fun onCopyWhatsappChange(v: String) = _uiState.update { it.copy(copyWhatsapp = v) }
    fun onCopyPlatformChange(v: String) = _uiState.update { it.copy(copyPlatform = v) }
    fun onCopyLoginIdChange(v: String) = _uiState.update { it.copy(copyLoginId = v) }
    fun onCopyBrokerNameChange(v: String) = _uiState.update { it.copy(copyBrokerName = v) }
    fun onCopyBrokerServerChange(v: String) = _uiState.update { it.copy(copyBrokerServer = v) }
    fun onCopyPasswordChange(v: String) = _uiState.update { it.copy(copyPassword = v) }
    fun onCopyNotesChange(v: String) = _uiState.update { it.copy(copyNotes = v) }
    fun onCopyScreenshotPicked(uri: String?) = _uiState.update { it.copy(copyScreenshotUri = uri) }

    fun submitMt5CopyRequest() {
        val s = _uiState.value
        if (s.copyFullName.isBlank() || s.copyWhatsapp.isBlank() || s.copyBrokerName.isBlank() ||
            s.copyBrokerServer.isBlank() || s.copyLoginId.isBlank() || s.copyPassword.isBlank()
        ) {
            _uiState.update { it.copy(copyError = "Please fill in all required MT5 trading credentials.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingCopy = true, copyError = null) }
            val req = CopyTradingRequest(
                id = "cpr_${System.currentTimeMillis()}",
                fullName = s.copyFullName.trim(),
                whatsappNumber = s.copyWhatsapp.trim(),
                telegramUsername = s.copyTelegram.trim(),
                brokerName = s.copyBrokerName.trim(),
                platform = s.copyPlatform.trim(),
                brokerServer = s.copyBrokerServer.trim(),
                loginId = s.copyLoginId.trim(),
                passwordProtected = s.copyPassword.trim(),
                additionalNotes = s.copyNotes.trim(),
                screenshotUri = s.copyScreenshotUri
            )
            localDataStore.saveCopyRequest(req)
            supabase.submitCopyRequest(req)

            _uiState.update {
                it.copy(
                    isSubmittingCopy = false,
                    copySuccessRequest = req,
                    submittedCopyRequests = localDataStore.getCopyRequests(),
                    copyFullName = "",
                    copyWhatsapp = "",
                    copyTelegram = "",
                    copyBrokerName = "",
                    copyBrokerServer = "",
                    copyLoginId = "",
                    copyPassword = "",
                    copyNotes = "",
                    copyScreenshotUri = null
                )
            }
        }
    }

    fun dismissCopySuccessDialog() {
        _uiState.update { it.copy(copySuccessRequest = null) }
    }

    // Account Management Form Handlers
    fun selectServiceOption(opt: ServiceOption) = _uiState.update { it.copy(selectedServiceOption = opt) }
    fun selectSubmissionMethod(m: SubmissionMethod) = _uiState.update { it.copy(selectedSubmissionMethod = m) }
    fun onMgmtFullNameChange(v: String) = _uiState.update { it.copy(mgmtFullName = v) }
    fun onMgmtWhatsappChange(v: String) = _uiState.update { it.copy(mgmtWhatsapp = v) }
    fun onMgmtTelegramChange(v: String) = _uiState.update { it.copy(mgmtTelegram = v) }
    fun onMgmtBrokerNameChange(v: String) = _uiState.update { it.copy(mgmtBrokerName = v) }
    fun onMgmtPlatformChange(v: String) = _uiState.update { it.copy(mgmtPlatform = v) }
    fun onMgmtBrokerServerChange(v: String) = _uiState.update { it.copy(mgmtBrokerServer = v) }
    fun onMgmtLoginIdChange(v: String) = _uiState.update { it.copy(mgmtLoginId = v) }
    fun onMgmtTradingPasswordChange(v: String) = _uiState.update { it.copy(mgmtTradingPassword = v) }
    fun onMgmtAccountBalanceChange(v: String) = _uiState.update { it.copy(mgmtAccountBalance = v) }
    fun onMgmtBrokerWebsiteOrAppChange(v: String) = _uiState.update { it.copy(mgmtBrokerWebsiteOrApp = v) }
    fun onMgmtBrokerEmailChange(v: String) = _uiState.update { it.copy(mgmtBrokerEmail = v) }
    fun onMgmtBrokerPasswordChange(v: String) = _uiState.update { it.copy(mgmtBrokerPassword = v) }
    fun onMgmtNotesChange(v: String) = _uiState.update { it.copy(mgmtNotes = v) }
    fun onMgmtScreenshotPicked(uri: String?) = _uiState.update { it.copy(mgmtScreenshotUri = uri) }

    fun submitAccountManagementRequest() {
        val s = _uiState.value
        if (s.mgmtFullName.isBlank() || s.mgmtWhatsapp.isBlank() || s.mgmtAccountBalance.isBlank()) {
            _uiState.update { it.copy(mgmtError = "Please fill in your name, WhatsApp, and current capital.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingMgmt = true, mgmtError = null) }
            val req = AccountManagementRequest(
                id = "mgr_${System.currentTimeMillis()}",
                serviceOption = s.selectedServiceOption,
                submissionMethod = s.selectedSubmissionMethod,
                fullName = s.mgmtFullName.trim(),
                whatsappNumber = s.mgmtWhatsapp.trim(),
                telegramUsername = s.mgmtTelegram.trim(),
                brokerName = s.mgmtBrokerName.trim(),
                platform = s.mgmtPlatform.trim(),
                brokerServer = s.mgmtBrokerServer.trim(),
                loginId = s.mgmtLoginId.trim(),
                tradingPasswordProtected = s.mgmtTradingPassword.trim(),
                accountBalance = s.mgmtAccountBalance.trim(),
                brokerWebsiteOrApp = s.mgmtBrokerWebsiteOrApp.trim(),
                brokerEmail = s.mgmtBrokerEmail.trim(),
                brokerPasswordProtected = s.mgmtBrokerPassword.trim(),
                additionalNotes = s.mgmtNotes.trim(),
                screenshotUri = s.mgmtScreenshotUri
            )
            localDataStore.saveAccountManagementRequest(req)
            supabase.submitAccountManagementRequest(req)

            _uiState.update {
                it.copy(
                    isSubmittingMgmt = false,
                    mgmtSuccessRequest = req,
                    submittedMgmtRequests = localDataStore.getAccountManagementRequests(),
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
        }
    }

    fun dismissMgmtSuccessDialog() {
        _uiState.update { it.copy(mgmtSuccessRequest = null) }
    }

    // Auth Handlers
    fun setSignInMode(signIn: Boolean) = _uiState.update { it.copy(isSignInMode = signIn, authError = null, authMessage = null) }
    fun onAuthNameChange(v: String) = _uiState.update { it.copy(authName = v) }
    fun onAuthEmailChange(v: String) = _uiState.update { it.copy(authEmail = v) }
    fun onAuthPasswordChange(v: String) = _uiState.update { it.copy(authPassword = v) }
    fun onAuthConfirmPasswordChange(v: String) = _uiState.update { it.copy(authConfirmPassword = v) }

    fun submitAuth() {
        val s = _uiState.value
        if (s.authEmail.trim().isBlank() || !s.authEmail.contains("@")) {
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
            val result = if (s.isSignInMode) {
                supabase.signIn(s.authEmail.trim(), s.authPassword)
            } else {
                supabase.signUp(s.authName.trim(), s.authEmail.trim(), s.authPassword)
            }

            result.onSuccess { user ->
                localDataStore.saveUserProfile(user)
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isAuthLoading = false,
                        authPassword = "",
                        authConfirmPassword = "",
                        authMessage = if (s.isSignInMode) "Signed in successfully. Welcome back, ${user.fullName}!" else "Account registered successfully. Welcome to Trading Signals!"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        authError = err.localizedMessage ?: "Authentication error"
                    )
                }
            }
        }
    }

    fun submitForgotPassword(email: String) {
        if (email.trim().isBlank() || !email.contains("@")) {
            _uiState.update { it.copy(authError = "Please enter a valid email address.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true) }
            supabase.resetPassword(email.trim())
            _uiState.update {
                it.copy(
                    isAuthLoading = false,
                    showForgotPasswordDialog = false,
                    authMessage = "Password reset instructions sent to $email."
                )
            }
        }
    }

    fun signOut() {
        localDataStore.saveUserProfile(null)
        _uiState.update {
            it.copy(
                currentUser = null,
                authMessage = "Signed out successfully."
            )
        }
    }

    // Dialog & UI state controls
    fun setAboutDialog(show: Boolean) = _uiState.update { it.copy(showAboutDialog = show) }
    fun setSupportDialog(show: Boolean) = _uiState.update { it.copy(showSupportDialog = show) }
    fun setPrivacyDialog(show: Boolean) = _uiState.update { it.copy(showPrivacyDialog = show) }
    fun setTermsDialog(show: Boolean) = _uiState.update { it.copy(showTermsDialog = show) }
    fun setForgotPasswordDialog(show: Boolean) = _uiState.update { it.copy(showForgotPasswordDialog = show) }
    fun setMarketingDialog(show: Boolean) = _uiState.update { it.copy(showMarketingDialog = show) }
    fun dismissSplashScreen() = _uiState.update { it.copy(showSplashScreen = false) }
    fun openSplashScreen() = _uiState.update { it.copy(showSplashScreen = true) }

    fun unlockVipByRewardedAd() {
        _uiState.update {
            it.copy(
                vipUnlockedByAd = true,
                authMessage = "VIP signals temporarily unlocked! Full access active."
            )
        }
    }
}
