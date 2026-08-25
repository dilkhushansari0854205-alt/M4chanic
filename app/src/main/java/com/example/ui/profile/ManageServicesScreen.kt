package com.example.ui.profile

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.domain.model.MechanicServiceItem
import com.example.ui.components.ErrorBanner
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.OnlineGreenLight
import com.example.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

@Composable
fun ManageServicesScreen(
    mechanicRepository: MechanicRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val profile by mechanicRepository.mechanicProfile.collectAsState()

    val allServiceTypes = listOf(
        "Puncture" to "Puncture Repair (Two Wheeler & Car)",
        "Battery" to "Battery Jumpstart & Health Check",
        "Engine" to "Engine Breakdown & Minor Repairs",
        "Towing" to "Towing & Flatbed Assistance",
        "Fuel Delivery" to "Emergency Fuel Delivery",
        "Key Lockout" to "Key Lockout / Vehicle Unlock"
    )

    var currentServices by remember {
        mutableStateOf(profile?.services ?: mechanicRepository.getDefaultServices())
    }

    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Manage Services & Pricing",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (errorMessage != null) {
            ErrorBanner(
                message = errorMessage ?: "",
                onDismiss = { errorMessage = null },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (successMessage != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = OnlineGreenLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = successMessage ?: "",
                    color = OnlineGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allServiceTypes) { (type, description) ->
                val activeItem = currentServices.find { it.type.equals(type, ignoreCase = true) }
                val isSelected = activeItem != null
                val price = activeItem?.price ?: 199.0

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    if (!checked) {
                                        currentServices = currentServices.filter { !it.type.equals(type, ignoreCase = true) }
                                    } else {
                                        val defaultItem = MechanicServiceItem(type, listOf("Car", "Two Wheeler"), 199.0)
                                        currentServices = currentServices + defaultItem
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = type,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 44.dp)
                            ) {
                                Text(
                                    text = "Base Rate:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedTextField(
                                    value = price.toInt().toString(),
                                    onValueChange = { input ->
                                        val digits = input.filter { it.isDigit() }
                                        val num = digits.toDoubleOrNull() ?: 0.0
                                        currentServices = currentServices.map {
                                            if (it.type.equals(type, ignoreCase = true)) it.copy(price = num) else it
                                        }
                                    },
                                    prefix = {
                                        Text("₹", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryBlue
                                    ),
                                    modifier = Modifier.width(110.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        M4Button(
            text = "Save Service Rates",
            onClick = {
                coroutineScope.launch {
                    isSaving = true
                    errorMessage = null
                    val currentProf = profile
                    val result = mechanicRepository.registerMechanic(
                        fullName = currentProf?.fullName ?: "Mechanic Partner",
                        email = currentProf?.email ?: "",
                        experience = currentProf?.experience ?: 3,
                        vehicleModel = currentProf?.vehicleModel ?: "Maruti Suzuki Eeco",
                        vehicleColor = currentProf?.vehicleColor ?: "White",
                        vehiclePlate = currentProf?.vehiclePlate ?: "BR11AB1234",
                        address = currentProf?.workAddress ?: "Purnia, Bihar",
                        lat = currentProf?.workLat ?: 25.7771,
                        lng = currentProf?.workLng ?: 87.4753,
                        services = currentServices
                    )
                    isSaving = false
                    if (result is NetworkResult.Success) {
                        successMessage = "Services & rates updated successfully!"
                    } else if (result is NetworkResult.Error) {
                        errorMessage = result.message
                    }
                }
            },
            isLoading = isSaving,
            enabled = !isSaving && currentServices.isNotEmpty(),
            style = M4ButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}
