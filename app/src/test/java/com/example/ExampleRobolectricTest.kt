package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LocalDataStore
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SignalCategory
import com.example.model.SignalSymbols
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies SignalVerse app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Trading Signals", appName)
        assertEquals("ca-app-pub-1895906484640218~9597991631", com.example.data.AdMobManager.APP_ID)
        assertEquals("ca-app-pub-1895906484640218/1337367399", com.example.data.AdMobManager.BANNER_AD_UNIT_ID)
    }

    @Test
    fun `verify signal categories and symbols match specifications`() {
        // Commodities specification verification
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("XAUUSD — Gold"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("XAGUSD — Silver"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("EURUSD"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("GBPUSD"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("USDJPY"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("AUDUSD"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("GBPJPY"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("USDCAD"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("US30"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("NASDAQ"))
        assertTrue(SignalSymbols.COMMODITIES_SYMBOLS.contains("SP500"))

        // Crypto specification verification
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("BTCUSD — Bitcoin"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("ETHUSD — Ethereum"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("SOLUSD — Solana"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("XRPUSD — XRP"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("LTCUSD — Litecoin"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("ADAUSD — Cardano"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("DOGEUSD — Dogecoin"))
        assertTrue(SignalSymbols.CRYPTO_SYMBOLS.contains("BNBUSD — BNB"))
    }

    @Test
    fun `verify account management profit share specifications`() {
        assertEquals(40, ServiceOption.ACCOUNT_MANAGEMENT.profitSharePct)
        assertEquals(50, ServiceOption.LOSS_RECOVERY.profitSharePct)
    }

    @Test
    fun `verify local datastore persists copy requests and avoids duplicate login IDs`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val store = LocalDataStore(context)

        val req1 = CopyTradingRequest(
            id = "test_1",
            fullName = "Trader Alex",
            telegramUsername = "@alex_trader",
            whatsappNumber = "+1234567890",
            platform = "MT5",
            loginId = "100234",
            brokerName = "IC Markets",
            brokerServer = "ICMarkets-Live",
            passwordProtected = "secret"
        )

        store.saveCopyRequest(req1)
        val list = store.getCopyRequests()
        assertTrue(list.any { it.loginId == "100234" })

        // Re-saving with same login ID updates rather than duplicates
        store.saveCopyRequest(req1.copy(fullName = "Trader Alex Updated"))
        val list2 = store.getCopyRequests()
        val matches = list2.filter { it.loginId == "100234" }
        assertEquals(1, matches.size)
    }
}
