package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ScreenshotPicker
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldDeepBackground
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary
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
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Intro Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldDeepBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(EmeraldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Institutional MT5 Copy Trading",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp,
                                    color = SurfaceWhite
                                )
                                Text(
                                    text = "Automated execution on your broker account",
                                    fontSize = 12.sp,
                                    color = EmeraldLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Connect your MetaTrader 4 / 5 terminal to our high-frequency low-latency server. Every winning signal will execute instantly with customized lot sizes and automatic risk limits.",
                            fontSize = 12.sp,
                            color = SurfaceWhite.copy(alpha = 0.9f),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Connection Request Form
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mt5_copy_form")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Submit Account for Copy Trading",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Credentials are encrypted with institutional security",
                            fontSize = 11.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Platform Selector: MT4 vs MT5
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = state.copyPlatform == "MT5",
                                onClick = { onPlatformChange("MT5") },
                                label = { Text("MetaTrader 5 (MT5)", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldLight,
                                    selectedLabelColor = EmeraldDark
                                )
                            )
                            FilterChip(
                                selected = state.copyPlatform == "MT4",
                                onClick = { onPlatformChange("MT4") },
                                label = { Text("MetaTrader 4 (MT4)", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldLight,
                                    selectedLabelColor = EmeraldDark
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = state.copyFullName,
                            onValueChange = onFullNameChange,
                            label = { Text("Trader Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("copy_name_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = state.copyWhatsapp,
                                onValueChange = onWhatsappChange,
                                label = { Text("WhatsApp (with code)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldDark) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_whatsapp_input")
                            )

                            OutlinedTextField(
                                value = state.copyTelegram,
                                onValueChange = onTelegramChange,
                                label = { Text("Telegram (@username)") },
                                leadingIcon = { Icon(Icons.Default.Send, contentDescription = null, tint = EmeraldDark) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_telegram_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = state.copyBrokerName,
                                onValueChange = onBrokerNameChange,
                                label = { Text("Broker Name (e.g. Exness)") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_broker_input")
                            )

                            OutlinedTextField(
                                value = state.copyBrokerServer,
                                onValueChange = onBrokerServerChange,
                                label = { Text("Server Name") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_server_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = state.copyLoginId,
                                onValueChange = onLoginIdChange,
                                label = { Text("Login / Account ID") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_login_input")
                            )

                            OutlinedTextField(
                                value = state.copyPassword,
                                onValueChange = onPasswordChange,
                                label = { Text("Trading Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldDark) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                modifier = Modifier.weight(1f).testTag("copy_password_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = state.copyNotes,
                            onValueChange = onNotesChange,
                            label = { Text("Lot Size & Risk Notes (Optional)") },
                            singleLine = false,
                            maxLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Screenshot Picker
                        ScreenshotPicker(
                            selectedUri = state.copyScreenshotUri,
                            onUriSelected = onScreenshotPicked,
                            title = "Account Verification Screenshot",
                            subtitle = "Attach screenshot of MT5 account balance & broker details"
                        )

                        if (state.copyError != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = state.copyError,
                                color = TradeSell,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onSubmitRequest,
                            enabled = !state.isSubmittingCopy,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("copy_submit_button")
                        ) {
                            if (state.isSubmittingCopy) {
                                CircularProgressIndicator(color = SurfaceWhite, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Connect My Account to Live Copier", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Submitted Requests History
            if (state.submittedCopyRequests.isNotEmpty()) {
                item {
                    Text(
                        text = "YOUR CONNECTED COPY TRADING ACCOUNTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(state.submittedCopyRequests) { item ->
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val date = dateFormat.format(Date(item.submissionDate))

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${item.brokerName} · ${item.platform}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmeraldLight)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Login ID: ${item.loginId} · Server: ${item.brokerServer}", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Submitted on $date", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }

    // Success Dialog
    if (state.copySuccessRequest != null) {
        AlertDialog(
            onDismissRequest = onDismissSuccessDialog,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Account Connected!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Your ${state.copySuccessRequest.platform} account (${state.copySuccessRequest.loginId} on ${state.copySuccessRequest.brokerName}) was transmitted successfully. Our trading desk will verify your login and commence automated signal copying within 1 hour.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = onDismissSuccessDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("OK, Great!")
                }
            }
        )
    }
}
