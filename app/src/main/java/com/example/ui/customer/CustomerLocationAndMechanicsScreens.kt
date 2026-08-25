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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.components.M4SuccessGreen

/**
 * 3. Choose Location Screen (Image 1, Screen 3)
 */
@Composable
fun ChooseLocationScreen(
    currentAddress: String = "Station Rd, Purnia Junction, Purnia, Bihar 854301",
    onBack: () -> Unit,
    onConfirmLocation: (String) -> Unit
) {
    var selectedSaved by remember { mutableStateOf("Home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Choose location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(M4InputBg)
                .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Search address or place",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Saved Locations (Home, Work)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SavedLocationChip(
                icon = Icons.Filled.Home,
                title = "Home",
                subtitle = "Purnia, Bihar",
                isSelected = selectedSaved == "Home",
                onClick = { selectedSaved = "Home" },
                modifier = Modifier.weight(1f)
            )
            SavedLocationChip(
                icon = Icons.Filled.Business,
                title = "Work",
                subtitle = "Add address",
                isSelected = selectedSaved == "Work",
                onClick = { selectedSaved = "Work" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Vector Dark Map with Pulsing Marker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            DarkCityMapCanvas(
                modifier = Modifier.fillMaxSize(),
                pulseCenter = true,
                centerLabel = "Gulab Bagh"
            )

            // Recenter Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "My Location",
                    tint = M4NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Bottom Sheet: Current location & Confirm
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
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF003880)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = M4NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Current location",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentAddress,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            maxLines = 2,
                            lineHeight = 16.sp
                        )
                    }

                    Text(
                        text = "Change",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Confirm Location Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(M4ElectricBlue)
                        .clickable { onConfirmLocation(currentAddress) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm location",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedLocationChip(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(M4InputBg)
            .border(1.dp, if (isSelected) M4ElectricBlue else M4InputBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) M4ElectricBlue else Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

/**
 * 4. Mechanics Near You Screen (Image 1, Screen 4)
 */
@Composable
fun MechanicsNearYouScreen(
    onBack: () -> Unit,
    onSelectMechanic: (mechanicId: String, name: String) -> Unit,
    onTabSelected: (String) -> Unit
) {
    var viewMode by remember { mutableStateOf("Map") } // "Map" or "List"
    var selectedVehicleFilter by remember { mutableStateOf("All") }

    val mechanics = listOf(
        MechanicCardData(
            id = "m1",
            name = "Rakesh Kumar",
            rating = 4.8,
            distance = "1.2 km",
            eta = "10 min",
            services = listOf("Puncture", "Jump Start", "Towing"),
            price = 149,
            isOnline = true
        ),
        MechanicCardData(
            id = "m2",
            name = "Sanjay Verma",
            rating = 4.8,
            distance = "2.1 km",
            eta = "14 min",
            services = listOf("Puncture", "Engine", "Towing"),
            price = 199,
            isOnline = true
        )
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
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mechanics near you",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                // Map / List Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(M4InputBg)
                        .border(1.dp, M4InputBorder, RoundedCornerShape(20.dp))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (viewMode == "Map") M4ElectricBlue else Color.Transparent)
                            .clickable { viewMode = "Map" }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Map",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (viewMode == "List") M4ElectricBlue else Color.Transparent)
                            .clickable { viewMode = "List" }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "List",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (viewMode == "List") Color.White else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // Filters row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterPillSmall("All", isSelected = selectedVehicleFilter == "All") { selectedVehicleFilter = "All" }
                FilterPillSmall("Bike", icon = Icons.Filled.TwoWheeler, isSelected = selectedVehicleFilter == "Bike") { selectedVehicleFilter = "Bike" }
                FilterPillSmall("Car", icon = Icons.Filled.DirectionsCar, isSelected = selectedVehicleFilter == "Car") { selectedVehicleFilter = "Car" }
                FilterPillSmall("Heavy", icon = Icons.Filled.LocalShipping, isSelected = selectedVehicleFilter == "Heavy") { selectedVehicleFilter = "Heavy" }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "Filter",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Upper Map with Green Mechanic Markers
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                DarkMechanicsRadarMapCanvas(modifier = Modifier.fillMaxSize())
            }

            // Bottom Mechanics Card List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                mechanics.forEach { mechanic ->
                    MechanicNearItemCard(
                        data = mechanic,
                        onView = { onSelectMechanic(mechanic.id, mechanic.name) }
                    )
                }
            }
        }
    }
}

data class MechanicCardData(
    val id: String,
    val name: String,
    val rating: Double,
    val distance: String,
    val eta: String,
    val services: List<String>,
    val price: Int,
    val isOnline: Boolean
)

@Composable
fun MechanicNearItemCard(
    data: MechanicCardData,
    onView: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(M4InputBg)
            .border(1.dp, M4InputBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar with online green badge
                Box(modifier = Modifier.size(44.dp)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A5F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = data.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(M4ElectricBlue)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = data.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(M4SuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Online",
                            fontSize = 11.sp,
                            color = M4SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${data.rating}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = " • ${data.distance} • ETA ${data.eta}",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                data.services.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF14243B))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF14243B))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+2",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "From ",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "₹${data.price}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(M4ElectricBlue)
                        .clickable { onView() }
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterPillSmall(
    label: String,
    icon: ImageVector? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) M4ElectricBlue else M4InputBg)
            .border(1.dp, if (isSelected) M4ElectricBlue else M4InputBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color(0xFF94A3B8)
            )
        }
    }
}

/**
 * Dark Map Vector Canvas with street grid lines & pulse center
 */
@Composable
fun DarkCityMapCanvas(
    modifier: Modifier = Modifier,
    pulseCenter: Boolean = true,
    centerLabel: String = ""
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseR"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseA"
    )

    Canvas(modifier = modifier.background(Color(0xFF070F1B))) {
        val w = size.width
        val h = size.height
        val roadColor = Color(0xFF132238)
        val majorRoadColor = Color(0xFF1E3554)

        // Draw Map Grid Roads
        drawLine(color = majorRoadColor, start = Offset(w * 0.1f, 0f), end = Offset(w * 0.35f, h), strokeWidth = 5.dp.toPx())
        drawLine(color = majorRoadColor, start = Offset(0f, h * 0.4f), end = Offset(w, h * 0.35f), strokeWidth = 4.dp.toPx())
        drawLine(color = majorRoadColor, start = Offset(w * 0.8f, 0f), end = Offset(w * 0.6f, h), strokeWidth = 4.dp.toPx())
        drawLine(color = roadColor, start = Offset(0f, h * 0.7f), end = Offset(w, h * 0.75f), strokeWidth = 3.dp.toPx())
        drawLine(color = roadColor, start = Offset(w * 0.4f, 0f), end = Offset(w * 0.4f, h), strokeWidth = 2.5.dp.toPx())
        drawLine(color = roadColor, start = Offset(0f, h * 0.15f), end = Offset(w, h * 0.2f), strokeWidth = 2.5.dp.toPx())

        // Center Pulsing Circle (Customer Location)
        val cx = w * 0.5f
        val cy = h * 0.45f

        if (pulseCenter) {
            drawCircle(
                color = M4ElectricBlue.copy(alpha = pulseAlpha),
                radius = pulseRadius.dp.toPx(),
                center = Offset(cx, cy)
            )
        }

        // Inner glowing blue ring & solid center
        drawCircle(
            color = M4ElectricBlue,
            radius = 12.dp.toPx(),
            center = Offset(cx, cy),
            style = Stroke(width = 3.dp.toPx())
        )
        drawCircle(
            color = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(cx, cy)
        )
    }
}

/**
 * Mechanics radar map showing multiple green mechanic markers scattered around user
 */
@Composable
fun DarkMechanicsRadarMapCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.background(Color(0xFF070F1B))) {
        val w = size.width
        val h = size.height
        val majorRoad = Color(0xFF162942)
        val minorRoad = Color(0xFF0F1C2E)

        // Road network
        drawLine(color = majorRoad, start = Offset(w * 0.2f, 0f), end = Offset(w * 0.45f, h), strokeWidth = 4.dp.toPx())
        drawLine(color = majorRoad, start = Offset(0f, h * 0.5f), end = Offset(w, h * 0.4f), strokeWidth = 4.dp.toPx())
        drawLine(color = majorRoad, start = Offset(w * 0.85f, 0f), end = Offset(w * 0.65f, h), strokeWidth = 3.dp.toPx())
        drawLine(color = minorRoad, start = Offset(0f, h * 0.25f), end = Offset(w, h * 0.3f), strokeWidth = 2.dp.toPx())
        drawLine(color = minorRoad, start = Offset(0f, h * 0.75f), end = Offset(w, h * 0.7f), strokeWidth = 2.dp.toPx())

        // Center User Blue Pin
        val cx = w * 0.52f
        val cy = h * 0.42f
        drawCircle(color = M4ElectricBlue, radius = 10.dp.toPx(), center = Offset(cx, cy), style = Stroke(width = 3.dp.toPx()))
        drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(cx, cy))

        // Green Mechanic Pins at various locations
        val pins = listOf(
            Offset(w * 0.28f, h * 0.22f),
            Offset(w * 0.50f, h * 0.15f),
            Offset(w * 0.80f, h * 0.28f),
            Offset(w * 0.22f, h * 0.42f),
            Offset(w * 0.48f, h * 0.58f),
            Offset(w * 0.80f, h * 0.58f)
        )

        pins.forEach { pos ->
            // Outer green circle with white person icon simulation
            drawCircle(color = M4SuccessGreen, radius = 13.dp.toPx(), center = pos)
            drawCircle(color = Color.White, radius = 13.dp.toPx(), center = pos, style = Stroke(width = 1.5.dp.toPx()))
            // Inner mini head & shoulders
            drawCircle(color = Color.White, radius = 3.5.dp.toPx(), center = Offset(pos.x, pos.y - 3.dp.toPx()))
            drawArc(
                color = Color.White,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(pos.x - 6.dp.toPx(), pos.y),
                size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 8.dp.toPx())
            )
        }
    }
}
