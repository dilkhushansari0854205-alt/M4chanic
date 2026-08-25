package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// ============================================
// ADMIN DTOs (Matching Server Code)
// ============================================

@JsonClass(generateAdapter = true)
data class AdminLoginRequest(
    @Json(name = "phoneNumber") val phoneNumber: String
)

@JsonClass(generateAdapter = true)
data class AdminLoginResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "phoneNumber") val phoneNumber: String? = null,
    @Json(name = "expiresIn") val expiresIn: Int? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminVerifyRequest(
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "otp") val otp: String
)

@JsonClass(generateAdapter = true)
data class AdminVerifyResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "token") val token: String? = null,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminDashboardResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "data") val data: AdminDashboardDataDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminDashboardDataDto(
    @Json(name = "stats") val stats: AdminStatsDto? = null,
    @Json(name = "recentOrders") val recentOrders: List<OrderDto>? = null
)

@JsonClass(generateAdapter = true)
data class AdminStatsDto(
    @Json(name = "totalUsers") val totalUsers: Int = 0,
    @Json(name = "totalCustomers") val totalCustomers: Int = 0,
    @Json(name = "totalMechanics") val totalMechanics: Int = 0,
    @Json(name = "pendingMechanics") val pendingMechanics: Int = 0,
    @Json(name = "approvedMechanics") val approvedMechanics: Int = 0,
    @Json(name = "activeOrders") val activeOrders: Int = 0,
    @Json(name = "completedOrders") val completedOrders: Int = 0,
    @Json(name = "totalOrders") val totalOrders: Int = 0
)

@JsonClass(generateAdapter = true)
data class AdminMechanicsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "mechanics") val mechanics: List<AdminMechanicDetailDto>? = null,
    @Json(name = "total") val total: Int? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminMechanicDetailDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "userId") val userId: String? = null,
    @Json(name = "vehicleDetails") val vehicleDetails: MechanicVehicleDetailsDto? = null,
    @Json(name = "location") val location: MechanicRegisterLocationDto? = null,
    @Json(name = "services") val services: List<MechanicServiceDto>? = null,
    @Json(name = "experience") val experience: Int? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "completedJobs") val completedJobs: Int? = null,
    @Json(name = "rating") val rating: Double? = null,
    @Json(name = "totalReviews") val totalReviews: Int? = null,
    @Json(name = "userDetails") val userDetails: UserDto? = null
)

@JsonClass(generateAdapter = true)
data class AdminLiveTrackingResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "data") val data: AdminLiveTrackingDataDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminLiveTrackingDataDto(
    @Json(name = "mechanics") val mechanics: List<AdminTrackingMechanicDto>? = null,
    @Json(name = "orders") val orders: List<AdminTrackingOrderDto>? = null
)

@JsonClass(generateAdapter = true)
data class AdminTrackingMechanicDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "userId") val userId: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "location") val location: MechanicRegisterLocationDto? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "status") val status: String? = null
)

@JsonClass(generateAdapter = true)
data class AdminTrackingOrderDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "customerName") val customerName: String? = null,
    @Json(name = "mechanicName") val mechanicName: String? = null,
    @Json(name = "serviceType") val serviceType: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "customerLocation") val customerLocation: OrderLocationDto? = null,
    @Json(name = "distance") val distance: Double? = null,
    @Json(name = "eta") val eta: Int? = null
)
