package com.example.data.remote

import com.example.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionManager.getToken()

        val newRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .header("Accept", "application/json")
                .build()
        }

        val response = chain.proceed(newRequest)

        // Handle HTTP 401: Unauthorized / Session Expired
        if (response.code == 401) {
            // Check if not an auth endpoint (challenge/login)
            val path = originalRequest.url.encodedPath
            if (!path.contains("/api/auth/challenge") && !path.contains("/api/auth/send-otp") && !path.contains("/api/auth/verify-otp")) {
                sessionManager.notifySessionExpired()
            }
        }

        return response
    }
}
