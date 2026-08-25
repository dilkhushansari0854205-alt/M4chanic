package com.example.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.dto.AdminDashboardDataDto
import com.example.data.dto.AdminLiveTrackingDataDto
import com.example.data.dto.AdminMechanicDetailDto
import com.example.data.remote.NetworkResult
import com.example.data.repository.AdminRepository
import com.example.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    val dashboardData: StateFlow<AdminDashboardDataDto?> = adminRepository.dashboardData
    val pendingMechanics: StateFlow<List<AdminMechanicDetailDto>> = adminRepository.pendingMechanics
    val allMechanics: StateFlow<List<AdminMechanicDetailDto>> = adminRepository.allMechanics
    val liveTrackingData: StateFlow<AdminLiveTrackingDataDto?> = adminRepository.liveTrackingData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun sendAdminOtp(phoneNumber: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = adminRepository.adminLogin(phoneNumber)) {
                is NetworkResult.Success -> {
                    _isLoading.value = false
                    _successMessage.value = result.data
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.message
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun verifyAdminOtp(phoneNumber: String, otp: String, onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = adminRepository.adminVerify(phoneNumber, otp)) {
                is NetworkResult.Success -> {
                    _isLoading.value = false
                    onSuccess(result.data)
                }
                is NetworkResult.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.message
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            adminRepository.fetchDashboard()
            adminRepository.fetchPendingMechanics()
            _isLoading.value = false
        }
    }

    fun loadMechanics() {
        viewModelScope.launch {
            _isLoading.value = true
            adminRepository.fetchAllMechanics()
            adminRepository.fetchPendingMechanics()
            _isLoading.value = false
        }
    }

    fun approveMechanic(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = adminRepository.approveMechanic(userId)) {
                is NetworkResult.Success -> {
                    _successMessage.value = "Mechanic approved successfully"
                    adminRepository.fetchDashboard()
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.message
                }
                is NetworkResult.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun rejectMechanic(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = adminRepository.rejectMechanic(userId)) {
                is NetworkResult.Success -> {
                    _successMessage.value = "Mechanic registration rejected"
                    adminRepository.fetchDashboard()
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.message
                }
                is NetworkResult.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun loadLiveTracking() {
        viewModelScope.launch {
            _isLoading.value = true
            adminRepository.fetchLiveTracking()
            _isLoading.value = false
        }
    }

    class Factory(private val adminRepository: AdminRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminViewModel(adminRepository) as T
        }
    }
}
