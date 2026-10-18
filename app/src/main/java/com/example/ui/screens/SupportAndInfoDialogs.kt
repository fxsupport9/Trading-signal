package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppConfig
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradeSell

@Composable
fun AboutUsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("About SignalVerse", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "SignalVerse is a modern institutional mobile trading platform delivering high-conviction Commodities and Crypto signals, automated MT5/MT4 copy execution, and managed account programs.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Key Pillars:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                BulletPoint(text = "Real-Time Market Intelligence: Institutional multi-timeframe analysis across Commodities (Gold, Silver, FX Majors, Indices) and Crypto (BTC, ETH, SOL, and top altcoins).")
                BulletPoint(text = "Non-Custodial Architecture: Funds remain safely inside your regulated broker terminal; we only synchronize algorithmic orders.")
                BulletPoint(text = "Cloud-Verified Backend: Direct PostgreSQL and Supabase integration ensuring authentic, unmanipulated signal records.")
                BulletPoint(text = "Risk Protection: Strict high-water mark profit sharing, zero hidden fees, and predefined loss mitigation algorithms.")
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ContactSupportDialog(
    onDismiss: () -> Unit
) {
    var supportMessage by remember { mutableStateOf("") }
    var ticketSent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Support", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Our 24/5 dedicated desk is available for account setup, copy-trading synchronization, and VIP inquiries.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Support Channels
                SupportChannelRow(
                    icon = Icons.Default.Send,
                    title = "Telegram Official Desk",
                    detail = "@SignalVerseSupport"
                )
                Spacer(modifier = Modifier.height(8.dp))
                SupportChannelRow(
                    icon = Icons.Default.Phone,
                    title = "WhatsApp VIP Hotline",
                    detail = "+1 (800) 555-0199"
                )
                Spacer(modifier = Modifier.height(8.dp))
                SupportChannelRow(
                    icon = Icons.Default.Email,
                    title = "Email Support Desk",
                    detail = "support@signalverse.app"
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                if (ticketSent) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Your message has been sent to our desk. We will respond shortly via Telegram or Email.",
                            color = EmeraldDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Send Quick In-App Message",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = supportMessage,
                        onValueChange = { supportMessage = it },
                        placeholder = { Text("Describe your question or issue...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            focusedLabelColor = EmeraldPrimary
                        )
                    )
                }
            }
        },
        confirmButton = {
            if (!ticketSent) {
                Button(
                    onClick = {
                        if (supportMessage.isNotBlank()) ticketSent = true else onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text(if (supportMessage.isNotBlank()) "Send Message" else "Close")
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            if (!ticketSent) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Privacy Policy", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Last updated: October 2026\n\nSignalVerse ('we', 'us', or 'our') is committed to protecting your privacy and confidential trading data.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                PolicySection(
                    title = "1. Non-Disclosure of Trading Credentials",
                    content = "Broker passwords, login IDs, and server details collected for MT5 Copy Trading or Account Management are encrypted with modern standards and never sold, shared, or displayed in plaintext."
                )

                PolicySection(
                    title = "2. Row Level Security (RLS)",
                    content = "Supabase PostgreSQL tables enforce strict Row Level Security. Requests and profile documents can only be inspected by the originating authenticated user or authorized administrative services."
                )

                PolicySection(
                    title = "3. Screenshot Handling",
                    content = "Screenshots submitted by users are used solely to verify terminal equity and are stored in isolated encrypted storage buckets."
                )

                PolicySection(
                    title = "4. Zero Third-Party Advertising",
                    content = "We do not monetize via advertising networks or tracker SDKs. Your contact information is used strictly to provide synchronization and support."
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("I Understand")
            }
        }
    )
}

@Composable
fun TermsAndConditionsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Terms & Conditions", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Please read these Terms and Conditions carefully before using SignalVerse.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                PolicySection(
                    title = "1. Financial Risk Warning",
                    content = "Trading commodities, foreign exchange (Forex), indices, and cryptocurrency carries substantial risk and may not be suitable for all investors. You may lose some or all of your invested capital."
                )

                PolicySection(
                    title = "2. No Guarantee of Profits",
                    content = "Neither Option A (Account Management - 40% profit share) nor Option B (Loss Recovery - 50% profit share) guarantees profits or full recovery. Market conditions fluctuate dynamically."
                )

                PolicySection(
                    title = "3. Client Responsibility",
                    content = "Clients maintain full responsibility for selecting regulated brokers, maintaining adequate account margin, and observing broker deposit/withdrawal regulations."
                )

                PolicySection(
                    title = "4. Non-Custodial Operation",
                    content = "SignalVerse does not hold custody of client funds. All deposits remain in client-controlled brokerage accounts."
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Accept Terms")
            }
        }
    )
}

@Composable
fun BackendSettingsDialog(
    isConfigured: Boolean,
    statusText: String,
    onSaveConfig: (String, String) -> Unit,
    onTestConnection: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var urlInput by remember { mutableStateOf(AppConfig.getSupabaseUrl(context)) }
    var keyInput by remember { mutableStateOf(AppConfig.getSupabaseAnonKey(context)) }
    var showSqlSchema by remember { mutableStateOf(false) }

    val sqlSchemaText = """
-- ========================================================
-- SIGNALVERSE SUPABASE POSTGRESQL SCHEMA (Copy & Run in SQL Editor)
-- ========================================================

-- 1. Signals Table
CREATE TABLE IF NOT EXISTS public.signals (
    id TEXT PRIMARY KEY DEFAULT gen_random_uuid()::text,
    symbol TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN ('COMMODITIES', 'CRYPTO')),
    direction TEXT NOT NULL CHECK (direction IN ('BUY', 'SELL')),
    entry_price NUMERIC NOT NULL,
    stop_loss NUMERIC NOT NULL,
    tp1 NUMERIC,
    tp2 NUMERIC,
    tp3 NUMERIC,
    status TEXT NOT NULL DEFAULT 'OPEN',
    is_vip BOOLEAN DEFAULT false,
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. MT5 Copy Requests Table
CREATE TABLE IF NOT EXISTS public.copy_requests (
    id TEXT PRIMARY KEY DEFAULT gen_random_uuid()::text,
    full_name TEXT NOT NULL,
    telegram_username TEXT NOT NULL,
    whatsapp_number TEXT NOT NULL,
    platform TEXT NOT NULL,
    login_id TEXT NOT NULL,
    broker_name TEXT NOT NULL,
    broker_server TEXT NOT NULL,
    password_hash TEXT NOT NULL,
    additional_notes TEXT,
    screenshot_uri TEXT,
    status TEXT NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Account Management Requests Table
CREATE TABLE IF NOT EXISTS public.account_management_requests (
    id TEXT PRIMARY KEY DEFAULT gen_random_uuid()::text,
    service_option TEXT NOT NULL,
    submission_method TEXT NOT NULL,
    full_name TEXT NOT NULL,
    whatsapp_number TEXT NOT NULL,
    telegram_username TEXT NOT NULL,
    broker_name TEXT,
    platform TEXT,
    broker_server TEXT,
    login_id TEXT,
    trading_password_hash TEXT,
    account_balance TEXT,
    broker_website_or_app TEXT,
    broker_email TEXT,
    broker_password_hash TEXT,
    additional_notes TEXT,
    screenshot_uri TEXT,
    status TEXT NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. Enable Row Level Security (RLS)
ALTER TABLE public.signals ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.copy_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.account_management_requests ENABLE ROW LEVEL SECURITY;

-- 5. Open Read for signals
CREATE POLICY "Public Read Signals" ON public.signals FOR SELECT USING (true);
CREATE POLICY "Insert Copy Requests" ON public.copy_requests FOR INSERT WITH CHECK (true);
CREATE POLICY "Insert Mgmt Requests" ON public.account_management_requests FOR INSERT WITH CHECK (true);
    """.trimIndent()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Backend & Supabase Setup", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Configure your Supabase project to power live signals, authentication, copy requests, and screenshot storage.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Status Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isConfigured) EmeraldLight else Color(0xFFFEF3C7),
                    border = BorderStroke(1.dp, if (isConfigured) Color(0xFFA7F3D0) else Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isConfigured) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isConfigured) EmeraldDark else Color(0xFFB45309),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isConfigured) EmeraldDark else Color(0xFFB45309)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    label = { Text("SUPABASE_URL") },
                    placeholder = { Text("https://your-id.supabase.co") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("supabase_url_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        focusedLabelColor = EmeraldPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("SUPABASE_ANON_KEY") },
                    placeholder = { Text("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("supabase_key_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        focusedLabelColor = EmeraldPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onTestConnection,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Test Connection", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onSaveConfig(urlInput, keyInput)
                            Toast.makeText(context, "Supabase configuration saved.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save & Apply", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                // Toggle SQL Schema View
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSqlSchema = !showSqlSchema },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PostgreSQL DDL Tables & RLS Rules",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = EmeraldDark
                    )
                    Text(
                        text = if (showSqlSchema) "Hide" else "Show SQL",
                        fontSize = 11.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (showSqlSchema) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF0F172A),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SQL Editor Schema",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("SignalVerse SQL Schema", sqlSchemaText)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "SQL Schema copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy SQL",
                                        tint = Color(0xFFA7F3D0),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sqlSchemaText,
                                color = Color(0xFFE2E8F0),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
private fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
        Text(text, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
    }
}

@Composable
private fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = content, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
    }
}

@Composable
private fun SupportChannelRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    detail: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceVariantLight,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(EmeraldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = EmeraldDark,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = detail, fontSize = 11.sp, color = EmeraldDark, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun MarketingAssetsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play Store Graphics & Assets", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Here are your generated production-ready assets for Google Play Console submission and app branding:",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Feature Graphic (1024x500)
                Text(
                    text = "1. Google Play Feature Graphic (1024 x 500)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = com.example.R.drawable.feature_graphic),
                        contentDescription = "Feature Graphic",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Splash Screen Art (9:16)
                Text(
                    text = "2. Splash Screen & Poster Graphic (9:16)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = com.example.R.drawable.splash_art),
                        contentDescription = "Splash Art",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. App Launcher Icon
                Text(
                    text = "3. Adaptive App Launcher Icon (512 x 512)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_signalverse_logo),
                            contentDescription = "Trading Signals Icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Trading Signals Icon",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Configured across all mipmap densities (mdpi to xxxhdpi) + adaptive vector layers.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Close")
            }
        }
    )
}
