package com.example.data.repository

import com.example.data.dto.ChallengeRequest
import com.example.data.dto.SendOtpRequest
import com.example.data.dto.VerifyOtpRequest
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.data.remote.ApiService
import com.example.data.remote.ErrorParser
import com.example.data.remote.NetworkResult
import com.example.data.session.SessionManager
import com.example.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) {

    suspend fun getChallenge(): NetworkResult<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val deviceId = sessionManager.getDeviceId()
            val response = apiService.createChallenge(ChallengeRequest(deviceId))
            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val challengeId = body.challengeId ?: "challenge_${System.currentTimeMillis()}"
                val nonce = body.nonce ?: "nonce_${UUID.randomUUID()}"
                NetworkResult.Success(Pair(challengeId, nonce))
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun sendOtp(
        phoneNumber: String,
        challengeId: String,
        nonce: String
    ): NetworkResult<String> = withContext(Dispatchers.IO) {
        try {
            val deviceId = sessionManager.getDeviceId()
            val requestId = UUID.randomUUID().toString()
            val request = SendOtpRequest(
                phoneNumber = phoneNumber,
                deviceId = deviceId,
                challengeId = challengeId,
                nonce = nonce,
                timestamp = System.currentTimeMillis(),
                requestId = requestId
            )
            val response = apiService.sendOtp(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val otpSessionId = response.body()?.otpSessionId ?: "otp_session_${System.currentTimeMillis()}"
                NetworkResult.Success(otpSessionId)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun verifyOtp(
        phoneNumber: String,
        otp: String,
        otpSessionId: String,
        fullName: String
    ): NetworkResult<User> = withContext(Dispatchers.IO) {
        try {
            val deviceId = sessionManager.getDeviceId()
            val request = VerifyOtpRequest(
                phoneNumber = phoneNumber,
                otp = otp,
                otpSessionId = otpSessionId,
                deviceId = deviceId,
                fullName = fullName.ifBlank { "Mechanic Partner" }
            )
            val response = apiService.verifyOtp(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val token = body.token ?: ""
                if (token.isNotBlank()) {
                    sessionManager.saveToken(token)
                }

                val uDto = body.user
                val user = User(
                    id = uDto?.getEffectiveId() ?: "user_${System.currentTimeMillis()}",
                    phoneNumber = uDto?.phoneNumber ?: phoneNumber,
                    fullName = uDto?.fullName ?: fullName.ifBlank { "Mechanic Partner" },
                    email = uDto?.email ?: "",
                    role = uDto?.role ?: "customer",
                    profileImage = uDto?.profileImage,
                    rating = uDto?.rating ?: 5.0
                )

                sessionManager.saveUser(user)
                sessionManager.saveRole(user.role)
                userDao.saveUser(UserEntity.fromDomain(user))

                NetworkResult.Success(user)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun fetchProfile(): NetworkResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body()?.success == true) {
                val uDto = response.body()?.user
                if (uDto != null) {
                    val user = User(
                        id = uDto.getEffectiveId(),
                        phoneNumber = uDto.phoneNumber ?: "",
                        fullName = uDto.fullName ?: "Mechanic",
                        email = uDto.email ?: "",
                        role = uDto.role ?: "customer",
                        profileImage = uDto.profileImage,
                        rating = uDto.rating ?: 5.0
                    )
                    sessionManager.saveUser(user)
                    sessionManager.saveRole(user.role)
                    userDao.saveUser(UserEntity.fromDomain(user))
                    NetworkResult.Success(user)
                } else {
                    NetworkResult.Error("Failed to parse user profile")
                }
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            apiService.logout()
        } catch (_: Exception) {
            // Ignore network errors during logout
        } finally {
            sessionManager.clearSession()
            userDao.clearUser()
        }
    }
}
