package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CopyTradingRequest
import com.example.ui.components.ScreenshotPicker
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldDeepBackground
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
fun Mt5CopyScreen(
    state: SignalUiState,
    onFullNameChange: (String) -> Unit,
    onTelegramChange: (String) -> Unit,
    onWhatsappChange: (String) -> Unit,
    onPlatformChange: (String) -> Unit,
    onLoginIdChange: (String) -> Unit,
    onBrokerNameChange: (String) -> Unit,
    onBrokerServerChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onScreenshotPicked: (String?) -> Unit,
    onSubmitRequest: () -> Unit,
    onDismissSuccessDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFormExpanded by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val popularBrokers = listOf("IC Markets", "Exness", "Pepperstone", "XM Global", "FP Markets", "RoboForex")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("mt5_copy_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Card & Introduction
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header with badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "INSTITUTIONAL SYNC",
                                color = EmeraldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "MT4 & MT5",
                                color = GoldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Automated MT5 / MT4 Copy Trading",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Synchronize our master portfolio strategies directly to your personal MetaTrader account with zero manual intervention. Your trading capital remains 100% under your ownership with your chosen broker.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Prominent Connect Button
                    Button(
                        onClick = { isFormExpanded = !isFormExpanded },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("connect_mt5_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFormExpanded) "Close Request Form" else "Connect MT5 / MT4 Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Benefits Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = "SERVICE BENEFITS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BenefitCard(
                        icon = Icons.Default.ElectricBolt,
                        title = "< 5ms Execution",
                        description = "Ultra-low latency VPS execution across top-tier liquidity providers.",
                        modifier = Modifier.weight(1f)
                    )
                    BenefitCard(
                        icon = Icons.Default.Security,
                        title = "100% Non-Custodial",
                        description = "Funds never leave your broker. You maintain full deposit/withdrawal control.",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BenefitCard(
                        icon = Icons.Default.Speed,
                        title = "Dynamic Lot Scaling",
                        description = "Automated position sizing tailored proportionally to your account equity.",
                        modifier = Modifier.weight(1f)
                    )
                    BenefitCard(
                        icon = Icons.Default.VerifiedUser,
                        title = "Drawdown Shield",
                        description = "Pre-configured equity stop loss thresholds protecting account principal.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Expandable Copy Trading Request Form
        if (isFormExpanded) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(1.dp, EmeraldLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("mt5_copy_form_card")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "MT5 / MT4 Connection Request",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Fill in your account details to initialize synchronization",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Platform Selector: MT4 vs MT5
                        Text(
                            text = "Platform Choice",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceVariantLight)
                                .padding(4.dp)
                        ) {
                            val isMt5 = state.copyPlatform == "MT5"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isMt5) EmeraldPrimary else Color.Transparent)
                                    .clickable { onPlatformChange("MT5") }
                                    .padding(vertical = 10.dp)
                                    .testTag("select_mt5_platform"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MetaTrader 5 (MT5)",
                                    fontWeight = if (isMt5) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isMt5) Color.White else TextSecondary,
                                    fontSize = 13.sp
                                )
                            }

                            val isMt4 = state.copyPlatform == "MT4"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isMt4) EmeraldPrimary else Color.Transparent)
                                    .clickable { onPlatformChange("MT4") }
                                    .padding(vertical = 10.dp)
                                    .testTag("select_mt4_platform"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MetaTrader 4 (MT4)",
                                    fontWeight = if (isMt4) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isMt4) Color.White else TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Full Name
                        OutlinedTextField(
                            value = state.copyFullName,
                            onValueChange = onFullNameChange,
                            label = { Text("Full Name *") },
                            placeholder = { Text("e.g. Alex Johnson") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_full_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Telegram Username
                        OutlinedTextField(
                            value = state.copyTelegram,
                            onValueChange = onTelegramChange,
                            label = { Text("Telegram Username *") },
                            placeholder = { Text("@username") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_telegram_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // WhatsApp Number
                        OutlinedTextField(
                            value = state.copyWhatsapp,
                            onValueChange = onWhatsappChange,
                            label = { Text("WhatsApp Number with Country Code *") },
                            placeholder = { Text("+1 234 567 8900") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_whatsapp_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Broker Name & Quick Suggestion Chips
                        OutlinedTextField(
                            value = state.copyBrokerName,
                            onValueChange = onBrokerNameChange,
                            label = { Text("Broker Name *") },
                            placeholder = { Text("e.g. IC Markets, Exness") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_broker_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(popularBrokers) { broker ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFF1F5F9),
                                    border = BorderStroke(0.5.dp, Color(0xFFCBD5E1)),
                                    modifier = Modifier.clickable { onBrokerNameChange(broker) }
                                ) {
                                    Text(
                                        text = broker,
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Broker Server
                        OutlinedTextField(
                            value = state.copyBrokerServer,
                            onValueChange = onBrokerServerChange,
                            label = { Text("Broker Server *") },
                            placeholder = { Text("e.g. ICMarketsSC-Live02") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_server_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Trading Login ID
                        OutlinedTextField(
                            value = state.copyLoginId,
                            onValueChange = onLoginIdChange,
                            label = { Text("Trading Login ID *") },
                            placeholder = { Text("e.g. 5209384") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_login_id_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Trading Account Password
                        OutlinedTextField(
                            value = state.copyPassword,
                            onValueChange = onPasswordChange,
                            label = { Text("Trading Account Password *") },
                            placeholder = { Text("Investor or Master Password") },
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Password Visibility"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        // Privacy & Protection Note
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Credentials are encrypted and protected by strict RLS access controls.",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Optional Note / Details
                        OutlinedTextField(
                            value = state.copyNotes,
                            onValueChange = onNotesChange,
                            label = { Text("Optional Note / Risk Preference") },
                            placeholder = { Text("e.g. Max 1% risk per trade, conservative lot multiplier") },
                            minLines = 2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("copy_notes_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                focusedLabelColor = EmeraldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Optional Screenshot Upload
                        ScreenshotPicker(
                            selectedUri = state.copyScreenshotUri,
                            onUriSelected = onScreenshotPicked,
                            title = "Account Statement Screenshot (Optional)",
                            subtitle = "Verify balance or margin level from your terminal"
                        )

                        // Error Banner if validation or backend failure
                        if (state.copyError != null) {
                            Spacer(modifier = Modifier.height(12.dp))
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
                                        text = state.copyError,
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
                            enabled = !state.isSubmittingCopy,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_copy_request_button")
                        ) {
                            if (state.isSubmittingCopy) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Encrypting & Submitting...", color = Color.White)
                            } else {
                                Text(
                                    text = "Submit Copy Request",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Previously Submitted Copy Requests Section
        if (state.submittedCopyRequests.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "YOUR COPY REQUESTS (${state.submittedCopyRequests.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )

                    state.submittedCopyRequests.forEach { req ->
                        CopyRequestItemCard(req = req)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Confirmation Success Dialog
    if (state.copySuccessRequest != null) {
        val req = state.copySuccessRequest
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
                    Text("Copy Request Submitted", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your ${req.platform} copy trading request has been securely recorded on the backend and sent to our administrative deployment team.",
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
                            Text(text = "Broker: ${req.brokerName} (${req.brokerServer})", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Login ID: ${req.loginId}", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Status: ${req.status}", fontSize = 12.sp, color = EmeraldDark, fontWeight = FontWeight.SemiBold)
                            Text(text = "Date: $dateStr", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismissSuccessDialog()
                        isFormExpanded = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun BenefitCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun CopyRequestItemCard(
    req: CopyTradingRequest,
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
                        text = "${req.platform} • #${req.loginId}",
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
                    text = "${req.brokerName} (${req.brokerServer})",
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
