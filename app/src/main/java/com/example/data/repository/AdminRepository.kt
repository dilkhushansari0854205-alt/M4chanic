package com.example.data.repository

import com.example.data.dto.AdminDashboardDataDto
import com.example.data.dto.AdminLiveTrackingDataDto
import com.example.data.dto.AdminLoginRequest
import com.example.data.dto.AdminMechanicDetailDto
import com.example.data.dto.AdminVerifyRequest
import com.example.data.remote.ApiService
import com.example.data.remote.ErrorParser
import com.example.data.remote.NetworkResult
import com.example.data.session.SessionManager
import com.example.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class AdminRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private val _dashboardData = MutableStateFlow<AdminDashboardDataDto?>(null)
    val dashboardData: StateFlow<AdminDashboardDataDto?> = _dashboardData.asStateFlow()

    private val _pendingMechanics = MutableStateFlow<List<AdminMechanicDetailDto>>(emptyList())
    val pendingMechanics: StateFlow<List<AdminMechanicDetailDto>> = _pendingMechanics.asStateFlow()

    private val _allMechanics = MutableStateFlow<List<AdminMechanicDetailDto>>(emptyList())
    val allMechanics: StateFlow<List<AdminMechanicDetailDto>> = _allMechanics.asStateFlow()

    private val _liveTrackingData = MutableStateFlow<AdminLiveTrackingDataDto?>(null)
    val liveTrackingData: StateFlow<AdminLiveTrackingDataDto?> = _liveTrackingData.asStateFlow()

    suspend fun adminLogin(phoneNumber: String): NetworkResult<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.adminLogin(AdminLoginRequest(phoneNumber))
            if (response.isSuccessful && response.body()?.success == true) {
                NetworkResult.Success(response.body()?.message ?: "OTP sent to admin phone")
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun adminVerify(phoneNumber: String, otp: String): NetworkResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.adminVerify(AdminVerifyRequest(phoneNumber, otp))
            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val token = body.token ?: ""
                if (token.isNotBlank()) {
                    sessionManager.saveToken(token)
                }
                val uDto = body.user
                val adminUser = User(
                    id = uDto?.getEffectiveId() ?: "admin_${System.currentTimeMillis()}",
                    phoneNumber = uDto?.phoneNumber ?: phoneNumber,
                    fullName = uDto?.fullName ?: "Super Admin",
                    email = uDto?.email ?: "",
                    role = "admin",
                    profileImage = null,
                    rating = 5.0
                )
                sessionManager.saveUser(adminUser)
                sessionManager.saveRole("admin")
                NetworkResult.Success(adminUser)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchDashboard(): NetworkResult<AdminDashboardDataDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAdminDashboard()
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data ?: AdminDashboardDataDto()
                _dashboardData.value = data
                NetworkResult.Success(data)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchPendingMechanics(): NetworkResult<List<AdminMechanicDetailDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAdminPendingMechanics()
            if (response.isSuccessful && response.body()?.success == true) {
                val list = response.body()?.mechanics ?: emptyList()
                _pendingMechanics.value = list
                NetworkResult.Success(list)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchAllMechanics(): NetworkResult<List<AdminMechanicDetailDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAdminMechanics()
            if (response.isSuccessful && response.body()?.success == true) {
                val list = response.body()?.mechanics ?: emptyList()
                _allMechanics.value = list
                NetworkResult.Success(list)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun approveMechanic(userId: String): NetworkResult<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.approveMechanic(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                fetchPendingMechanics()
                fetchAllMechanics()
                NetworkResult.Success(response.body()?.message ?: "Mechanic approved")
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun rejectMechanic(userId: String): NetworkResult<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.rejectMechanic(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                fetchPendingMechanics()
                fetchAllMechanics()
                NetworkResult.Success(response.body()?.message ?: "Mechanic rejected")
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchLiveTracking(): NetworkResult<AdminLiveTrackingDataDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAdminLiveTracking()
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data ?: AdminLiveTrackingDataDto()
                _liveTrackingData.value = data
                NetworkResult.Success(data)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }
}
