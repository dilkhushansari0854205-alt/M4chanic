package com.example.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.dto.NearbyMechanicDto
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.data.repository.OrderRepository
import com.example.data.session.SessionManager
import com.example.data.socket.SocketManager
import com.example.domain.model.LocationPoint
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val mechanicRepository: MechanicRepository,
    private val orderRepository: OrderRepository,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _nearbyMechanics = MutableStateFlow<List<NearbyMechanicDto>>(emptyList())
    val nearbyMechanics: StateFlow<List<NearbyMechanicDto>> = _nearbyMechanics.asStateFlow()

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    val currentActiveOrder: StateFlow<Order?> = orderRepository.currentActiveOrder

    private val _mechanicLiveLocation = MutableStateFlow<LocationPoint?>(null)
    val mechanicLiveLocation: StateFlow<LocationPoint?> = _mechanicLiveLocation.asStateFlow()

    private val _customerAddress = MutableStateFlow("Station Rd, Purnia Junction, Purnia, Bihar 854301")
    val customerAddress: StateFlow<String> = _customerAddress.asStateFlow()

    private val _customerLat = MutableStateFlow(25.7771)
    val customerLat: StateFlow<Double> = _customerLat.asStateFlow()

    private val _customerLng = MutableStateFlow(87.4753)
    val customerLng: StateFlow<Double> = _customerLng.asStateFlow()

    private val _selectedVehicleType = MutableStateFlow("Bike")
    val selectedVehicleType: StateFlow<String> = _selectedVehicleType.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        socketManager.connect()
        observeSocketEvents()
        loadNearbyMechanics()
        loadOrderHistory()
    }

    fun setLocation(address: String, lat: Double = 25.7771, lng: Double = 87.4753) {
        _customerAddress.value = address
        _customerLat.value = lat
        _customerLng.value = lng
        loadNearbyMechanics()
    }

    fun setVehicleType(type: String) {
        _selectedVehicleType.value = type
    }

    fun loadNearbyMechanics() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = mechanicRepository.fetchNearbyMechanics(_customerLat.value, _customerLng.value)) {
                is NetworkResult.Success -> {
                    _nearbyMechanics.value = result.data
                }
                is NetworkResult.Error -> {
                    // Keep existing list or show error
                }
                is NetworkResult.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun createBooking(
        mechanicId: String,
        serviceType: String,
        notes: String = "",
        vehicleModel: String = "Honda Activa",
        vehiclePlate: String = "BR11AB1234",
        onOrderCreated: (Order) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = orderRepository.createOrder(
                mechanicId = mechanicId,
                serviceType = serviceType,
                address = _customerAddress.value,
                lat = _customerLat.value,
                lng = _customerLng.value,
                vehicleModel = vehicleModel,
                vehiclePlate = vehiclePlate,
                notes = notes
            )
            when (result) {
                is NetworkResult.Success -> {
                    _isLoading.value = false
                    socketManager.joinOrder(result.data.id)
                    onOrderCreated(result.data)
                }
                is NetworkResult.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.message
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun loadOrderHistory() {
        viewModelScope.launch {
            when (val result = orderRepository.fetchOrderHistory()) {
                is NetworkResult.Success -> {
                    _orderHistory.value = result.data
                }
                is NetworkResult.Error -> {
                    // Fallback to cached
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun submitRating(orderId: String, rating: Int, review: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = orderRepository.completeOrderWithRating(orderId, rating, review)) {
                is NetworkResult.Success -> {
                    _isLoading.value = false
                    _successMessage.value = "Thank you for your rating!"
                    loadOrderHistory()
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

    private fun observeSocketEvents() {
        viewModelScope.launch {
            orderRepository.currentActiveOrder.collect { order ->
                if (order != null) {
                    socketManager.joinOrder(order.id)
                }
            }
        }
    }

    class Factory(
        private val mechanicRepository: MechanicRepository,
        private val orderRepository: OrderRepository,
        private val socketManager: SocketManager,
        private val sessionManager: SessionManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CustomerViewModel(
                mechanicRepository,
                orderRepository,
                socketManager,
                sessionManager
            ) as T
        }
    }
}
