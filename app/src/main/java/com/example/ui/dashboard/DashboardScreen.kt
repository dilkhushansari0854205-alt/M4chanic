package com.example.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.components.ErrorBanner
import com.example.ui.components.M4BottomNavBar
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.components.MechanicBottomTab
import com.example.ui.components.MechanicOnlineToggleSwitch
import com.example.ui.components.OnlineBadge
import com.example.ui.components.SocketDisconnectedBanner
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.DarkBlue
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.OnlineGreenLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.StarGold
import com.example.ui.theme.WarningYellow

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTabSelected: (MechanicBottomTab) -> Unit,
    onNavigateToJobAccepted: (orderId: String) -> Unit,
    onNavigateToActiveMap: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.mechanicProfile.collectAsState()
    val isSocketConnected by viewModel.isSocketConnected.collectAsState()
    val activeOrder by viewModel.activeOrder.collectAsState()
    val incomingOrder by viewModel.incomingOrder.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isToggling by viewModel.isTogglingStatus.collectAsState()
    val isResponding by viewModel.isRespondingJob.collectAsState()

    val isOnline = profile?.isOnline ?: false

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is DashboardUiState.JobAccepted -> {
                onNavigateToJobAccepted(state.order.id)
                viewModel.clearError()
            }
            else -> {}
        }
    }

    // Modal incoming request dialog
    incomingOrder?.let { event ->
        IncomingOrderDialog(
            event = event,
            onAccept = { viewModel.acceptIncomingOrder(it) },
            onReject = { viewModel.rejectIncomingOrder(it) },
            isProcessing = isResponding
        )
    }

    Scaffold(
        bottomBar = {
            M4BottomNavBar(
                selectedTab = MechanicBottomTab.HOME,
                onTabSelected = onTabSelected
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // 1. Top Header Bar
            DashboardTopBar(
                partnerName = profile?.fullName ?: "Mechanic Partner",
                rating = profile?.rating ?: 4.8,
                isOnline = isOnline,
                isToggling = isToggling,
                onToggleOnline = { viewModel.toggleOnlineStatus(it) }
            )

            // Socket Banner if disconnected
            if (!isSocketConnected) {
                SocketDisconnectedBanner()
            }

            if (uiState is DashboardUiState.Error) {
                ErrorBanner(
                    message = (uiState as DashboardUiState.Error).message,
                    onDismiss = { viewModel.clearError() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // 2. Active Job Sticky Card (If active job exists)
            activeOrder?.let { active ->
                if (active.status != OrderStatus.COMPLETED && active.status != OrderStatus.REJECTED) {
                    ActiveJobNotificationCard(
                        order = active,
                        onResume = { onNavigateToActiveMap(active.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // 3. Body: Online vs Offline state
            if (!isOnline) {
                OfflineDashboardContent(
                    profile = profile,
                    onGoOnline = { viewModel.toggleOnlineStatus(true) },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
            } else {
                OnlineDashboardContent(
                    profile = profile,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun DashboardTopBar(
    partnerName: String,
    rating: Double,
    isOnline: Boolean,
    isToggling: Boolean,
    onToggleOnline: (Boolean) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Partner Profile Avatar & Details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(PrimaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = partnerName.take(1).uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = partnerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", rating),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OnlineBadge(isOnline = isOnline)
                    }
                }
            }

            // Online Switch
            MechanicOnlineToggleSwitch(
                isOnline = isOnline,
                onToggle = onToggleOnline,
                isLoading = isToggling
            )
        }
    }
}

@Composable
private fun OfflineDashboardContent(
    profile: com.example.domain.model.MechanicProfile?,
    onGoOnline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Big Offline Graphic
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFF334155).copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF475569), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PowerSettingsNew,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You're Currently Offline",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Switch Online to start receiving immediate customer breakdown requests in your service radius.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        M4Button(
            text = "GO ONLINE",
            onClick = onGoOnline,
            leadingIcon = Icons.Filled.PowerSettingsNew,
            style = M4ButtonStyle.PRIMARY,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Today's Stats Card
        Text(
            text = "Today's Performance",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Today's Earnings",
                value = "₹${(profile?.earnings?.today ?: 0.0).toInt()}",
                icon = Icons.Filled.AccountBalanceWallet,
                iconTint = OnlineGreen,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Completed",
                value = "${profile?.completedJobs ?: 0} Jobs",
                icon = Icons.Filled.AssignmentTurnedIn,
                iconTint = PrimaryBlue,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Partner Tips
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = ActionOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tips to Earn More",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Keep your phone charged and GPS on high accuracy\n• Respond to incoming requests within 30 seconds\n• Carry standard tyre puncture strips & jumpstart cables",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun OnlineDashboardContent(
    profile: com.example.domain.model.MechanicProfile?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Radar Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive Radar Sweep Graphic
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color(0xFF0F172A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val maxR = size.width * 0.45f

                        // Outer Pulse
                        drawCircle(
                            color = OnlineGreen.copy(alpha = pulseAlpha),
                            radius = maxR * pulseScale,
                            center = center
                        )

                        // Concentric grid circles
                        drawCircle(
                            color = OnlineGreen.copy(alpha = 0.3f),
                            radius = maxR,
                            center = center,
                            style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                        )
                        drawCircle(
                            color = OnlineGreen.copy(alpha = 0.2f),
                            radius = maxR * 0.6f,
                            center = center,
                            style = Stroke(width = 1.5f)
                        )
                    }

                    // Center Mechanic Pin
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(OnlineGreen, CircleShape)
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Handyman,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(OnlineGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SEARCHING FOR BREAKDOWNS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnlineGreen,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Live in ${profile?.workAddress ?: "Purnia Base"} • 15 km Radius",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Today's Earned",
                value = "₹${(profile?.earnings?.today ?: 0.0).toInt()}",
                icon = Icons.Filled.AccountBalanceWallet,
                iconTint = OnlineGreen,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Jobs Today",
                value = "${profile?.completedJobs ?: 0}",
                icon = Icons.Filled.AssignmentTurnedIn,
                iconTint = PrimaryBlue,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Rating",
                value = "${profile?.rating ?: 4.8} ★",
                icon = Icons.Filled.Star,
                iconTint = StarGold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Ready Status Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = OnlineGreenLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = OnlineGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "GPS Tracking Active",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnlineGreen
                    )
                    Text(
                        text = "You will hear a chime and prompt as soon as a breakdown occurs near you.",
                        fontSize = 12.sp,
                        color = Color(0xFF166534)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveJobNotificationCard(
    order: com.example.domain.model.Order,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ActionOrange.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, ActionOrange),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onResume() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ActionOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Handyman,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "ACTIVE JOB IN PROGRESS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ActionOrange
                    )
                    Text(
                        text = "${order.customerName} • ${order.serviceType}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${order.distanceKm} km • ${order.status.label}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ActionOrange
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resume",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(iconTint.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
