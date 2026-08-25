package com.example.ui.job

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.LocationPoint
import com.example.domain.model.OrderStatus
import com.example.ui.components.ErrorBanner
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.components.MechanicActiveJobMap
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.launchDialer
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.OnlineGreenLight
import com.example.ui.theme.PrimaryBlue

@Composable
fun ActiveJobMapScreen(
    orderId: String,
    viewModel: ActiveJobViewModel,
    onBack: () -> Unit,
    onNavigateToDetails: (orderId: String) -> Unit,
    onNavigateToChat: (orderId: String, customerId: String, customerName: String) -> Unit,
    onNavigateToCompleted: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val order by viewModel.order.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isCompleting by viewModel.isCompleting.collectAsState()

    var showCompleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ActiveJobUiState.JobCompleted -> {
                showCompleteDialog = false
                onNavigateToCompleted(state.order.id)
                viewModel.clearState()
            }
            else -> {}
        }
    }

    val currentOrder = order

    if (showCompleteDialog && currentOrder != null) {
        CompleteJobDialog(
            order = currentOrder,
            onDismiss = { showCompleteDialog = false },
            onConfirmComplete = { notes ->
                viewModel.completeJob(currentOrder.id, notes)
            },
            isProcessing = isCompleting
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // 1. Live Active Map Canvas
        val custLoc = currentOrder?.customerLocation ?: LocationPoint(25.7800, 87.4700, "Customer Breakdown Spot")
        val mechLoc = currentLocation ?: LocationPoint(25.7771, 87.4753, "Mechanic Location")

        MechanicActiveJobMap(
            customerLoc = custLoc,
            mechanicLoc = mechLoc,
            customerName = currentOrder?.customerName ?: "Customer",
            serviceType = currentOrder?.serviceType ?: "Repair Service",
            distanceKm = currentOrder?.distanceKm ?: 2.3,
            etaMinutes = currentOrder?.etaMinutes ?: 8,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top App Bar with back & details
        Surface(
            color = Color(0xFF0F172A).copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFF1E293B), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Active Job",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentOrder?.orderNumber ?: "#MCN-${orderId.takeLast(6).uppercase()}",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                IconButton(
                    onClick = { onNavigateToDetails(orderId) },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFF1E293B), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Details",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 3. Bottom Customer & Job Action Card
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Customer Row with Call & Chat buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(PrimaryBlue.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentOrder?.customerName ?: "C").take(1).uppercase(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = currentOrder?.customerName ?: "Customer",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${currentOrder?.vehicle?.brand ?: "Car"} ${currentOrder?.vehicle?.model ?: "Dzire"}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Contact Actions: Call and Chat
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                currentOrder?.let {
                                    onNavigateToChat(it.id, it.customerId, it.customerName)
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Chat,
                                contentDescription = "Chat",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                currentOrder?.customerPhone?.let { phone ->
                                    launchDialer(context, phone)
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(OnlineGreenLight, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = "Call",
                                tint = OnlineGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(14.dp))

                // Breakdown Service & Address Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Handyman,
                                contentDescription = null,
                                tint = ActionOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentOrder?.serviceType ?: "Puncture Repair",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentOrder?.customerLocation?.address ?: "Breakdown Point",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${(currentOrder?.totalAmount ?: 199.0).toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnlineGreen
                        )
                        Text(
                            text = "Cash / UPI",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Complete Job Button
                M4Button(
                    text = "Complete Repair & Collect ₹${(currentOrder?.totalAmount ?: 199.0).toInt()}",
                    onClick = { showCompleteDialog = true },
                    leadingIcon = Icons.Filled.CheckCircle,
                    style = M4ButtonStyle.PRIMARY
                )
            }
        }
    }
}
