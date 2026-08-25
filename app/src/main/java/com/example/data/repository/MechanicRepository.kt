package com.example.data.repository

import com.example.data.dto.EarningsDto
import com.example.data.dto.MechanicDetailsDto
import com.example.data.dto.MechanicRecordDto
import com.example.data.dto.MechanicRegisterLocationDto
import com.example.data.dto.MechanicRegisterRequest
import com.example.data.dto.MechanicRegisterResponse
import com.example.data.dto.MechanicServiceDto
import com.example.data.dto.MechanicVehicleDetailsDto
import com.example.data.dto.UpdateStatusRequest
import com.example.data.remote.ApiService
import com.example.data.remote.ErrorParser
import com.example.data.remote.NetworkResult
import com.example.data.session.SessionManager
import com.example.domain.model.EarningsSummary
import com.example.domain.model.MechanicProfile
import com.example.domain.model.MechanicServiceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MechanicRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private val _mechanicProfile = MutableStateFlow<MechanicProfile?>(null)
    val mechanicProfile: StateFlow<MechanicProfile?> = _mechanicProfile.asStateFlow()

    fun getDefaultServices(): List<MechanicServiceItem> {
        return listOf(
            MechanicServiceItem(
                type = "Puncture",
                specialization = listOf("Two Wheeler", "Car"),
                price = 199.0
            ),
            MechanicServiceItem(
                type = "Battery",
                specialization = listOf("Two Wheeler", "Car"),
                price = 249.0
            ),
            MechanicServiceItem(
                type = "Engine",
                specialization = listOf("Car"),
                price = 349.0
            ),
            MechanicServiceItem(
                type = "Towing",
                specialization = listOf("Car"),
                price = 599.0
            ),
            MechanicServiceItem(
                type = "Fuel Delivery",
                specialization = listOf("Two Wheeler", "Car"),
                price = 149.0
            ),
            MechanicServiceItem(
                type = "Key Lockout",
                specialization = listOf("Car"),
                price = 299.0
            )
        )
    }

    suspend fun registerMechanic(
        fullName: String,
        email: String,
        experience: Int,
        vehicleModel: String,
        vehicleColor: String,
        vehiclePlate: String,
        address: String,
        lat: Double,
        lng: Double,
        services: List<MechanicServiceItem>
    ): NetworkResult<MechanicRegisterResponse> = withContext(Dispatchers.IO) {
        try {
            val request = MechanicRegisterRequest(
                fullName = fullName,
                email = email,
                experience = experience,
                vehicleDetails = MechanicVehicleDetailsDto(
                    model = vehicleModel,
                    color = vehicleColor,
                    plateNumber = vehiclePlate
                ),
                location = MechanicRegisterLocationDto(
                    address = address,
                    lat = lat,
                    lng = lng
                ),
                services = services.map {
                    MechanicServiceDto(
                        type = it.type,
                        specialization = it.specialization,
                        price = it.price
                    )
                }
            )

            val response = apiService.registerMechanic(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val status = body.status ?: "pending"
                sessionManager.saveRole("mechanic")
                sessionManager.saveMechanicStatus(status)

                // Update local profile representation
                fetchMechanicProfile()

                NetworkResult.Success(body)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchMechanicProfile(): NetworkResult<MechanicProfile> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMechanicProfile()
            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val userDto = body.user
                val mechDto = body.mechanic
                val detailsDto = body.mechanicDetails

                val profile = mapToMechanicProfile(userDto, mechDto, detailsDto)

                sessionManager.saveMechanicStatus(profile.status)
                sessionManager.setOnlineStatus(profile.isOnline)
                if (profile.id.isNotBlank()) {
                    sessionManager.saveMechanicRecordId(profile.id)
                }
                if (!profile.currentOrderId.isNullOrBlank()) {
                    sessionManager.setActiveOrderId(profile.currentOrderId)
                }

                _mechanicProfile.value = profile
                NetworkResult.Success(profile)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun updateStatus(isOnline: Boolean, isAvailable: Boolean = true): NetworkResult<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateMechanicStatus(
                    UpdateStatusRequest(isOnline = isOnline, isAvailable = isAvailable)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    val resultOnline = response.body()?.isOnline ?: isOnline
                    sessionManager.setOnlineStatus(resultOnline)
                    _mechanicProfile.value = _mechanicProfile.value?.copy(isOnline = resultOnline)
                    NetworkResult.Success(resultOnline)
                } else {
                    ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
                }
            } catch (e: Exception) {
                ErrorParser.parseError(e)
            }
        }

    suspend fun fetchNearbyMechanics(
        lat: Double,
        lng: Double,
        radius: Double = 15.0
    ): NetworkResult<List<com.example.data.dto.NearbyMechanicDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getNearbyMechanics(
                com.example.data.dto.NearbyMechanicsRequest(lat = lat, lng = lng, radius = radius)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                val list = response.body()?.mechanics ?: emptyList()
                NetworkResult.Success(list)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    private fun mapToMechanicProfile(
        userDto: com.example.data.dto.UserDto?,
        mechDto: MechanicRecordDto?,
        detailsDto: MechanicDetailsDto?
    ): MechanicProfile {
        val user = sessionManager.currentUser.value
        val name = userDto?.fullName ?: user?.fullName ?: "Rohit Sharma"
        val phone = userDto?.phoneNumber ?: user?.phoneNumber ?: "9876543210"
        val email = userDto?.email ?: user?.email ?: ""
        val status = mechDto?.status ?: detailsDto?.status ?: sessionManager.getMechanicStatus()
        val isOnline = mechDto?.isOnline ?: detailsDto?.isOnline ?: sessionManager.getOnlineStatus()
        val isAvailable = mechDto?.isAvailable ?: detailsDto?.isAvailable ?: true

        val vehicle = mechDto?.vehicleDetails ?: detailsDto?.vehicleDetails
        val loc = mechDto?.location ?: detailsDto?.location
        val servicesList = (mechDto?.services ?: detailsDto?.services)?.map {
            MechanicServiceItem(
                type = it.type,
                specialization = it.specialization,
                price = it.price
            )
        } ?: getDefaultServices()

        val earningsDto: EarningsDto? = mechDto?.earnings
        val earnings = EarningsSummary(
            today = earningsDto?.today ?: 0.0,
            week = earningsDto?.week ?: 0.0,
            month = earningsDto?.month ?: 0.0,
            total = earningsDto?.total ?: 0.0
        )

        return MechanicProfile(
            id = mechDto?.getRecordId() ?: "mech_${System.currentTimeMillis()}",
            userId = userDto?.getEffectiveId() ?: user?.id ?: "",
            fullName = name,
            phoneNumber = phone,
            email = email,
            rating = mechDto?.rating ?: userDto?.rating ?: 4.8,
            totalReviews = mechDto?.totalReviews ?: 0,
            completedJobs = mechDto?.completedJobs ?: 0,
            experience = mechDto?.experience ?: detailsDto?.experience ?: 5,
            isOnline = isOnline,
            isAvailable = isAvailable,
            status = status,
            vehicleModel = vehicle?.model ?: "Maruti Suzuki Eeco",
            vehicleColor = vehicle?.color ?: "White",
            vehiclePlate = vehicle?.plateNumber ?: "BR11AB1234",
            workAddress = loc?.address ?: "Purnia, Bihar",
            workLat = loc?.lat ?: 25.7771,
            workLng = loc?.lng ?: 87.4753,
            services = servicesList,
            earnings = earnings,
            currentOrderId = mechDto?.currentOrderId
        )
    }
}
