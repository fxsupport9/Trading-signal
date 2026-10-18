package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccountManagementRequest
import com.example.model.ServiceOption
import com.example.model.SubmissionMethod
import com.example.ui.components.ScreenshotPicker
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusOpen
import com.example.ui.theme.StatusOpenBg
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TradeSell
import com.example.ui.viewmodel.SignalUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountManagementScreen(
    state: SignalUiState,
    onSelectServiceOption: (ServiceOption) -> Unit,
    onSelectSubmissionMethod: (SubmissionMethod) -> Unit,
    onFullNameChange: (String) -> Unit,
    onWhatsappChange: (String) -> Unit,
    onTelegramChange: (String) -> Unit,
    // Method 1
    onBrokerNameChange: (String) -> Unit,
    onPlatformChange: (String) -> Unit,
    onBrokerServerChange: (String) -> Unit,
    onLoginIdChange: (String) -> Unit,
    onTradingPasswordChange: (String) -> Unit,
    onAccountBalanceChange: (String) -> Unit,
    // Method 2
    onBrokerWebsiteOrAppChange: (String) -> Unit,
    onBrokerEmailChange: (String) -> Unit,
    onBrokerPasswordChange: (String) -> Unit,
    // General
    onNotesChange: (String) -> Unit,
    onScreenshotPicked: (String?) -> Unit,
    onSubmitRequest: () -> Unit,
    onDismissSuccessDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("account_mgmt_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header introduction
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                Text(
                    text = "Professional Account Services",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 22.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select an institutional management program tailored to your capital goals and risk profile.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )
            }
        }

        // Two Selectable Service Options Cards (Option A vs Option B)
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Option A: Account Management (40%)
                val isOptA = state.selectedServiceOption == ServiceOption.ACCOUNT_MANAGEMENT
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOptA) Color(0xFFF0FDF4) else SurfaceWhite
                    ),
                    border = BorderStroke(
                        width = if (isOptA) 2.dp else 1.dp,
                        color = if (isOptA) EmeraldPrimary else BorderSubtle
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isOptA) 3.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectServiceOption(ServiceOption.ACCOUNT_MANAGEMENT) }
                        .testTag("option_a_account_mgmt")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isOptA) EmeraldPrimary else EmeraldLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalance,
                                        contentDescription = null,
                                        tint = if (isOptA) Color.White else EmeraldDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Option A — Account Management",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Standard Growth & Scaling",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // 40% Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "40% Profit Share",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldDark,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Institutional portfolio team actively manages day-trading and swing operations with rigorous risk controls. High-water mark calculation applies.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Disclaimer: Financial trading involves substantial risk of loss. Profits are not guaranteed.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Option B: Loss Recovery (50%)
                val isOptB = state.selectedServiceOption == ServiceOption.LOSS_RECOVERY
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOptB) Color(0xFFFFFBEB) else SurfaceWhite
                    ),
                    border = BorderStroke(
                        width = if (isOptB) 2.dp else 1.dp,
                        color = if (isOptB) GoldAccent else BorderSubtle
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isOptB) 3.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectServiceOption(ServiceOption.LOSS_RECOVERY) }
                        .testTag("option_b_loss_recovery")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isOptB) GoldPrimary else GoldLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Healing,
                                        contentDescription = null,
                                        tint = if (isOptB) Color.White else GoldPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Option B — Loss Recovery",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Distressed Account Rehabilitation",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // 50% Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "50% Profit Share",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Specialized hedging and gradual drawdown reduction designed for underwater accounts. Focuses on capital preservation before generating net profits.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Disclaimer: Recovery is not guaranteed and is subject to market volatility and initial available margin.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Active Service Selected Summary Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (state.selectedServiceOption == ServiceOption.ACCOUNT_MANAGEMENT) EmeraldPrimary else GoldPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPLICATION FOR: ${state.selectedServiceOption.title.uppercase()} (${state.selectedServiceOption.profitSharePct}%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contact Details Section
                    Text(
                        text = "1. Contact Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EmeraldDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = state.mgmtFullName,
                        onValueChange = onFullNameChange,
                        label = { Text("Full Name *") },
                        placeholder = { Text("Alex Johnson") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mgmt_full_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            focusedLabelColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = state.mgmtWhatsapp,
                        onValueChange = onWhatsappChange,
                        label = { Text("WhatsApp Number with Country Code *") },
                        placeholder = { Text("+1 234 567 8900") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mgmt_whatsapp_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            focusedLabelColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = state.mgmtTelegram,
                        onValueChange = onTelegramChange,
                        label = { Text("Telegram Username *") },
                        placeholder = { Text("@username") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mgmt_telegram_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            focusedLabelColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Submission Method Selector
                    Text(
                        text = "2. Choose Submission Method",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EmeraldDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceVariantLight)
                            .padding(4.dp)
                    ) {
                        val isMethod1 = state.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isMethod1) EmeraldPrimary else Color.Transparent)
                                .clickable { onSelectSubmissionMethod(SubmissionMethod.TRADING_ACCOUNT) }
                                .padding(vertical = 10.dp)
                                .testTag("select_method_trading_account"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Method 1: MT4/MT5",
                                fontWeight = if (isMethod1) FontWeight.Bold else FontWeight.Medium,
                                color = if (isMethod1) Color.White else TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        val isMethod2 = state.selectedSubmissionMethod == SubmissionMethod.BROKER_LOGIN
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isMethod2) EmeraldPrimary else Color.Transparent)
                                .clickable { onSelectSubmissionMethod(SubmissionMethod.BROKER_LOGIN) }
                                .padding(vertical = 10.dp)
                                .testTag("select_method_broker_login"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Method 2: Broker Portal",
                                fontWeight = if (isMethod2) FontWeight.Bold else FontWeight.Medium,
                                color = if (isMethod2) Color.White else TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // METHOD 1 FIELDS: Trading Account
                    if (state.selectedSubmissionMethod == SubmissionMethod.TRADING_ACCOUNT) {
                        Text(
                            text = "Trading Account Credentials",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = state.mgmtBrokerName,
                            onValueChange = onBrokerNameChange,
                            label = { Text("Broker Name *") },
                            placeholder = { Text("IC Markets, Exness, etc.") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_broker_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Platform toggle inside Method 1
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val isP5 = state.mgmtPlatform == "MT5"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isP5) EmeraldLight else Color(0xFFF1F5F9))
                                    .clickable { onPlatformChange("MT5") }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MT5 Platform",
                                    color = if (isP5) EmeraldDark else TextSecondary,
                                    fontWeight = if (isP5) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                            val isP4 = state.mgmtPlatform == "MT4"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isP4) EmeraldLight else Color(0xFFF1F5F9))
                                    .clickable { onPlatformChange("MT4") }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MT4 Platform",
                                    color = if (isP4) EmeraldDark else TextSecondary,
                                    fontWeight = if (isP4) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtBrokerServer,
                            onValueChange = onBrokerServerChange,
                            label = { Text("Broker Server *") },
                            placeholder = { Text("e.g. Live-01") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_broker_server_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtLoginId,
                            onValueChange = onLoginIdChange,
                            label = { Text("Trading Login ID *") },
                            placeholder = { Text("5209384") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_login_id_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtTradingPassword,
                            onValueChange = onTradingPasswordChange,
                            label = { Text("Trading Account Password *") },
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Visibility"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtAccountBalance,
                            onValueChange = onAccountBalanceChange,
                            label = { Text("Account Balance / Size (USD) *") },
                            placeholder = { Text("e.g. $5,000 or $25,000") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_balance_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )
                    } else {
                        // METHOD 2 FIELDS: Broker Login
                        Text(
                            text = "Broker Member Area Portal Login",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = state.mgmtBrokerWebsiteOrApp,
                            onValueChange = onBrokerWebsiteOrAppChange,
                            label = { Text("Broker Website or App Name *") },
                            placeholder = { Text("e.g. my.exness.com or IC Markets Portal") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_broker_web_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtBrokerEmail,
                            onValueChange = onBrokerEmailChange,
                            label = { Text("Broker Member Email / Login Email *") },
                            placeholder = { Text("investor@example.com") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_broker_email_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.mgmtBrokerPassword,
                            onValueChange = onBrokerPasswordChange,
                            label = { Text("Broker Portal Password *") },
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Visibility"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("mgmt_broker_pass_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Optional Note
                    OutlinedTextField(
                        value = state.mgmtNotes,
                        onValueChange = onNotesChange,
                        label = { Text("Optional Additional Note") },
                        placeholder = { Text("Specific instructions, target drawdown, leverage info") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth().testTag("mgmt_notes_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            focusedLabelColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Optional Screenshot
                    ScreenshotPicker(
                        selectedUri = state.mgmtScreenshotUri,
                        onUriSelected = onScreenshotPicked,
                        title = "Optional Screenshot Upload",
                        subtitle = "Attach account statement or current floating drawdown summary"
                    )

                    // Mandatory Security Notice
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Security Protocol: Credentials are encrypted end-to-end and strictly isolated to authorization and trade synchronization.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Error Banner
                    if (state.mgmtError != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEE2E2),
                            border = BorderStroke(1.dp, Color(0xFFFECACA)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Error",
                                    tint = TradeSell,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = state.mgmtError,
                                    color = TradeSell,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button
                    Button(
                        onClick = onSubmitRequest,
                        enabled = !state.isSubmittingMgmt,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_mgmt_request_button")
                    ) {
                        if (state.isSubmittingMgmt) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting to Secure Backend...", color = Color.White)
                        } else {
                            Text(
                                text = "Submit ${state.selectedServiceOption.title} Request",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Previously Submitted Account Management Requests
        if (state.submittedMgmtRequests.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "YOUR SERVICE REQUESTS (${state.submittedMgmtRequests.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )

                    state.submittedMgmtRequests.forEach { req ->
                        MgmtRequestItemCard(req = req)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Success Confirmation Dialog
    if (state.mgmtSuccessRequest != null) {
        val req = state.mgmtSuccessRequest
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(req.submissionDate))

        AlertDialog(
            onDismissRequest = onDismissSuccessDialog,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request Registered", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your application for ${req.serviceOption.title} (${req.serviceOption.profitSharePct}% profit-share) was successfully saved and queued for review.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceVariantLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Reference ID: ${req.id}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "Service: ${req.serviceOption.title}", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Method: ${req.submissionMethod.label}", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Status: ${req.status}", fontSize = 12.sp, color = EmeraldDark, fontWeight = FontWeight.SemiBold)
                            Text(text = "Date: $dateStr", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissSuccessDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Continue")
                }
            }
        )
    }
}

@Composable
private fun MgmtRequestItemCard(
    req: AccountManagementRequest,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(req.submissionDate))

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = req.serviceOption.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusOpenBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = req.status,
                            color = StatusOpen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Method: ${req.submissionMethod.label}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Submitted $dateStr",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = "Stored",
                tint = EmeraldPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
