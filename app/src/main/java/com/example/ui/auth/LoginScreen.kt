package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ErrorBanner
import com.example.ui.components.GlowingCard
import com.example.ui.components.M4DarkNavyBg
import com.example.ui.components.M4InputBg
import com.example.ui.components.M4InputBorder
import com.example.ui.components.M4OrangeEnd
import com.example.ui.components.M4OrangeStart
import com.example.ui.components.M4chanicLogo
import com.example.ui.components.ShieldIcon
import com.example.ui.components.WhatsAppIcon

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToOtp: (phone: String, name: String, sessionId: String) -> Unit,
    onRoleSelected: (role: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    var selectedRole by remember { mutableStateOf("customer") } // "customer" or "mechanic"
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.OtpSent -> {
                onRoleSelected(selectedRole)
                onNavigateToOtp(state.phoneNumber, state.fullName, state.sessionId)
                viewModel.resetState()
            }
            is AuthUiState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    val isPhoneValid = phoneNumber.filter { it.isDigit() }.length == 10

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        // Top Left Brand Logo (M4chanic pin + text)
        M4chanicLogo(
            iconSize = 28.dp,
            fontSize = 17,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

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
                // Role Switcher Tabs (Customer vs Mechanic)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(M4InputBg)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedRole == "customer") Brush.horizontalGradient(
                                    listOf(M4OrangeStart, M4OrangeEnd)
                                ) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                            .clickable { selectedRole = "customer" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🚗 Customer",
                            fontSize = 13.sp,
                            fontWeight = if (selectedRole == "customer") FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedRole == "customer") Color.White else Color(0xFF94A3B8)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedRole == "mechanic") Brush.horizontalGradient(
                                    listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                                ) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                            .clickable { selectedRole = "mechanic" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔧 Mechanic",
                            fontSize = 13.sp,
                            fontWeight = if (selectedRole == "mechanic") FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedRole == "mechanic") Color.White else Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Card Header
                Text(
                    text = if (selectedRole == "customer") "Find Nearby Mechanic" else "Mechanic Partner Login",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (selectedRole == "customer")
                        "Instant 24/7 Roadside breakdown assistance"
                    else
                        "Accept roadside jobs & earn daily in your area",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Full Name Input
                CustomDarkInputField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = if (selectedRole == "customer") "Your Full Name" else "Mechanic Full Name",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mobile Number Input with +91 Prefix
                CustomDarkPhoneInputField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        if (digits.length <= 10) {
                            phoneNumber = digits
                        }
                    },
                    placeholder = "Mobile number",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = "Phone",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (isPhoneValid) {
                                errorMessage = null
                                val defaultName = if (selectedRole == "customer") "Customer" else "Mechanic Partner"
                                viewModel.sendOtp(phoneNumber, fullName.ifBlank { defaultName })
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Send OTP Button
                val isButtonLoading = uiState is AuthUiState.Loading
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isPhoneValid && !isButtonLoading) {
                                if (selectedRole == "customer") {
                                    Brush.horizontalGradient(colors = listOf(M4OrangeStart, M4OrangeEnd))
                                } else {
                                    Brush.horizontalGradient(colors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)))
                                }
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        M4OrangeStart.copy(alpha = 0.4f),
                                        M4OrangeEnd.copy(alpha = 0.4f)
                                    )
                                )
                            }
                        )
                        .clickable(
                            enabled = isPhoneValid && !isButtonLoading,
                            onClick = {
                                focusManager.clearFocus()
                                errorMessage = null
                                val defaultName = if (selectedRole == "customer") "Customer" else "Mechanic Partner"
                                viewModel.sendOtp(phoneNumber, fullName.ifBlank { defaultName })
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
                            text = if (selectedRole == "customer") "Continue as Customer" else "Continue as Mechanic",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // WhatsApp Notice
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    WhatsAppIcon(modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "We'll send OTP on WhatsApp",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Safe and secure guarantee
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShieldIcon(
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Verified Roadside Assistance Network",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

/**
 * Custom Dark Theme Input Box matching reference design
 */
@Composable
fun CustomDarkInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        cursorBrush = SolidColor(Color(0xFF00D2FF)),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(M4InputBg)
            .border(
                width = if (isFocused) 1.2.dp else 1.dp,
                color = if (isFocused) Color(0xFF00A3FF) else M4InputBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                leadingIcon()
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/**
 * Custom Dark Theme Phone Input with "+91" prefix
 */
@Composable
fun CustomDarkPhoneInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        ),
        cursorBrush = SolidColor(Color(0xFF00D2FF)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(M4InputBg)
            .border(
                width = if (isFocused) 1.2.dp else 1.dp,
                color = if (isFocused) Color(0xFF00A3FF) else M4InputBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                leadingIcon()
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "+91",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}
