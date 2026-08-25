package com.example.ui.customer

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

/**
 * 9. Customer Live Tracking Map Screen (Image 3, Screens 1 & 2)
 */
@Composable
fun CustomerLiveTrackingScreen(
    mechanicName: String = "Rakesh Kumar",
    etaMinutes: Int = 6,
    distanceMeters: String = "800 m",
    orderId: String = "#MCN-784512",
    onBack: () -> Unit,
    onCallMechanic: () -> Unit,
    onChatMechanic: () -> Unit,
    onViewOrderDetails: () -> Unit,
    onCompleteJob: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
    ) {
        // Top Floating Status Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            // Status Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0C2444))
                    .border(1.dp, M4NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(M4SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mechanic is on the way",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // SOS button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(M4SosRed)
                    .clickable { }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = "SOS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // Live Route Map Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LiveRouteTrackingCanvas(modifier = Modifier.fillMaxSize())

            // Simulated floating action to test job completion flow
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(M4ElectricBlue)
                    .clickable { onCompleteJob() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = "Simulate Finish", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Bottom Sheet Card: ETA, Mechanic Info, Quick Action Buttons
        Surface(
            color = M4CardBg,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, M4CardBorder, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // ETA Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Arriving in", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        Text(text = "$etaMinutes mins ($distanceMeters)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                    }

                    // 4-Step Progress Dots
                    TrackingStepsIndicator(currentStep = 2)
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Mechanic Info Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A5F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = mechanicName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Towing & On-spot Repair Specialist", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }

                    // Call Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(M4ElectricBlue)
                            .clickable { onCallMechanic() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Chat Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(M4InputBg)
                            .border(1.dp, M4InputBorder, CircleShape)
                            .clickable { onChatMechanic() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = "Chat", tint = M4NeonCyan, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // View Details Link
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(M4InputBg)
                        .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                        .clickable { onViewOrderDetails() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "View order details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun TrackingStepsIndicator(currentStep: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(4) { idx ->
            Box(
                modifier = Modifier
                    .size(if (idx == currentStep) 10.dp else 7.dp)
                    .clip(CircleShape)
                    .background(if (idx <= currentStep) M4NeonCyan else Color(0xFF1E3554))
            )
        }
    }
}

@Composable
fun LiveRouteTrackingCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "carMove")
    val vehicleProgress by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(animation = tween(6000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "vProg"
    )

    Canvas(modifier = modifier.background(Color(0xFF070F1B))) {
        val w = size.width
        val h = size.height
        val roadColor = Color(0xFF14243B)

        // Road layout
        drawLine(color = roadColor, start = Offset(0f, h * 0.3f), end = Offset(w, h * 0.3f), strokeWidth = 14.dp.toPx())
        drawLine(color = roadColor, start = Offset(w * 0.7f, 0f), end = Offset(w * 0.7f, h), strokeWidth = 14.dp.toPx())
        drawLine(color = roadColor, start = Offset(0f, h * 0.7f), end = Offset(w, h * 0.7f), strokeWidth = 12.dp.toPx())
        drawLine(color = roadColor, start = Offset(w * 0.3f, h * 0.3f), end = Offset(w * 0.3f, h), strokeWidth = 12.dp.toPx())

        // Active Route Line in glowing cyan
        val routePath = Path().apply {
            moveTo(w * 0.2f, h * 0.3f)
            lineTo(w * 0.7f, h * 0.3f)
            lineTo(w * 0.7f, h * 0.7f)
            lineTo(w * 0.85f, h * 0.7f)
        }

        drawPath(
            path = routePath,
            color = M4NeonCyan.copy(alpha = 0.35f),
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = routePath,
            color = M4NeonCyan,
            style = Stroke(
                width = 4.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
            )
        )

        // Customer Destination Pin (Red/Cyan)
        drawCircle(color = M4ElectricBlue, radius = 10.dp.toPx(), center = Offset(w * 0.85f, h * 0.7f))
        drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(w * 0.85f, h * 0.7f))

        // Moving Mechanic Marker
        val mechanicX = w * (0.2f + (0.5f * vehicleProgress))
        val mechanicY = h * 0.3f
        drawCircle(color = M4SuccessGreen, radius = 12.dp.toPx(), center = Offset(mechanicX, mechanicY))
        drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(mechanicX, mechanicY))
    }
}

/**
 * 10. Customer Chat with Mechanic Screen (Image 3, Screen 3)
 */
@Composable
fun CustomerMechanicChatScreen(
    mechanicName: String = "Rakesh Kumar",
    orderId: String = "#MCN-784512",
    onBack: () -> Unit,
    onCall: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var chatMessages by remember {
        mutableStateOf(
            listOf(
                ChatMessageItem("Hello! I am on my way to your location.", false, "10:32 AM"),
                ChatMessageItem("Hi, please bring a puncture kit for car tyre.", true, "10:33 AM"),
                ChatMessageItem("Yes, I have all the tools ready. ETA 5 minutes.", false, "10:34 AM")
            )
        )
    }

    Scaffold(
        bottomBar = {
            // Chat Input Bar
            Surface(
                color = M4CardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, M4CardBorder)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.AttachFile, contentDescription = "Attach", tint = Color(0xFF94A3B8))
                    }

                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...", color = Color(0xFF64748B), fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = M4InputBg,
                            unfocusedContainerColor = M4InputBg,
                            focusedBorderColor = M4ElectricBlue,
                            unfocusedBorderColor = M4InputBorder
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(M4ElectricBlue)
                            .clickable {
                                if (messageText.isNotBlank()) {
                                    chatMessages = chatMessages + ChatMessageItem(messageText, true, "10:35 AM")
                                    messageText = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        },
        containerColor = M4DarkNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(M4DarkNavyBg)
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            // Chat Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E3A5F)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = mechanicName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "$orderId • Online", fontSize = 11.sp, color = M4SuccessGreen)
                }

                IconButton(onClick = onCall) {
                    Icon(Icons.Filled.Phone, contentDescription = "Call", tint = M4NeonCyan)
                }
            }

            // Quick suggestion chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickChatChip("I'm at the location") {
                    chatMessages = chatMessages + ChatMessageItem("I'm at the location", true, "10:35 AM")
                }
                QuickChatChip("Please call me") {
                    chatMessages = chatMessages + ChatMessageItem("Please call me", true, "10:35 AM")
                }
            }

            // Chat Messages List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                chatMessages.forEach { msg ->
                    ChatBubble(message = msg)
                }
            }
        }
    }
}

data class ChatMessageItem(val text: String, val isMe: Boolean, val time: String)

@Composable
private fun ChatBubble(message: ChatMessageItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isMe) 16.dp else 4.dp,
                        bottomEnd = if (message.isMe) 4.dp else 16.dp
                    )
                )
                .background(if (message.isMe) M4ElectricBlue else M4CardBg)
                .border(1.dp, if (message.isMe) M4ElectricBlue else M4CardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(text = message.text, fontSize = 13.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = message.time, fontSize = 10.sp, color = Color(0xFF64748B))
    }
}

@Composable
private fun QuickChatChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(M4InputBg)
            .border(1.dp, M4InputBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = text, fontSize = 11.sp, color = Color(0xFF94A3B8))
    }
}

/**
 * 11. Service Completed & Rating Screen (Image 3, Screen 4)
 */
@Composable
fun ServiceCompletedRatingScreen(
    mechanicName: String = "Rakesh Kumar",
    serviceName: String = "Puncture Repair",
    totalPaid: Int = 199,
    onSubmitRating: (rating: Int, comment: String) -> Unit,
    onSkip: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Green Checkmark
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF063518))
                    .border(2.dp, M4SuccessGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Success", tint = M4SuccessGreen, modifier = Modifier.size(38.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Service completed!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total amount paid ₹$totalPaid",
                fontSize = 14.sp,
                color = M4NeonCyan,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Order Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Mechanic", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = mechanicName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Service done", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = serviceName, fontSize = 13.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Rate Experience
            Text(
                text = "How was your experience?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 5 Star Rating Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { starIndex ->
                    IconButton(onClick = { selectedRating = starIndex }) {
                        Icon(
                            imageVector = if (starIndex <= selectedRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star $starIndex",
                            tint = if (starIndex <= selectedRating) Color(0xFFFFB800) else Color(0xFF64748B),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Optional Feedback Text Field
            OutlinedTextField(
                value = reviewComment,
                onValueChange = { reviewComment = it },
                placeholder = { Text("Leave a note for the mechanic (optional)", color = Color(0xFF64748B), fontSize = 13.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = M4InputBg,
                    unfocusedContainerColor = M4InputBg,
                    focusedBorderColor = M4ElectricBlue,
                    unfocusedBorderColor = M4InputBorder
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(M4OrangeStart, M4OrangeEnd)))
                    .clickable { onSubmitRating(selectedRating, reviewComment) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Submit rating", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Skip for now",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                modifier = Modifier.clickable { onSkip() }
            )
        }
    }
}
