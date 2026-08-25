package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.ui.components.ErrorBanner
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.components.M4TextField
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.OnlineGreenLight
import com.example.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    mechanicRepository: MechanicRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val profile by mechanicRepository.mechanicProfile.collectAsState()

    var fullName by remember { mutableStateOf(profile?.fullName ?: "") }
    var email by remember { mutableStateOf(profile?.email ?: "") }
    var vehicleModel by remember { mutableStateOf(profile?.vehicleModel ?: "Maruti Suzuki Eeco") }
    var vehicleColor by remember { mutableStateOf(profile?.vehicleColor ?: "White") }
    var vehiclePlate by remember { mutableStateOf(profile?.vehiclePlate ?: "BR11AB1234") }
    var workAddress by remember { mutableStateOf(profile?.workAddress ?: "Purnia, Bihar") }

    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                text = "Edit Profile & Vehicle",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (errorMessage != null) {
            ErrorBanner(
                message = errorMessage ?: "",
                onDismiss = { errorMessage = null },
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }

        if (successMessage != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = OnlineGreenLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
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

        Text(
            text = "Partner Information",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        M4TextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "Enter your full name",
            label = "Full Name",
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        M4TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter email address",
            label = "Email Address",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        M4TextField(
            value = workAddress,
            onValueChange = { workAddress = it },
            placeholder = "e.g., Purnia, Bihar",
            label = "Base Service Address",
            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null, tint = PrimaryBlue) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Vehicle Information",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        M4TextField(
            value = vehicleModel,
            onValueChange = { vehicleModel = it },
            placeholder = "e.g., Maruti Suzuki Eeco / Bajaj Pulsar",
            label = "Vehicle Model",
            leadingIcon = { Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                M4TextField(
                    value = vehicleColor,
                    onValueChange = { vehicleColor = it },
                    placeholder = "Color",
                    label = "Color",
                    leadingIcon = { Icon(Icons.Filled.FormatColorFill, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1.3f)) {
                M4TextField(
                    value = vehiclePlate,
                    onValueChange = { vehiclePlate = it.uppercase() },
                    placeholder = "e.g., BR11AB1234",
                    label = "Number Plate",
                    leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        M4Button(
            text = "Save Changes",
            onClick = {
                coroutineScope.launch {
                    isSaving = true
                    errorMessage = null
                    val currentServices = profile?.services ?: mechanicRepository.getDefaultServices()
                    val result = mechanicRepository.registerMechanic(
                        fullName = fullName,
                        email = email,
                        experience = profile?.experience ?: 3,
                        vehicleModel = vehicleModel,
                        vehicleColor = vehicleColor,
                        vehiclePlate = vehiclePlate,
                        address = workAddress,
                        lat = profile?.workLat ?: 25.7771,
                        lng = profile?.workLng ?: 87.4753,
                        services = currentServices
                    )
                    isSaving = false
                    if (result is NetworkResult.Success) {
                        successMessage = "Profile updated successfully!"
                    } else if (result is NetworkResult.Error) {
                        errorMessage = result.message
                    }
                }
            },
            isLoading = isSaving,
            enabled = !isSaving && fullName.isNotBlank() && vehicleModel.isNotBlank(),
            style = M4ButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
