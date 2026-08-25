package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MechanicRegisterRequest(
    @Json(name = "fullName") val fullName: String,
    @Json(name = "email") val email: String,
    @Json(name = "experience") val experience: Int,
    @Json(name = "vehicleDetails") val vehicleDetails: MechanicVehicleDetailsDto,
    @Json(name = "location") val location: MechanicRegisterLocationDto,
    @Json(name = "services") val services: List<MechanicServiceDto>
)

@JsonClass(generateAdapter = true)
data class MechanicVehicleDetailsDto(
    @Json(name = "model") val model: String,
    @Json(name = "color") val color: String,
    @Json(name = "plateNumber") val plateNumber: String
)

@JsonClass(generateAdapter = true)
data class MechanicRegisterLocationDto(
    @Json(name = "address") val address: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lng") val lng: Double
)

@JsonClass(generateAdapter = true)
data class MechanicServiceDto(
    @Json(name = "type") val type: String,
    @Json(name = "specialization") val specialization: List<String>,
    @Json(name = "price") val price: Double
)

@JsonClass(generateAdapter = true)
data class MechanicRegisterResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class MechanicProfileResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "mechanicDetails") val mechanicDetails: MechanicDetailsDto? = null,
    @Json(name = "mechanic") val mechanic: MechanicRecordDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class MechanicDetailsDto(
    @Json(name = "vehicleDetails") val vehicleDetails: MechanicVehicleDetailsDto? = null,
    @Json(name = "location") val location: MechanicRegisterLocationDto? = null,
    @Json(name = "services") val services: List<MechanicServiceDto>? = null,
    @Json(name = "experience") val experience: Int? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "status") val status: String? = null
)

@JsonClass(generateAdapter = true)
data class MechanicRecordDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "_id") val underscoreId: String? = null,
    @Json(name = "userId") val userId: String? = null,
    @Json(name = "vehicleDetails") val vehicleDetails: MechanicVehicleDetailsDto? = null,
    @Json(name = "location") val location: MechanicRegisterLocationDto? = null,
    @Json(name = "services") val services: List<MechanicServiceDto>? = null,
    @Json(name = "experience") val experience: Int? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "completedJobs") val completedJobs: Int? = null,
    @Json(name = "earnings") val earnings: EarningsDto? = null,
    @Json(name = "rating") val rating: Double? = null,
    @Json(name = "totalReviews") val totalReviews: Int? = null,
    @Json(name = "currentOrderId") val currentOrderId: String? = null
) {
    fun getRecordId(): String = id ?: underscoreId ?: ""
}

@JsonClass(generateAdapter = true)
data class EarningsDto(
    @Json(name = "today") val today: Double? = 0.0,
    @Json(name = "week") val week: Double? = 0.0,
    @Json(name = "month") val month: Double? = 0.0,
    @Json(name = "total") val total: Double? = 0.0
)

@JsonClass(generateAdapter = true)
data class UpdateStatusRequest(
    @Json(name = "isOnline") val isOnline: Boolean,
    @Json(name = "isAvailable") val isAvailable: Boolean? = true
)

@JsonClass(generateAdapter = true)
data class UpdateStatusResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class NearbyMechanicsRequest(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lng") val lng: Double,
    @Json(name = "radius") val radius: Double = 10.0
)

@JsonClass(generateAdapter = true)
data class NearbyMechanicsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "mechanics") val mechanics: List<NearbyMechanicDto>? = null,
    @Json(name = "total") val total: Int? = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class NearbyMechanicDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "userId") val userId: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "phoneNumber") val phoneNumber: String? = null,
    @Json(name = "profileImage") val profileImage: String? = null,
    @Json(name = "rating") val rating: Double? = null,
    @Json(name = "totalReviews") val totalReviews: Int? = null,
    @Json(name = "vehicleDetails") val vehicleDetails: MechanicVehicleDetailsDto? = null,
    @Json(name = "location") val location: MechanicRegisterLocationDto? = null,
    @Json(name = "services") val services: List<MechanicServiceDto>? = null,
    @Json(name = "experience") val experience: Int? = null,
    @Json(name = "distance") val distance: Double? = null,
    @Json(name = "eta") val eta: Int? = null,
    @Json(name = "isOnline") val isOnline: Boolean? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "status") val status: String? = null
)
