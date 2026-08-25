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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlinx.coroutines.delay

/**
 * 5. Mechanic Details Screen (Image 2, Screen 1)
 */
@Composable
fun MechanicDetailsScreen(
    mechanicName: String = "Rakesh Kumar",
    rating: Double = 4.8,
    reviewsCount: Int = 124,
    experienceYears: Int = 6,
    jobsCompleted: Int = 1248,
    onBack: () -> Unit,
    onBookMechanic: () -> Unit
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
                        .clickable { onBookMechanic() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Book mechanic",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                    text = "Mechanic details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(54.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E3A5F)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(M4ElectricBlue)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = mechanicName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.Verified,
                                    contentDescription = "Verified",
                                    tint = M4NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(M4SuccessGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Online • Near Purnia Junction",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row: Rating, Experience, Jobs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(M4InputBg)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        DetailStatItem(value = "$rating ★", label = "$reviewsCount reviews")
                        DetailStatItem(value = "$experienceYears+ Years", label = "Experience")
                        DetailStatItem(value = "$jobsCompleted", label = "Jobs done")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Services Offered Section
            Text(
                text = "Services offered",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))

            val mechanicServices = listOf(
                Pair("Puncture Repair", 149),
                Pair("Battery Jump Start", 299),
                Pair("Engine Problem", 399),
                Pair("Out of Fuel", 299),
                Pair("Towing Service", 799)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                mechanicServices.forEach { (name, price) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = name, fontSize = 14.sp, color = Color.White)
                        Text(text = "₹$price", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Supported Vehicles
            Text(
                text = "Supported vehicles",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SupportedVehicleChip(icon = Icons.Filled.TwoWheeler, label = "Bike")
                SupportedVehicleChip(icon = Icons.Filled.DirectionsCar, label = "Car")
                SupportedVehicleChip(icon = Icons.Filled.LocalShipping, label = "Heavy")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
    }
}

@Composable
private fun SupportedVehicleChip(icon: ImageVector, label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(M4InputBg)
            .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = M4ElectricBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}

/**
 * 6. Service Details Screen (Image 2, Screen 2)
 */
@Composable
fun ServiceBookingDetailsScreen(
    initialService: String = "Puncture Repair",
    initialPrice: Int = 149,
    onBack: () -> Unit,
    onContinue: (serviceName: String, vehicleType: String, vehicleNumber: String, issueDesc: String, price: Int) -> Unit
) {
    var selectedService by remember { mutableStateOf(initialService) }
    var selectedVehicle by remember { mutableStateOf("Car") }
    var vehicleNumber by remember { mutableStateOf("BR 11 AB 1234") }
    var issueDescription by remember { mutableStateOf("") }
    var price by remember { mutableStateOf(initialPrice) }

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Estimated total", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹$price", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(M4ElectricBlue)
                            .clickable {
                                onContinue(selectedService, selectedVehicle, vehicleNumber, issueDescription, price)
                            }
                            .padding(horizontal = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Continue", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Service details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Service Dropdown Card
            Text(text = "Selected service", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ServiceCanvasIcon(iconType = "puncture", modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = selectedService, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(text = "₹$price", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Select Vehicle Type
            Text(text = "Select vehicle", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VehicleRadioTab("Bike", icon = Icons.Filled.TwoWheeler, isSelected = selectedVehicle == "Bike", modifier = Modifier.weight(1f)) { selectedVehicle = "Bike" }
                VehicleRadioTab("Car", icon = Icons.Filled.DirectionsCar, isSelected = selectedVehicle == "Car", modifier = Modifier.weight(1f)) { selectedVehicle = "Car" }
                VehicleRadioTab("Heavy", icon = Icons.Filled.LocalShipping, isSelected = selectedVehicle == "Heavy", modifier = Modifier.weight(1f)) { selectedVehicle = "Heavy" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Plate Number
            Text(text = "Vehicle registration number", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = vehicleNumber,
                onValueChange = { vehicleNumber = it.uppercase() },
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

            // Issue Description
            Text(text = "Describe your issue (optional)", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = issueDescription,
                onValueChange = { issueDescription = it },
                placeholder = { Text("e.g. Front right tyre is punctured near bus stand...", color = Color(0xFF64748B), fontSize = 13.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Add Photos (Optional)
            Text(text = "Add photos (optional)", fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(M4InputBg)
                        .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AddAPhoto, contentDescription = "Add photo", tint = M4ElectricBlue, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Add", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun VehicleRadioTab(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) M4ElectricBlue else M4InputBg)
            .border(1.dp, if (isSelected) M4ElectricBlue else M4InputBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = if (isSelected) Color.White else Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) Color.White else Color(0xFF94A3B8))
        }
    }
}

/**
 * 7. Review Booking Screen (Image 2, Screen 3)
 */
@Composable
fun ReviewBookingScreen(
    mechanicName: String = "Rakesh Kumar",
    serviceName: String = "Puncture Repair",
    vehicleType: String = "Car (BR 11 AB 1234)",
    locationAddress: String = "Station Rd, Purnia Junction, Purnia",
    serviceCharge: Int = 149,
    visitCharge: Int = 50,
    onBack: () -> Unit,
    onConfirmAndRequest: () -> Unit
) {
    val totalAmount = serviceCharge + visitCharge

    Scaffold(
        bottomBar = {
            Surface(
                color = M4CardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, M4CardBorder)
                    .padding(16.dp)
            ) {
                // Vibrant Orange Confirm & Request button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.horizontalGradient(listOf(M4OrangeStart, M4OrangeEnd)))
                        .clickable { onConfirmAndRequest() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm & Request",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Review booking", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Details Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ReviewRowItem(label = "Mechanic", value = mechanicName, icon = Icons.Filled.Person)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ReviewRowItem(label = "Service", value = serviceName, icon = Icons.Filled.Build)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ReviewRowItem(label = "Vehicle", value = vehicleType, icon = Icons.Filled.DirectionsCar)
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    ReviewRowItem(label = "Location", value = locationAddress, icon = Icons.Filled.LocationOn)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Price Details Breakdown
            Text(text = "Price details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(M4CardBg)
                    .border(1.dp, M4CardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Service charge", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹$serviceCharge", fontSize = 13.sp, color = Color.White)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Visiting fee", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹$visitCharge", fontSize = 13.sp, color = Color.White)
                    }
                    HorizontalDivider(color = M4InputBorder, thickness = 0.8.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total amount", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "₹$totalAmount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = M4NeonCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Free cancellation info pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C1D33))
                    .border(1.dp, Color(0xFF1E3A5F), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Security, contentDescription = null, tint = M4NeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Free cancellation until mechanic departs to your location.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ReviewRowItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = M4ElectricBlue, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

/**
 * 8. Sending Request / Radar Screen (Image 2, Screen 4)
 */
@Composable
fun SendingRequestRadarScreen(
    mechanicName: String = "Rakesh Kumar",
    serviceName: String = "Puncture Repair",
    onAccepted: () -> Unit,
    onCancel: () -> Unit
) {
    var countdown by remember { mutableStateOf(45) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
            if (countdown == 41) { // Simulate acceptance in 4 seconds for interactive feel
                onAccepted()
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "radarRipple")
    val ripple1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "r1"
    )
    val ripple2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2400, delayMillis = 800, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "r2"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sending request...",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Connecting with $mechanicName for $serviceName",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }

        // Radar Ripple Animation
        Box(
            modifier = Modifier
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val maxRadius = size.width / 2f

                // Expanding Ripples
                drawCircle(
                    color = M4ElectricBlue.copy(alpha = (1f - ripple1) * 0.5f),
                    radius = maxRadius * ripple1,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = M4NeonCyan.copy(alpha = (1f - ripple2) * 0.5f),
                    radius = maxRadius * ripple2,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // Center Mechanic Avatar with pulse ring
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF132B4F))
                    .border(2.dp, M4NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(38.dp))
            }
        }

        // Circular Timer & Cancel
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "00:${if (countdown < 10) "0$countdown" else countdown}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = M4NeonCyan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estimated time to connect",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(M4InputBg)
                    .border(1.dp, M4InputBorder, RoundedCornerShape(12.dp))
                    .clickable { onCancel() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel request",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}
