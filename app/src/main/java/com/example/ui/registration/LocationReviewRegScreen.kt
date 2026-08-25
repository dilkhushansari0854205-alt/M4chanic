package com.example.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.location.LocationManager
import com.example.ui.components.ErrorBanner
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.components.M4TextField
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun LocationReviewRegScreen(
    viewModel: RegistrationViewModel,
    locationManager: LocationManager,
    onBack: () -> Unit,
    onNavigatePending: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.draft.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val currentLocation by locationManager.currentLocation.collectAsState()

    var address by remember { mutableStateOf(draft.address) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { loc ->
            viewModel.updateLocation(
                address = loc.address.ifBlank { address },
                lat = loc.lat,
                lng = loc.lng
            )
            if (address.isBlank() || address == "Purnia, Bihar") {
                address = loc.address
            }
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegistrationUiState.Success -> {
                onNavigatePending()
                viewModel.resetState()
            }
            is RegistrationUiState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
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
            Column {
                Text(
                    text = "Step 3 of 3",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                Text(
                    text = "Work Location & Review",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (errorMessage != null) {
            ErrorBanner(
                message = errorMessage ?: "",
                onDismiss = { errorMessage = null },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Base Work Location Input
        Text(
            text = "Primary Service Area / Garage Base",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "You will receive nearby breakdown calls within 15 km of this base.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        M4TextField(
            value = address,
            onValueChange = {
                address = it
                viewModel.updateLocation(it, draft.lat, draft.lng)
            },
            label = "Garage / Service Base Address",
            placeholder = "e.g. Near Bus Stand, Purnia, Bihar",
            leadingIcon = {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = PrimaryBlue)
            },
            trailingIcon = {
                IconButton(onClick = {
                    currentLocation?.let { loc ->
                        address = loc.address
                        viewModel.updateLocation(loc.address, loc.lat, loc.lng)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.MyLocation,
                        contentDescription = "Use Current GPS",
                        tint = PrimaryBlue
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Application Summary Card
        Text(
            text = "Application Review",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Partner & Vehicle Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = draft.fullName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${draft.experience} Years Experience • ${draft.email.ifBlank { "No email" }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Vehicle Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DirectionsCar,
                        contentDescription = null,
                        tint = ActionOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${draft.vehicleModel} (${draft.vehicleColor})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Plate: ${draft.vehiclePlate}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Services Row
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Filled.Handyman,
                        contentDescription = null,
                        tint = OnlineGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${draft.services.size} Services Configured",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        draft.services.forEach { service ->
                            Text(
                                text = "• ${service.type} - ₹${service.price.toInt()}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        M4Button(
            text = "Submit Application",
            onClick = {
                errorMessage = null
                viewModel.submitRegistration()
            },
            isLoading = uiState is RegistrationUiState.Submitting,
            enabled = uiState !is RegistrationUiState.Submitting && address.isNotBlank(),
            style = M4ButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
