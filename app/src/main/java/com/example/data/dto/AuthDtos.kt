package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChallengeRequest(
    @Json(name = "deviceId") val deviceId: String? = null
)

@JsonClass(generateAdapter = true)
data class ChallengeResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "challengeId") val challengeId: String? = null,
    @Json(name = "nonce") val nonce: String? = null,
    @Json(name = "expiresIn") val expiresIn: Int? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class SendOtpRequest(
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "deviceId") val deviceId: String,
    @Json(name = "challengeId") val challengeId: String,
    @Json(name = "nonce") val nonce: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "requestId") val requestId: String
)

@JsonClass(generateAdapter = true)
data class SendOtpResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "phoneNumber") val phoneNumber: String? = null,
    @Json(name = "otpSessionId") val otpSessionId: String? = null,
    @Json(name = "expiresIn") val expiresIn: Int? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class VerifyOtpRequest(
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "otp") val otp: String,
    @Json(name = "otpSessionId") val otpSessionId: String,
    @Json(name = "deviceId") val deviceId: String,
    @Json(name = "fullName") val fullName: String = ""
)

@JsonClass(generateAdapter = true)
data class VerifyOtpResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "token") val token: String? = null,
    @Json(name = "isNewUser") val isNewUser: Boolean? = null,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "_id") val underscoreId: String? = null,
    @Json(name = "phoneNumber") val phoneNumber: String? = null,
    @Json(name = "fullName") val fullName: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "role") val role: String? = null, // "customer" or "mechanic"
    @Json(name = "profileImage") val profileImage: String? = null,
    @Json(name = "rating") val rating: Double? = null
) {
    fun getEffectiveId(): String = id ?: underscoreId ?: ""
}

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class GenericResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null,
    @Json(name = "code") val code: String? = null
)
