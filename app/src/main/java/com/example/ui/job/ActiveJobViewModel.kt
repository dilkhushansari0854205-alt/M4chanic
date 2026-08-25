package com.example.ui.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.location.LocationManager
import com.example.data.remote.NetworkResult
import com.example.data.repository.OrderRepository
import com.example.data.socket.SocketManager
import com.example.domain.model.LocationPoint
import com.example.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ActiveJobUiState {
    object Idle : ActiveJobUiState
    object Loading : ActiveJobUiState
    data class JobCompleted(val order: Order) : ActiveJobUiState
    data class Error(val message: String) : ActiveJobUiState
}

class ActiveJobViewModel(
    private val orderRepository: OrderRepository,
    private val locationManager: LocationManager,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _uiState = MutableStateFlow<ActiveJobUiState>(ActiveJobUiState.Idle)
    val uiState: StateFlow<ActiveJobUiState> = _uiState.asStateFlow()

    val currentLocation: StateFlow<LocationPoint?> = locationManager.currentLocation

    private val _isCompleting = MutableStateFlow(false)
    val isCompleting: StateFlow<Boolean> = _isCompleting.asStateFlow()

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = ActiveJobUiState.Loading
            val result = orderRepository.fetchOrderStatus(orderId)
            when (result) {
                is NetworkResult.Success -> {
                    _order.value = result.data
                    _uiState.value = ActiveJobUiState.Idle
                }
                is NetworkResult.Error -> {
                    _uiState.value = ActiveJobUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun completeJob(orderId: String, notes: String = "") {
        viewModelScope.launch {
            _isCompleting.value = true
            val result = orderRepository.completeJob(orderId, notes)
            _isCompleting.value = false

            when (result) {
                is NetworkResult.Success -> {
                    _order.value = result.data
                    _uiState.value = ActiveJobUiState.JobCompleted(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = ActiveJobUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun clearState() {
        _uiState.value = ActiveJobUiState.Idle
    }
}

class ActiveJobViewModelFactory(
    private val orderRepository: OrderRepository,
    private val locationManager: LocationManager,
    private val socketManager: SocketManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActiveJobViewModel(orderRepository, locationManager, socketManager) as T
    }
}
