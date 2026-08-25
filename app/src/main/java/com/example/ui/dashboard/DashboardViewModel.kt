package com.example.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.data.repository.OrderRepository
import com.example.data.session.SessionManager
import com.example.data.socket.SocketManager
import com.example.domain.model.IncomingOrderEvent
import com.example.domain.model.MechanicProfile
import com.example.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    object Idle : DashboardUiState
    object Loading : DashboardUiState
    data class JobAccepted(val order: Order) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

class DashboardViewModel(
    private val mechanicRepository: MechanicRepository,
    private val orderRepository: OrderRepository,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    val mechanicProfile: StateFlow<MechanicProfile?> = mechanicRepository.mechanicProfile
    val isSocketConnected: StateFlow<Boolean> = socketManager.isConnected
    val activeOrder: StateFlow<Order?> = orderRepository.currentActiveOrder

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _incomingOrder = MutableStateFlow<IncomingOrderEvent?>(null)
    val incomingOrder: StateFlow<IncomingOrderEvent?> = _incomingOrder.asStateFlow()

    private val _isTogglingStatus = MutableStateFlow(false)
    val isTogglingStatus: StateFlow<Boolean> = _isTogglingStatus.asStateFlow()

    private val _isRespondingJob = MutableStateFlow(false)
    val isRespondingJob: StateFlow<Boolean> = _isRespondingJob.asStateFlow()

    init {
        loadProfile()
        listenToIncomingOrders()
        recoverActiveOrder()
    }

    fun loadProfile() {
        viewModelScope.launch {
            mechanicRepository.fetchMechanicProfile()
        }
    }

    private fun listenToIncomingOrders() {
        viewModelScope.launch {
            socketManager.incomingOrders.collect { event ->
                // Only show if online and not currently handling another active order
                if (sessionManager.getOnlineStatus() && activeOrder.value == null) {
                    _incomingOrder.value = event
                    orderRepository.cacheIncomingOrder(event)
                }
            }
        }
    }

    private fun recoverActiveOrder() {
        viewModelScope.launch {
            val activeId = sessionManager.getActiveOrderId()
            if (!activeId.isNullOrBlank()) {
                orderRepository.fetchOrderStatus(activeId)
            }
        }
    }

    fun toggleOnlineStatus(isOnline: Boolean) {
        viewModelScope.launch {
            _isTogglingStatus.value = true
            val result = mechanicRepository.updateStatus(isOnline)
            _isTogglingStatus.value = false
            if (result is NetworkResult.Error) {
                _uiState.value = DashboardUiState.Error(result.message)
            }
        }
    }

    fun acceptIncomingOrder(orderId: String) {
        viewModelScope.launch {
            _isRespondingJob.value = true
            val result = orderRepository.respondToOrder(orderId, accept = true)
            _isRespondingJob.value = false
            _incomingOrder.value = null

            when (result) {
                is NetworkResult.Success -> {
                    _uiState.value = DashboardUiState.JobAccepted(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = DashboardUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun rejectIncomingOrder(orderId: String) {
        viewModelScope.launch {
            _incomingOrder.value = null
            orderRepository.respondToOrder(orderId, accept = false)
        }
    }

    fun clearError() {
        _uiState.value = DashboardUiState.Idle
    }
}

class DashboardViewModelFactory(
    private val mechanicRepository: MechanicRepository,
    private val orderRepository: OrderRepository,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(mechanicRepository, orderRepository, socketManager, sessionManager) as T
    }
}
