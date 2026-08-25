package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RespondOrderRequest(
    @Json(name = "action") val action: String // "accept" or "reject"
)

@JsonClass(generateAdapter = true)
data class RespondOrderResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "order") val order: OrderDto? = null,
    @Json(name = "trackingStarted") val trackingStarted: Boolean? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class CompleteJobRequest(
    @Json(name = "notes") val notes: String? = null
)

@JsonClass(generateAdapter = true)
data class CompleteJobResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "order") val order: OrderDto? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class OrderStatusResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "order") val order: OrderDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class OrderDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "_id") val underscoreId: String? = null,
    @Json(name = "orderNumber") val orderNumber: String? = null,
    @Json(name = "customerId") val customerId: String? = null,
    @Json(name = "customerName") val customerName: String? = null,
    @Json(name = "customerPhone") val customerPhone: String? = null,
    @Json(name = "mechanicId") val mechanicId: String? = null,
    @Json(name = "mechanicUserId") val mechanicUserId: String? = null,
    @Json(name = "mechanicName") val mechanicName: String? = null,
    @Json(name = "serviceType") val serviceType: String? = null,
    @Json(name = "serviceName") val serviceName: String? = null,
    @Json(name = "customerLocation") val customerLocation: OrderLocationDto? = null,
    @Json(name = "location") val location: OrderLocationDto? = null,
    @Json(name = "vehicleDetails") val vehicleDetails: VehicleDetailsDto? = null,
    @Json(name = "notes") val notes: String? = null,
    @Json(name = "status") val status: String? = null, // pending, accepted, rejected, completed
    @Json(name = "distance") val distance: Double? = null,
    @Json(name = "eta") val eta: Int? = null,
    @Json(name = "price") val price: Double? = null,
    @Json(name = "totalAmount") val totalAmount: Double? = null,
    @Json(name = "rating") val rating: Double? = null,
    @Json(name = "review") val review: String? = null,
    @Json(name = "createdAt") val createdAt: Any? = null,
    @Json(name = "acceptedAt") val acceptedAt: Any? = null,
    @Json(name = "completedAt") val completedAt: Any? = null,
    @Json(name = "customer") val customer: OrderPersonDto? = null,
    @Json(name = "mechanic") val mechanic: OrderPersonDto? = null
) {
    fun getEffectiveId(): String = id ?: underscoreId ?: ""
    fun getDisplayOrderNumber(): String = orderNumber ?: "#MCN-${getEffectiveId().takeLast(6).uppercase()}"
    fun getResolvedCustomerName(): String = customerName ?: customer?.fullName ?: "Customer"
    fun getResolvedCustomerPhone(): String = customerPhone ?: customer?.phoneNumber ?: "+91 9876543210"
    fun getResolvedLocation(): OrderLocationDto = customerLocation ?: location ?: OrderLocationDto("Customer Location", 25.7800, 87.4700)
}

@JsonClass(generateAdapter = true)
data class OrderLocationDto(
    @Json(name = "address") val address: String = "Location",
    @Json(name = "lat") val lat: Double = 0.0,
    @Json(name = "lng") val lng: Double = 0.0
)

@JsonClass(generateAdapter = true)
data class VehicleDetailsDto(
    @Json(name = "type") val type: String? = "Bike",
    @Json(name = "brand") val brand: String? = "Honda",
    @Json(name = "model") val model: String? = "Activa",
    @Json(name = "registrationNumber") val registrationNumber: String? = "BR11AB1234"
)

@JsonClass(generateAdapter = true)
data class OrderPersonDto(
    @Json(name = "fullName") val fullName: String? = null,
    @Json(name = "phoneNumber") val phoneNumber: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateOrderRequest(
    @Json(name = "mechanicId") val mechanicId: String,
    @Json(name = "serviceType") val serviceType: String,
    @Json(name = "location") val location: OrderLocationDto,
    @Json(name = "vehicleDetails") val vehicleDetails: VehicleDetailsDto? = null,
    @Json(name = "notes") val notes: String? = ""
)

@JsonClass(generateAdapter = true)
data class CreateOrderResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "order") val order: OrderDto? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class OrderHistoryResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "orders") val orders: List<OrderDto>? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class CompleteOrderWithRatingRequest(
    @Json(name = "rating") val rating: Int? = null,
    @Json(name = "review") val review: String? = null
)
