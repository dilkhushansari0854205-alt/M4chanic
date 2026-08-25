package com.example.ui.customer

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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CustomerBottomNavBar
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
import com.example.ui.components.WhatsAppIcon

/**
 * 12. My Orders Screen (Image 4, Screen 1)
 */
@Composable
fun MyOrdersScreen(
    onSelectOrder: (orderId: String) -> Unit,
    onTabSelected: (String) -> Unit
) {
    var selectedOrderTab by remember { mutableStateOf("Completed") } // "Active", "Completed", "Cancelled"

    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(selectedTab = "orders", onTabSelected = onTabSelected)
        },
        containerColor = M4DarkNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(M4DarkNavyBg)
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(
                text = "My orders",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tab selectors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(M4InputBg)
                    .border(1.dp, M4InputBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OrderTabPill("Active", isSelected = selectedOrderTab == "Active", modifier = Modifier.weight(1f)) { selectedOrderTab = "Active" }
                OrderTabPill("Completed", isSelected = selectedOrderTab == "Completed", modifier = Modifier.weight(1f)) { selectedOrderTab = "Completed" }
                OrderTabPill("Cancelled", isSelected = selectedOrderTab == "Cancelled", modifier = Modifier.weight(1f)) { selectedOrderTab = "Cancelled" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedOrderTab == "Completed") {
                    OrderHistoryCard(
                        orderId = "#MCN-784512",
                        date = "24 Aug, 10:30 AM",
                        serviceName = "Puncture Repair",
                        vehicleType = "Car (BR 11 AB 1234)",
                        mechanicName = "Rakesh Kumar",
                        price = 199,
                        status = "Completed",
                        rating = 5,
                        onClick = { onSelectOrder("MCN-784512") }
                    )

                    OrderHistoryCard(
                        orderId = "#MCN-651239",
                        date = "18 Aug, 03:15 PM",
                        serviceName = "Battery Jump Start",
                        vehicleType = "Bike (BR 11 K 9876)",
                        mechanicName = "Sanjay Verma",
                        price = 299,
                        status = "Completed",
                        rating = 4,
                        onClick = { onSelectOrder("MCN-651239") }
                    )
                } else if (selectedOrderTab == "Active") {
                    // Active order card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(M4CardBg)
                            .border(1.dp, M4NeonCyan, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Active Job • #MCN-784512", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(M4ElectricBlue)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "On the way", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Puncture Repair", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Mechanic: Rakesh Kumar (ETA 6 mins)", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            Spacer(modifier = Modifier.height(14.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(M4ElectricBlue)
                                    .clickable { onSelectOrder("MCN-784512") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Track order", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No cancelled orders", color = Color(0xFF64748B), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderTabPill(label: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) M4ElectricBlue else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun OrderHistoryCard(
    orderId: String,
    date: String,
    serviceName: String,
    vehicleType: String,
    mechanicName: String,
    price: Int,
    status: String,
    rating: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = orderId, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(M4SuccessGreen))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = status, fontSize = 12.sp, color = M4SuccessGreen, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = serviceName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "$vehicleType • $mechanicName", fontSize = 12.sp, color = Color(0xFF94A3B8))

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = date, fontSize = 11.sp, color = Color(0xFF64748B))
                Text(text = "₹$price", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
            }
        }
    }
}

/**
 * 13. Order Details Screen (Image 4, Screen 2)
 */
@Composable
fun OrderDetailsScreen(
    orderId: String = "#MCN-784512",
    onBack: () -> Unit,
    onBookAgain: () -> Unit,
    onGetHelp: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                color = M4CardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, M4CardBorder)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(M4InputBg)
                            .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                            .clickable { onGetHelp() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Get help", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(M4ElectricBlue)
                            .clickable { onBookAgain() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Book again", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Order $orderId", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completed Status Badge Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF072718))
                    .border(1.dp, M4SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = M4SuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Service Completed", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "24 Aug 2026 at 10:45 AM", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mechanic & Service Info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Service Summary", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Mechanic", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "Rakesh Kumar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Service", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "Puncture Repair", fontSize = 13.sp, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Vehicle", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "Car (BR 11 AB 1234)", fontSize = 13.sp, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Location", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "Purnia Junction", fontSize = 13.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Receipt Breakdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Payment details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Service fee", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹149", fontSize = 13.sp, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Visiting fee", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹50", fontSize = 13.sp, color = Color.White)
                    }
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total paid", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "₹199", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * 14. Notifications Screen (Image 4, Screen 3)
 */
@Composable
fun CustomerNotificationsScreen(
    onBack: () -> Unit,
    onTabSelected: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(selectedTab = "chat", onTabSelected = onTabSelected)
        },
        containerColor = M4DarkNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(M4DarkNavyBg)
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Notifications", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "Mark all read", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan, modifier = Modifier.clickable { })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NotificationItemCard(
                    title = "Service Completed",
                    desc = "Your order #MCN-784512 has been completed successfully.",
                    time = "10 mins ago",
                    icon = Icons.Filled.CheckCircle,
                    iconTint = M4SuccessGreen
                )
                NotificationItemCard(
                    title = "Mechanic is on the way",
                    desc = "Rakesh Kumar has started traveling to your vehicle location.",
                    time = "35 mins ago",
                    icon = Icons.Filled.DirectionsCar,
                    iconTint = M4ElectricBlue
                )
                NotificationItemCard(
                    title = "New Offer Available",
                    desc = "Get 20% off on battery jump starts this weekend with code SAVE20.",
                    time = "Yesterday",
                    icon = Icons.Filled.Star,
                    iconTint = Color(0xFFFFB800)
                )
            }
        }
    }
}

@Composable
private fun NotificationItemCard(
    title: String,
    desc: String,
    time: String,
    icon: ImageVector,
    iconTint: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(M4InputBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, fontSize = 12.sp, color = Color(0xFF94A3B8), lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = time, fontSize = 10.sp, color = Color(0xFF64748B))
            }
        }
    }
}

/**
 * 15. Customer Profile Screen (Image 4, Screen 4)
 */
@Composable
fun CustomerProfileScreen(
    customerName: String = "Amit Kumar",
    customerPhone: String = "+91 98765 43210",
    onEditProfile: () -> Unit,
    onSavedAddresses: () -> Unit,
    onHelpSupport: () -> Unit,
    onBecomeMechanic: () -> Unit = {},
    onLogout: () -> Unit,
    onTabSelected: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(selectedTab = "profile", onTabSelected = onTabSelected)
        },
        containerColor = M4DarkNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(M4DarkNavyBg)
                .statusBarsPadding()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(text = "Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A5F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = customerName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = customerPhone, fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }

                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = M4NeonCyan, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Become a Mechanic Partner Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0F2A4A), Color(0xFF1E3A8A))
                        )
                    )
                    .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .clickable { onBecomeMechanic() }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Build, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Become a Mechanic Partner", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Accept roadside repair jobs & earn daily", fontSize = 11.sp, color = Color(0xFF93C5FD))
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color(0xFF93C5FD), modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(18.dp))
                    .padding(vertical = 6.dp)
            ) {
                Column {
                    ProfileMenuItem(icon = Icons.Filled.LocationOn, label = "Saved addresses", onClick = onSavedAddresses)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ProfileMenuItem(icon = Icons.Filled.DirectionsCar, label = "My vehicles", onClick = { })
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ProfileMenuItem(icon = Icons.AutoMirrored.Filled.HelpOutline, label = "Help & support", onClick = onHelpSupport)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ProfileMenuItem(icon = Icons.Filled.Security, label = "Privacy policy", onClick = { })
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(M4InputBg)
                    .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .clickable { onLogout() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PowerSettingsNew, contentDescription = "Logout", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Log out", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = M4ElectricBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = label, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
    }
}

/**
 * 16. Edit Customer Profile Screen (Image 5, Screen 1)
 */
@Composable
fun EditCustomerProfileScreen(
    currentName: String = "Amit Kumar",
    currentPhone: String = "9876543210",
    currentEmail: String = "amit.kumar@example.com",
    onBack: () -> Unit,
    onSave: (name: String, email: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }

    Scaffold(
        bottomBar = {
            Surface(
                color = M4CardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, M4CardBorder)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(M4ElectricBlue)
                        .clickable { onSave(name, email) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Save changes", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Edit profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar with camera change button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(80.dp)) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A5F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(M4ElectricBlue)
                            .align(Alignment.BottomEnd)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Change", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Full name", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Email address", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Phone number (verified)", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = "+91 $currentPhone",
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF94A3B8),
                    unfocusedTextColor = Color(0xFF94A3B8),
                    focusedContainerColor = M4InputBg,
                    unfocusedContainerColor = M4InputBg,
                    focusedBorderColor = M4InputBorder,
                    unfocusedBorderColor = M4InputBorder
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

/**
 * 17. Saved Addresses Screen (Image 5, Screen 2)
 */
@Composable
fun SavedAddressesScreen(
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                color = M4CardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, M4CardBorder)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(M4ElectricBlue)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add new address", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Saved addresses", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AddressItemCard(title = "Home", address = "House 14, Main Road, Gulab Bagh, Purnia, Bihar 854326", isDefault = true)
                AddressItemCard(title = "Work", address = "Software Tech Park, Line Bazar, Purnia, Bihar 854301", isDefault = false)
            }
        }
    }
}

@Composable
private fun AddressItemCard(title: String, address: String, isDefault: Boolean) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (title == "Home") Icons.Filled.Home else Icons.Filled.Business, contentDescription = null, tint = M4ElectricBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    if (isDefault) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF003880))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "Default", fontSize = 10.sp, color = M4NeonCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = address, fontSize = 12.sp, color = Color(0xFF94A3B8), lineHeight = 16.sp)
        }
    }
}

/**
 * 18. Help & Support Screen (Image 5, Screen 3)
 */
@Composable
fun CustomerHelpSupportScreen(
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = M4DarkNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(M4DarkNavyBg)
                .statusBarsPadding()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Help & support", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 24x7 Call Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D254C))
                    .border(1.dp, M4ElectricBlue, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(M4ElectricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "24x7 Helpline Support", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "1800-419-4444 (Toll free)", fontSize = 12.sp, color = M4NeonCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // WhatsApp Support Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WhatsAppIcon(modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "Chat on WhatsApp", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Instant response from our team", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Frequently Asked Questions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))

            FaqAccordionItem("How long does it take for a mechanic to arrive?", "Usually between 10 to 20 minutes depending on your exact location and traffic conditions.")
            Spacer(modifier = Modifier.height(8.dp))
            FaqAccordionItem("What if I need to cancel my request?", "Cancellation is completely free until the mechanic starts traveling to your spot.")
            Spacer(modifier = Modifier.height(8.dp))
            FaqAccordionItem("What payment methods are supported?", "We support Cash on Delivery, UPI, Debit/Credit Cards, and Net Banking.")
        }
    }
}

@Composable
private fun FaqAccordionItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(14.dp))
            .clickable { expanded = !expanded }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = question, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(12.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = answer, fontSize = 12.sp, color = Color(0xFF94A3B8), lineHeight = 16.sp)
            }
        }
    }
}
