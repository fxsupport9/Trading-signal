package com.example.model

enum class SignalCategory(val displayName: String) {
    COMMODITIES("Commodities"),
    CRYPTO("Crypto")
}

enum class TradeDirection {
    BUY,
    SELL
}

enum class SignalStatus(val label: String) {
    OPEN("Active"),
    TP1_HIT("TP1 Hit"),
    TP2_HIT("TP2 Hit"),
    TP3_HIT("TP3 Hit"),
    SL_HIT("SL Hit"),
    CLOSED("Closed")
}

enum class SubmissionMethod(val label: String) {
    TRADING_ACCOUNT("Trading Account (MT4/MT5)"),
    BROKER_LOGIN("Broker Portal Login")
}

enum class ServiceOption(
    val title: String,
    val profitSharePct: Int,
    val description: String
) {
    ACCOUNT_MANAGEMENT(
        "Account Management",
        40,
        "Professional automated & discretionary execution managed by institutional traders. Transparent 40% high-water mark profit-share. Trade positions adhere to strict capital allocation."
    ),
    LOSS_RECOVERY(
        "Loss Recovery",
        50,
        "Specialized capital stabilization and drawdown rehabilitation strategy for distressed accounts. 50% profit-share arrangement with conservative recovery parameters."
    )
}

data class TradingSignal(
    val id: String,
    val symbol: String,
    val category: SignalCategory,
    val direction: TradeDirection,
    val entryPrice: Double,
    val stopLoss: Double,
    val tp1: Double? = null,
    val tp2: Double? = null,
    val tp3: Double? = null,
    val status: SignalStatus = SignalStatus.OPEN,
    val isVip: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val livePrice: Double? = null
)

data class UserProfile(
    val id: String,
    val email: String,
    val fullName: String,
    val accountStatus: String = "Active",
    val tier: String = "Free Trader",
    val token: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class CopyTradingRequest(
    val id: String,
    val fullName: String,
    val whatsappNumber: String,
    val telegramUsername: String,
    val brokerName: String,
    val platform: String,
    val brokerServer: String,
    val loginId: String,
    val passwordProtected: String,
    val additionalNotes: String,
    val screenshotUri: String? = null,
    val submissionDate: Long = System.currentTimeMillis(),
    val status: String = "Pending"
)

data class AccountManagementRequest(
    val id: String,
    val serviceOption: ServiceOption,
    val submissionMethod: SubmissionMethod,
    val fullName: String,
    val whatsappNumber: String,
    val telegramUsername: String,
    val brokerName: String,
    val platform: String,
    val brokerServer: String,
    val loginId: String,
    val tradingPasswordProtected: String,
    val accountBalance: String,
    val brokerWebsiteOrApp: String,
    val brokerEmail: String,
    val brokerPasswordProtected: String,
    val additionalNotes: String,
    val screenshotUri: String? = null,
    val submissionDate: Long = System.currentTimeMillis(),
    val status: String = "Pending"
)

object SignalSymbols {
    val COMMODITIES_SYMBOLS = listOf(
        "XAUUSD — Gold",
        "XAGUSD — Silver",
        "EURUSD",
        "GBPUSD",
        "USDJPY",
        "AUDUSD",
        "GBPJPY",
        "USDCAD",
        "US30",
        "NASDAQ",
        "SP500"
    )

    val CRYPTO_SYMBOLS = listOf(
        "BTCUSD — Bitcoin",
        "ETHUSD — Ethereum",
        "SOLUSD — Solana",
        "XRPUSD — XRP",
        "LTCUSD — Litecoin",
        "ADAUSD — Cardano",
        "DOGEUSD — Dogecoin",
        "BNBUSD — BNB"
    )
}
