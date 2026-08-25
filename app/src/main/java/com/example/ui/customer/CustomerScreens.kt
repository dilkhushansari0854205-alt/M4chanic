package com.example.ui.customer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.ui.components.M4chanicLogo
import com.example.ui.components.TowTruckIllustration
import kotlinx.coroutines.delay

/**
 * 1. Customer Home Dashboard (Image 1, Screen 1)
 */
@Composable
fun CustomerHomeScreen(
    customerName: String = "Amit",
    currentLocation: String = "Purnia, Bihar",
    onSelectService: (serviceName: String, price: Int) -> Unit,
    onNavigateAllServices: () -> Unit,
    onNavigateChooseLocation: () -> Unit,
    onNavigateMechanicsNearYou: () -> Unit,
    onNavigateEmergency: () -> Unit,
    onTabSelected: (String) -> Unit
) {
    var selectedVehicle by remember { mutableStateOf("Bike") }

    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(
                selectedTab = "home",
                onTabSelected = onTabSelected
            )
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
            // Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                M4chanicLogo(iconSize = 26.dp, fontSize = 17)

                // Location Picker Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(M4InputBg)
                        .border(1.dp, M4InputBorder, RoundedCornerShape(20.dp))
                        .clickable { onNavigateChooseLocation() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = M4ElectricBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentLocation,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Notification icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(M4InputBg)
                            .border(1.dp, M4InputBorder, CircleShape)
                            .clickable { onTabSelected("chat") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // SOS Red Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(M4SosRed)
                            .clickable { onNavigateEmergency() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SOS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Greeting text
            Text(
                text = "Good morning, $customerName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Reliable roadside help, right when you need it.",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Banner: "Stuck on the road? We'll be there in minutes."
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0D254C), Color(0xFF091426), Color(0xFF08101E))
                        )
                    )
                    .border(1.dp, Color(0xFF1E3A5F), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1.1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Stuck on the road?",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We'll be there in minutes.",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Get Help Button (Vibrant Orange)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(M4OrangeStart, M4OrangeEnd)
                                    )
                                )
                                .clickable { onNavigateMechanicsNearYou() }
                                .padding(horizontal = 18.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Get Help",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Vector Tow Truck graphic
                    TowTruckIllustration(
                        modifier = Modifier
                            .weight(0.9f)
                            .height(110.dp),
                        lineColor = Color(0xFF00D2FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Vehicle Selector Tabs: Bike, Car, Heavy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VehicleTabItem(
                    label = "Bike",
                    icon = Icons.Filled.TwoWheeler,
                    isSelected = selectedVehicle == "Bike",
                    onClick = { selectedVehicle = "Bike" },
                    modifier = Modifier.weight(1f)
                )
                VehicleTabItem(
                    label = "Car",
                    icon = Icons.Filled.DirectionsCar,
                    isSelected = selectedVehicle == "Car",
                    onClick = { selectedVehicle = "Car" },
                    modifier = Modifier.weight(1f)
                )
                VehicleTabItem(
                    label = "Heavy",
                    icon = Icons.Filled.LocalShipping,
                    isSelected = selectedVehicle == "Heavy",
                    onClick = { selectedVehicle = "Heavy" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 6 Grid Services
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceGridCard(
                    title = "Puncture Repair",
                    subtitle = "On-spot tyre repair",
                    price = 149,
                    iconType = "puncture",
                    onClick = { onSelectService("Puncture Repair", 149) },
                    modifier = Modifier.weight(1f)
                )
                ServiceGridCard(
                    title = "Battery Jump Start",
                    subtitle = "Quick start for your battery",
                    price = 299,
                    iconType = "battery",
                    onClick = { onSelectService("Battery Jump Start", 299) },
                    modifier = Modifier.weight(1f)
                )
                ServiceGridCard(
                    title = "Engine Problem",
                    subtitle = "Diagnosis & repair",
                    price = 399,
                    iconType = "engine",
                    onClick = { onSelectService("Engine Problem Diagnosis", 399) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceGridCard(
                    title = "Out of Fuel",
                    subtitle = "We'll bring fuel to you",
                    price = 299,
                    iconType = "fuel",
                    onClick = { onSelectService("Out of Fuel Delivery", 299) },
                    modifier = Modifier.weight(1f)
                )
                ServiceGridCard(
                    title = "Key Lockout",
                    subtitle = "Locked out? We'll help",
                    price = 249,
                    iconType = "key",
                    onClick = { onSelectService("Key Lockout Assistance", 249) },
                    modifier = Modifier.weight(1f)
                )
                ServiceGridCard(
                    title = "Towing Service",
                    subtitle = "Safe towing anytime",
                    price = 799,
                    iconType = "tow",
                    onClick = { onSelectService("Towing Service", 799) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun VehicleTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) M4ElectricBlue else M4InputBg)
            .border(
                1.dp,
                if (isSelected) M4ElectricBlue else M4InputBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun ServiceGridCard(
    title: String,
    subtitle: String,
    price: Int,
    iconType: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(138.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom Service Vector Icon
            ServiceCanvasIcon(iconType = iconType, modifier = Modifier.size(36.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    lineHeight = 11.sp
                )
            }

            Text(
                text = "From ₹$price",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = M4NeonCyan
            )
        }
    }
}

@Composable
fun ServiceCanvasIcon(iconType: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (iconType) {
            "puncture" -> {
                // Tyre with tread marks
                drawCircle(color = Color(0xFF1E293B), radius = w * 0.45f, center = Offset(w/2f, h/2f))
                drawCircle(color = Color(0xFF00D2FF), radius = w * 0.45f, center = Offset(w/2f, h/2f), style = Stroke(width = 3.dp.toPx()))
                drawCircle(color = Color(0xFF64748B), radius = w * 0.22f, center = Offset(w/2f, h/2f), style = Stroke(width = 2.dp.toPx()))
                drawCircle(color = Color.White, radius = w * 0.08f, center = Offset(w/2f, h/2f))
            }
            "battery" -> {
                // Battery with lightning pulse
                drawRoundRect(
                    color = M4ElectricBlue,
                    topLeft = Offset(w * 0.15f, h * 0.3f),
                    size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.55f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                drawRect(color = Color.White, topLeft = Offset(w * 0.28f, h * 0.18f), size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.12f))
                drawRect(color = Color.White, topLeft = Offset(w * 0.58f, h * 0.18f), size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.12f))
                // Bolt
                val bolt = Path().apply {
                    moveTo(w * 0.52f, h * 0.36f)
                    lineTo(w * 0.42f, h * 0.55f)
                    lineTo(w * 0.52f, h * 0.55f)
                    lineTo(w * 0.46f, h * 0.74f)
                    lineTo(w * 0.60f, h * 0.50f)
                    lineTo(w * 0.50f, h * 0.50f)
                    close()
                }
                drawPath(path = bolt, color = Color.White, style = Fill)
            }
            "engine" -> {
                // Engine block in neon cyan
                drawRoundRect(
                    color = Color(0xFF0077FF),
                    topLeft = Offset(w * 0.2f, h * 0.3f),
                    size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.45f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawLine(color = Color(0xFF00D2FF), start = Offset(w*0.1f, h*0.4f), end = Offset(w*0.2f, h*0.4f), strokeWidth = 3.dp.toPx())
                drawLine(color = Color(0xFF00D2FF), start = Offset(w*0.8f, h*0.4f), end = Offset(w*0.9f, h*0.4f), strokeWidth = 3.dp.toPx())
                drawLine(color = Color.White, start = Offset(w*0.35f, h*0.2f), end = Offset(w*0.65f, h*0.2f), strokeWidth = 2.5.dp.toPx())
            }
            "fuel" -> {
                // Jerrycan / fuel dispenser in orange
                drawRoundRect(
                    color = M4OrangeStart,
                    topLeft = Offset(w * 0.22f, h * 0.28f),
                    size = androidx.compose.ui.geometry.Size(w * 0.56f, h * 0.58f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                drawCircle(color = Color.White, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.56f))
                drawLine(color = Color.White, start = Offset(w*0.35f, h*0.2f), end = Offset(w*0.65f, h*0.2f), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
            }
            "key" -> {
                // Key lock in bright amber
                drawCircle(color = M4OrangeEnd, radius = w * 0.22f, center = Offset(w * 0.38f, h * 0.42f), style = Stroke(width = 3.5.dp.toPx()))
                drawLine(color = M4OrangeEnd, start = Offset(w*0.54f, h*0.52f), end = Offset(w*0.82f, h*0.75f), strokeWidth = 3.5.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = M4OrangeEnd, start = Offset(w*0.72f, h*0.67f), end = Offset(w*0.66f, h*0.74f), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
            }
            else -> {
                // Tow truck in neon blue
                drawRoundRect(
                    color = M4ElectricBlue,
                    topLeft = Offset(w * 0.15f, h * 0.45f),
                    size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.35f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawLine(color = Color.White, start = Offset(w*0.3f, h*0.3f), end = Offset(w*0.6f, h*0.45f), strokeWidth = 2.dp.toPx())
                drawCircle(color = Color.White, radius = w*0.08f, center = Offset(w*0.32f, h*0.82f))
                drawCircle(color = Color.White, radius = w*0.08f, center = Offset(w*0.70f, h*0.82f))
            }
        }
    }
}

/**
 * 2. All Services Screen (Image 1, Screen 2)
 */
@Composable
fun AllServicesScreen(
    onBack: () -> Unit,
    onSelectService: (serviceName: String, price: Int) -> Unit,
    onTabSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val services = listOf(
        Triple("Puncture Repair", "On-spot tyre repair at your location", 149),
        Triple("Battery Jump Start", "Quick start for dead or drained battery", 299),
        Triple("Engine Problem", "Diagnosis & repair by expert mechanics", 399),
        Triple("Out of Fuel", "Fuel delivery to get you back on track", 299),
        Triple("Key Lockout", "Locked out of your vehicle? We'll help", 249),
        Triple("Towing Service", "Safe towing for breakdowns & more", 799),
        Triple("Brake Repair", "Brake inspection & quick repair", 349)
    )

    Scaffold(
        bottomBar = {
            CustomerBottomNavBar(selectedTab = "home", onTabSelected = onTabSelected)
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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "All services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(M4InputBg)
                    .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Search, contentDescription = "Search", tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search a service",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Vehicle Category Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill("All", isSelected = selectedFilter == "All") { selectedFilter = "All" }
                FilterPill("Bike", icon = Icons.Filled.TwoWheeler, isSelected = selectedFilter == "Bike") { selectedFilter = "Bike" }
                FilterPill("Car", icon = Icons.Filled.DirectionsCar, isSelected = selectedFilter == "Car") { selectedFilter = "Car" }
                FilterPill("Heavy", icon = Icons.Filled.LocalShipping, isSelected = selectedFilter == "Heavy") { selectedFilter = "Heavy" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Service items list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                services.forEach { (name, desc, price) ->
                    ServiceRowItem(
                        name = name,
                        desc = desc,
                        price = price,
                        onClick = { onSelectService(name, price) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    icon: ImageVector? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) M4ElectricBlue else M4InputBg)
            .border(1.dp, if (isSelected) M4ElectricBlue else M4InputBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun ServiceRowItem(
    name: String,
    desc: String,
    price: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(M4CardBg)
            .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            val type = when {
                name.contains("Puncture", ignoreCase = true) -> "puncture"
                name.contains("Battery", ignoreCase = true) -> "battery"
                name.contains("Engine", ignoreCase = true) -> "engine"
                name.contains("Fuel", ignoreCase = true) -> "fuel"
                name.contains("Key", ignoreCase = true) -> "key"
                name.contains("Towing", ignoreCase = true) -> "tow"
                else -> "puncture"
            }
            ServiceCanvasIcon(iconType = type, modifier = Modifier.size(38.dp))

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 2,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "From",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "₹$price",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
