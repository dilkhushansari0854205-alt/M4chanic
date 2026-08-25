package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.session.SessionManager
import com.example.ui.components.ErrorBanner
import com.example.ui.components.GlowingCard
import com.example.ui.components.M4DarkNavyBg
import com.example.ui.components.M4ElectricBlue
import com.example.ui.components.M4InputBg
import com.example.ui.components.M4InputBorder
import com.example.ui.components.M4OrangeEnd
import com.example.ui.components.M4chanicLogo

@Composable
fun OtpScreen(
    phoneNumber: String,
    fullName: String,
    sessionId: String,
    viewModel: AuthViewModel,
    sessionManager: SessionManager,
    onBack: () -> Unit,
    onNavigateNext: (destination: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val countdown by viewModel.resendCountdown.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> {
                val role = sessionManager.getRole()
                val status = sessionManager.getMechanicStatus()
                val activeOrder = sessionManager.getActiveOrderId()

                if (!activeOrder.isNullOrBlank()) {
                    onNavigateNext("active_job_map/$activeOrder")
                } else if (role.equals("customer", ignoreCase = true)) {
                    onNavigateNext("customer_home")
                } else if (status.equals("pending", ignoreCase = true)) {
                    onNavigateNext("approval_pending")
                } else if (status.equals("rejected", ignoreCase = true)) {
                    onNavigateNext("registration_rejected")
                } else if (status.equals("suspended", ignoreCase = true)) {
                    onNavigateNext("account_suspended")
                } else if (status.equals("approved", ignoreCase = true) || status.equals("active", ignoreCase = true)) {
                    onNavigateNext("dashboard")
                } else {
                    onNavigateNext("mechanic_intro")
                }
                viewModel.resetState()
            }
            is AuthUiState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    // Mask phone number: +91 98•••••210 (Matching exact mockup)
    val maskedPhone = if (phoneNumber.length >= 10) {
        val start = phoneNumber.take(2)
        val end = phoneNumber.takeLast(3)
        "+91 $start•••••$end"
    } else {
        "+91 $phoneNumber"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        // Top Left Brand Logo (M4chanic)
        M4chanicLogo(
            iconSize = 28.dp,
            fontSize = 17,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        Spacer(modifier = Modifier.weight(0.15f))

        if (errorMessage != null) {
            ErrorBanner(
                message = errorMessage ?: "",
                onDismiss = { errorMessage = null },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Centered Glowing Card (Exact match to reference mockup)
        GlowingCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Card Header
                Text(
                    text = "Verify your number",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Enter the 6-digit code sent to",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = maskedPhone,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 6-digit Individual Square Digit Boxes
                BasicTextField(
                    value = otp,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        if (digits.length <= 6) {
                            otp = digits
                            if (digits.length == 6) {
                                focusManager.clearFocus()
                                errorMessage = null
                                viewModel.verifyOtp(phoneNumber, digits, sessionId, fullName)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (otp.length == 6) {
                                errorMessage = null
                                viewModel.verifyOtp(phoneNumber, otp, sessionId, fullName)
                            }
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth(),
                    decorationBox = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (i in 0 until 6) {
                                val char = otp.getOrNull(i)?.toString() ?: ""
                                val isCurrent = otp.length == i

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(M4InputBg)
                                        .border(
                                            width = if (isCurrent) 1.5.dp else 1.dp,
                                            color = if (isCurrent) Color(0xFF00D2FF) else M4InputBorder,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Resend Timer Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (countdown <= 0) {
                        Text(
                            text = "Resend OTP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = M4OrangeEnd,
                            modifier = Modifier.clickable {
                                viewModel.resendOtp(phoneNumber, fullName)
                            }
                        )
                    } else {
                        val formatted = String.format("%02d", countdown)
                        Text(
                            text = "Resend OTP in ",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "00:$formatted",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = M4OrangeEnd
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Verify & Continue Button (Electric Royal Blue)
                val isButtonLoading = uiState is AuthUiState.Loading
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (otp.length == 6 && !isButtonLoading) {
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF0066FF), Color(0xFF0052CC))
                                )
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF0066FF).copy(alpha = 0.5f),
                                        Color(0xFF0052CC).copy(alpha = 0.5f)
                                    )
                                )
                            }
                        )
                        .clickable(
                            enabled = otp.length == 6 && !isButtonLoading,
                            onClick = {
                                focusManager.clearFocus()
                                errorMessage = null
                                viewModel.verifyOtp(phoneNumber, otp, sessionId, fullName)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isButtonLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "Verify & Continue",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Change number link
                Text(
                    text = "Change number",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.85f))
    }
}
