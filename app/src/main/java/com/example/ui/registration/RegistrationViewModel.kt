package com.example.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.data.session.SessionManager
import com.example.domain.model.MechanicServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegistrationDraft(
    // Step 1
    val fullName: String = "",
    val email: String = "",
    val experience: Int = 3,
    val vehicleModel: String = "Maruti Suzuki Eeco",
    val vehicleColor: String = "White",
    val vehiclePlate: String = "BR11AB1234",

    // Step 2
    val services: List<MechanicServiceItem> = emptyList(),

    // Step 3
    val address: String = "Purnia, Bihar",
    val lat: Double = 25.7771,
    val lng: Double = 87.4753
)

sealed interface RegistrationUiState {
    object Idle : RegistrationUiState
    object Submitting : RegistrationUiState
    data class Success(val status: String) : RegistrationUiState
    data class Error(val message: String) : RegistrationUiState
}

class RegistrationViewModel(
    private val mechanicRepository: MechanicRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _draft = MutableStateFlow(RegistrationDraft())
    val draft: StateFlow<RegistrationDraft> = _draft.asStateFlow()

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    init {
        val user = sessionManager.currentUser.value
        val defaultServices = mechanicRepository.getDefaultServices()
        _draft.value = _draft.value.copy(
            fullName = user?.fullName ?: "Mechanic Partner",
            email = user?.email ?: "",
            services = defaultServices
        )
    }

    fun updatePersonalAndVehicle(
        fullName: String,
        email: String,
        experience: Int,
        vehicleModel: String,
        vehicleColor: String,
        vehiclePlate: String
    ) {
        _draft.value = _draft.value.copy(
            fullName = fullName,
            email = email,
            experience = experience,
            vehicleModel = vehicleModel,
            vehicleColor = vehicleColor,
            vehiclePlate = vehiclePlate
        )
    }

    fun updateServicePrice(serviceType: String, newPrice: Double) {
        val updated = _draft.value.services.map {
            if (it.type == serviceType) it.copy(price = newPrice) else it
        }
        _draft.value = _draft.value.copy(services = updated)
    }

    fun toggleServiceSelection(serviceType: String, isSelected: Boolean) {
        if (!isSelected) {
            val updated = _draft.value.services.filter { it.type != serviceType }
            _draft.value = _draft.value.copy(services = updated)
        } else {
            val defaultItem = mechanicRepository.getDefaultServices().find { it.type == serviceType }
                ?: MechanicServiceItem(serviceType, listOf("Car", "Two Wheeler"), 199.0)
            if (_draft.value.services.none { it.type == serviceType }) {
                _draft.value = _draft.value.copy(services = _draft.value.services + defaultItem)
            }
        }
    }

    fun updateLocation(address: String, lat: Double, lng: Double) {
        _draft.value = _draft.value.copy(
            address = address,
            lat = lat,
            lng = lng
        )
    }

    fun submitRegistration() {
        val currentDraft = _draft.value
        if (currentDraft.fullName.isBlank()) {
            _uiState.value = RegistrationUiState.Error("Full Name cannot be empty")
            return
        }
        if (currentDraft.services.isEmpty()) {
            _uiState.value = RegistrationUiState.Error("Please select at least one repair service")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Submitting

            val result = mechanicRepository.registerMechanic(
                fullName = currentDraft.fullName,
                email = currentDraft.email,
                experience = currentDraft.experience,
                vehicleModel = currentDraft.vehicleModel,
                vehicleColor = currentDraft.vehicleColor,
                vehiclePlate = currentDraft.vehiclePlate,
                address = currentDraft.address,
                lat = currentDraft.lat,
                lng = currentDraft.lng,
                services = currentDraft.services
            )

            when (result) {
                is NetworkResult.Success -> {
                    val status = result.data.status ?: "pending"
                    _uiState.value = RegistrationUiState.Success(status)
                }
                is NetworkResult.Error -> {
                    _uiState.value = RegistrationUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}

class RegistrationViewModelFactory(
    private val mechanicRepository: MechanicRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(mechanicRepository, sessionManager) as T
    }
}
