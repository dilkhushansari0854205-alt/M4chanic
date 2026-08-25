package com.example.ui.admin

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.dto.AdminMechanicDetailDto
import com.example.ui.components.M4CardBg
import com.example.ui.components.M4CardBorder
import com.example.ui.components.M4DarkNavyBg
import com.example.ui.components.M4ElectricBlue
import com.example.ui.components.M4InputBg
import com.example.ui.components.M4InputBorder
import com.example.ui.components.M4NeonCyan
import com.example.ui.components.M4OrangeEnd
import com.example.ui.components.M4OrangeStart
import com.example.ui.components.M4SosRed
import com.example.ui.components.M4SuccessGreen
import com.example.ui.components.M4chanicLogo

/**
 * 1. Admin Login & OTP Screen
 */
@Composable
fun AdminLoginScreen(
    viewModel: AdminViewModel,
    onLoginSuccess: () -> Unit,
    onBackToHome: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("7631325464") }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // Rotating neon light effect
    val infiniteTransition = rememberInfiniteTransition(label = "admin_neon")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "admin_angle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackToHome) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shield Logo
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Platform Admin Portal",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Direct Server Access & Partner Management",
            fontSize = 13.sp,
            color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Card Container with Neon Border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeW = 2.dp.toPx()
                    val radius = 24.dp.toPx()
                    drawRoundRect(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6),
                                Color(0xFF06B6D4),
                                Color(0xFF8B5CF6)
                            ),
                            center = Offset(size.width / 2, size.height / 2)
                        ),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
                        style = Stroke(width = strokeW)
                    )
                }
                .clip(RoundedCornerShape(24.dp))
                .background(M4CardBg)
                .padding(20.dp)
        ) {
            Column {
                if (!isOtpSent) {
                    Text(
                        text = "Authorized Admin Phone",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { if (it.length <= 10) phoneNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("7631325464 or 9110106136", color = Color(0xFF64748B), fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFF8B5CF6))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = M4InputBg,
                            unfocusedContainerColor = M4InputBg,
                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = M4InputBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))))
                            .clickable(enabled = !isLoading && phoneNumber.length == 10) {
                                viewModel.sendAdminOtp(phoneNumber) {
                                    isOtpSent = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                        } else {
                            Text(
                                text = "Send Verification OTP",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Enter 6-Digit OTP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1)
                    )
                    Text(
                        text = "Sent via WhatsApp to +91 $phoneNumber",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter 6 digits", color = Color(0xFF64748B)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFF8B5CF6))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = M4InputBg,
                            unfocusedContainerColor = M4InputBg,
                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = M4InputBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))))
                            .clickable(enabled = !isLoading && otp.length == 6) {
                                viewModel.verifyAdminOtp(phoneNumber, otp) {
                                    onLoginSuccess()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                        } else {
                            Text(
                                text = "Verify & Access Admin",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Change Number",
                        color = Color(0xFF8B5CF6),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { isOtpSent = false; otp = "" }
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = M4SosRed,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * 2. Admin Dashboard & Operations Screen
 */
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onNavigateToMechanics: () -> Unit,
    onNavigateToLiveTracking: () -> Unit,
    onLogout: () -> Unit
) {
    val dashboardData by viewModel.dashboardData.collectAsState()
    val pendingMechanics by viewModel.pendingMechanics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        containerColor = M4DarkNavyBg,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Admin Control Room",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Server v4.0.1 • Connected",
                            fontSize = 11.sp,
                            color = M4SuccessGreen
                        )
                    }
                }

                Row {
                    IconButton(onClick = { viewModel.loadDashboard() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Logout", tint = Color(0xFFEF4444))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Stats Grid
            val stats = dashboardData?.stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Users",
                    value = "${stats?.totalUsers ?: 0}",
                    icon = Icons.Default.Group,
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Active Orders",
                    value = "${stats?.activeOrders ?: 0}",
                    icon = Icons.Default.Speed,
                    color = M4OrangeStart,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Mechanics",
                    value = "${stats?.totalMechanics ?: 0}",
                    icon = Icons.Default.DirectionsCar,
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Pending Approvals",
                    value = "${stats?.pendingMechanics ?: 0}",
                    icon = Icons.Default.VerifiedUser,
                    color = if ((stats?.pendingMechanics ?: 0) > 0) Color(0xFFEF4444) else Color(0xFF64748B),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Access Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))))
                        .clickable { onNavigateToMechanics() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Manage Mechanics (${stats?.totalMechanics ?: 0})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.horizontalGradient(listOf(M4OrangeStart, M4OrangeEnd)))
                        .clickable { onNavigateToLiveTracking() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Live Radar Tracking",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pending Approvals Section
            Text(
                text = "Pending Mechanic Approvals (${pendingMechanics.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (pendingMechanics.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(M4CardBg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No pending mechanic verification requests.",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            } else {
                pendingMechanics.forEach { mech ->
                    AdminPendingMechanicCard(
                        mechanic = mech,
                        onApprove = { mech.userId?.let { viewModel.approveMechanic(it) } },
                        onReject = { mech.userId?.let { viewModel.rejectMechanic(it) } }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Orders
            Text(
                text = "Recent Platform Bookings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            val recentOrders = dashboardData?.recentOrders ?: emptyList()
            if (recentOrders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(M4CardBg)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No recent orders logged on server", color = Color(0xFF94A3B8), fontSize = 13.sp)
                }
            } else {
                recentOrders.take(5).forEach { order ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(M4CardBg)
                            .border(1.dp, M4CardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = order.serviceType ?: "Roadside Service",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Customer: ${order.customerName ?: "User"} • Mech: ${order.mechanicName ?: "Assigned"}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            Text(
                                text = (order.status ?: "pending").uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (order.status) {
                                    "completed" -> M4SuccessGreen
                                    "accepted" -> M4NeonCyan
                                    else -> M4OrangeStart
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = Color(0xFF94A3B8))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun AdminPendingMechanicCard(
    mechanic: AdminMechanicDetailDto,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = mechanic.userDetails?.fullName ?: "Mechanic Partner",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Phone: +91 ${mechanic.userDetails?.phoneNumber ?: "N/A"}",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "PENDING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Vehicle: ${mechanic.vehicleDetails?.model ?: "Eeco"} (${mechanic.vehicleDetails?.plateNumber ?: "Plate N/A"})",
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1)
            )
            Text(
                text = "Address: ${mechanic.location?.address ?: "Purnia, Bihar"}",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF10B981))
                        .clickable { onApprove() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓ Approve Partner", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEF4444))
                        .clickable { onReject() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✕ Reject", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
