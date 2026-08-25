package com.example.data.repository

import android.content.Context
import com.example.data.dto.CompleteJobRequest
import com.example.data.dto.OrderDto
import com.example.data.dto.RespondOrderRequest
import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import com.example.data.remote.ApiService
import com.example.data.remote.ErrorParser
import com.example.data.remote.NetworkResult
import com.example.data.session.SessionManager
import com.example.domain.model.IncomingOrderEvent
import com.example.domain.model.LocationPoint
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.domain.model.TimelineStep
import com.example.domain.model.VehicleInfo
import com.example.service.LocationTrackingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderRepository(
    private val context: Context,
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val orderDao: OrderDao
) {

    private val _currentActiveOrder = MutableStateFlow<Order?>(null)
    val currentActiveOrder: StateFlow<Order?> = _currentActiveOrder.asStateFlow()

    val cachedOrders: Flow<List<Order>> = orderDao.getAllOrders().map { list ->
        list.map { it.toDomain() }
    }

    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    suspend fun fetchOrderStatus(orderId: String): NetworkResult<Order> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getOrderStatus(orderId)
            if (response.isSuccessful && response.body()?.success == true) {
                val orderDto = response.body()?.order
                if (orderDto != null) {
                    val order = mapDtoToDomain(orderDto, fallbackId = orderId)
                    orderDao.insertOrder(OrderEntity.fromDomain(order))

                    if (order.status == OrderStatus.ACCEPTED ||
                        order.status == OrderStatus.ON_THE_WAY ||
                        order.status == OrderStatus.ARRIVED ||
                        order.status == OrderStatus.IN_PROGRESS
                    ) {
                        _currentActiveOrder.value = order
                        sessionManager.setActiveOrderId(order.id)
                        sessionManager.saveActiveCustomerUserId(order.customerId)
                    } else if (order.status == OrderStatus.COMPLETED || order.status == OrderStatus.REJECTED) {
                        if (sessionManager.getActiveOrderId() == orderId) {
                            sessionManager.setActiveOrderId(null)
                            sessionManager.saveActiveCustomerUserId(null)
                            _currentActiveOrder.value = null
                            LocationTrackingService.stop(context)
                        }
                    }

                    NetworkResult.Success(order)
                } else {
                    NetworkResult.Error("Could not retrieve order details")
                }
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            val cached = orderDao.getOrderById(orderId)
            if (cached != null) {
                val domain = cached.toDomain()
                _currentActiveOrder.value = domain
                NetworkResult.Success(domain)
            } else {
                ErrorParser.parseError(e)
            }
        }
    }

    suspend fun respondToOrder(orderId: String, accept: Boolean): NetworkResult<Order> = withContext(Dispatchers.IO) {
        try {
            val action = if (accept) "accept" else "reject"
            val response = apiService.respondToOrder(
                orderId = orderId,
                request = RespondOrderRequest(action = action)
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val orderDto = body.order
                val order = mapDtoToDomain(orderDto, fallbackId = orderId)

                if (accept) {
                    val activeOrder = order.copy(
                        status = OrderStatus.ACCEPTED,
                        acceptedAtFormatted = timeFormat.format(Date())
                    )
                    _currentActiveOrder.value = activeOrder
                    sessionManager.setActiveOrderId(orderId)
                    sessionManager.saveActiveCustomerUserId(activeOrder.customerId)
                    orderDao.insertOrder(OrderEntity.fromDomain(activeOrder))

                    // Start foreground location service to share GPS
                    LocationTrackingService.start(context, orderId)

                    NetworkResult.Success(activeOrder)
                } else {
                    val rejectedOrder = order.copy(status = OrderStatus.REJECTED)
                    orderDao.insertOrder(OrderEntity.fromDomain(rejectedOrder))
                    if (sessionManager.getActiveOrderId() == orderId) {
                        sessionManager.setActiveOrderId(null)
                        _currentActiveOrder.value = null
                        LocationTrackingService.stop(context)
                    }
                    NetworkResult.Success(rejectedOrder)
                }
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun completeJob(orderId: String, notes: String = ""): NetworkResult<Order> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.completeJob(
                orderId = orderId,
                request = CompleteJobRequest(notes = notes.ifBlank { null })
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val orderDto = body.order
                val completedOrder = mapDtoToDomain(orderDto, fallbackId = orderId).copy(
                    status = OrderStatus.COMPLETED,
                    completedAtFormatted = timeFormat.format(Date())
                )

                sessionManager.setActiveOrderId(null)
                sessionManager.saveActiveCustomerUserId(null)
                _currentActiveOrder.value = null
                LocationTrackingService.stop(context)

                orderDao.insertOrder(OrderEntity.fromDomain(completedOrder))

                NetworkResult.Success(completedOrder)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun createOrder(
        mechanicId: String,
        serviceType: String,
        address: String,
        lat: Double,
        lng: Double,
        vehicleModel: String = "Maruti Swift",
        vehiclePlate: String = "BR11AB1234",
        notes: String = ""
    ): NetworkResult<Order> = withContext(Dispatchers.IO) {
        try {
            val request = com.example.data.dto.CreateOrderRequest(
                mechanicId = mechanicId,
                serviceType = serviceType,
                location = com.example.data.dto.OrderLocationDto(
                    address = address,
                    lat = lat,
                    lng = lng
                ),
                vehicleDetails = com.example.data.dto.VehicleDetailsDto(
                    model = vehicleModel,
                    registrationNumber = vehiclePlate
                ),
                notes = notes
            )
            val response = apiService.createOrder(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val orderDto = response.body()?.order
                val order = mapDtoToDomain(orderDto, fallbackId = "order_${System.currentTimeMillis()}")
                _currentActiveOrder.value = order
                sessionManager.setActiveOrderId(order.id)
                orderDao.insertOrder(OrderEntity.fromDomain(order))
                NetworkResult.Success(order)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchOrderHistory(): NetworkResult<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getOrderHistory()
            if (response.isSuccessful && response.body()?.success == true) {
                val list = response.body()?.orders ?: emptyList()
                val domainList = list.map { mapDtoToDomain(it, fallbackId = it.getEffectiveId()) }
                domainList.forEach { orderDao.insertOrder(OrderEntity.fromDomain(it)) }
                NetworkResult.Success(domainList)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun completeOrderWithRating(
        orderId: String,
        rating: Int,
        review: String
    ): NetworkResult<Order> = withContext(Dispatchers.IO) {
        try {
            val request = com.example.data.dto.CompleteOrderWithRatingRequest(
                rating = rating,
                review = review.ifBlank { null }
            )
            val response = apiService.completeOrderWithRating(orderId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                val orderDto = response.body()?.order
                val order = mapDtoToDomain(orderDto, fallbackId = orderId).copy(
                    status = OrderStatus.COMPLETED,
                    ratingGiven = rating.toDouble(),
                    reviewGiven = review
                )
                orderDao.insertOrder(OrderEntity.fromDomain(order))
                _currentActiveOrder.value = null
                sessionManager.setActiveOrderId(null)
                NetworkResult.Success(order)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    fun cacheIncomingOrder(event: IncomingOrderEvent): Order {
        val user = sessionManager.currentUser.value
        val order = Order(
            id = event.orderId,
            orderNumber = "#MCN-${event.orderId.takeLast(6).uppercase()}",
            customerId = event.customerId,
            customerName = event.customerName,
            customerPhone = "+91 9876543210",
            mechanicId = sessionManager.getMechanicRecordId() ?: "",
            mechanicUserId = user?.id ?: "",
            mechanicName = user?.fullName ?: "Mechanic",
            serviceType = event.serviceType,
            servicePrice = 199.0,
            totalAmount = 199.0,
            distanceKm = event.distance,
            etaMinutes = event.eta,
            status = OrderStatus.PENDING,
            customerLocation = event.customerLocation,
            vehicle = VehicleInfo(),
            problemNotes = "",
            createdAtFormatted = timeFormat.format(Date(event.timestamp))
        )
        return order
    }

    private fun mapDtoToDomain(
        dto: OrderDto?,
        fallbackId: String
    ): Order {
        val id = dto?.getEffectiveId()?.ifBlank { fallbackId } ?: fallbackId
        val orderNum = dto?.getDisplayOrderNumber() ?: "#MCN-${id.takeLast(6).uppercase()}"
        val custLocDto = dto?.getResolvedLocation()
        val customerLoc = LocationPoint(
            lat = custLocDto?.lat ?: 25.7800,
            lng = custLocDto?.lng ?: 87.4700,
            address = custLocDto?.address ?: "Customer Location"
        )

        val status = OrderStatus.fromString(dto?.status)
        val vehicle = VehicleInfo(
            type = dto?.vehicleDetails?.type ?: "Car",
            brand = dto?.vehicleDetails?.brand ?: "Maruti",
            model = dto?.vehicleDetails?.model ?: "Swift Dzire",
            registrationNumber = dto?.vehicleDetails?.registrationNumber ?: "BR01AB1234"
        )

        val timeline = listOf(
            TimelineStep("Request Received", "10:15 AM", isCompleted = true, isCurrent = status == OrderStatus.PENDING),
            TimelineStep("Job Accepted", "10:18 AM", isCompleted = status != OrderStatus.PENDING && status != OrderStatus.REJECTED, isCurrent = status == OrderStatus.ACCEPTED),
            TimelineStep("On The Way", "10:20 AM", isCompleted = status == OrderStatus.ON_THE_WAY || status == OrderStatus.ARRIVED || status == OrderStatus.IN_PROGRESS || status == OrderStatus.COMPLETED, isCurrent = status == OrderStatus.ON_THE_WAY),
            TimelineStep("Arrived At Location", "10:30 AM", isCompleted = status == OrderStatus.ARRIVED || status == OrderStatus.IN_PROGRESS || status == OrderStatus.COMPLETED, isCurrent = status == OrderStatus.ARRIVED),
            TimelineStep("Job Completed", "10:45 AM", isCompleted = status == OrderStatus.COMPLETED, isCurrent = status == OrderStatus.COMPLETED)
        )

        return Order(
            id = id,
            orderNumber = orderNum,
            customerId = dto?.customerId ?: "",
            customerName = dto?.getResolvedCustomerName() ?: "Customer",
            customerPhone = dto?.getResolvedCustomerPhone() ?: "+91 9876543210",
            mechanicId = dto?.mechanicId ?: "",
            mechanicUserId = dto?.mechanicUserId ?: "",
            mechanicName = dto?.mechanicName ?: "Mechanic",
            serviceType = dto?.serviceName ?: dto?.serviceType ?: "Roadside Service",
            servicePrice = dto?.price ?: 199.0,
            totalAmount = dto?.totalAmount ?: dto?.price ?: 199.0,
            distanceKm = dto?.distance ?: 2.3,
            etaMinutes = dto?.eta ?: 8,
            status = status,
            customerLocation = customerLoc,
            vehicle = vehicle,
            problemNotes = dto?.notes ?: "",
            createdAtFormatted = "Today, 10:15 AM",
            acceptedAtFormatted = "10:18 AM",
            completedAtFormatted = if (status == OrderStatus.COMPLETED) "10:45 AM" else null,
            ratingGiven = dto?.rating,
            reviewGiven = dto?.review,
            timeline = timeline
        )
    }
}
