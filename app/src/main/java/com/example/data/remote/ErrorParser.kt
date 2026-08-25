package com.example.data.remote

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorParser {

    fun parseError(throwable: Throwable): NetworkResult.Error {
        return when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                NetworkResult.Error(
                    message = "No internet connection. Please check your network and try again.",
                    isNetworkError = true
                )
            }
            is IOException -> {
                NetworkResult.Error(
                    message = "Network error occurred. Please try again.",
                    isNetworkError = true
                )
            }
            is HttpException -> {
                val code = throwable.code()
                val errorBody = throwable.response()?.errorBody()?.string()
                parseHttpError(code, errorBody)
            }
            else -> {
                NetworkResult.Error(
                    message = throwable.localizedMessage ?: "An unexpected error occurred."
                )
            }
        }
    }

    fun parseHttpError(code: Int, errorBody: String?): NetworkResult.Error {
        if (code == 401) {
            return NetworkResult.Error(
                message = "Your session has expired. Please log in again.",
                code = 401,
                isSessionExpired = true
            )
        }

        if (errorBody.isNullOrBlank()) {
            val message = when (code) {
                400 -> "Invalid request. Please verify your details."
                403 -> "You do not have permission to perform this action."
                404 -> "Requested service or order was not found."
                429 -> "Too many requests. Please wait a moment and try again."
                500, 502, 503 -> "Server is temporarily unavailable. Please try again shortly."
                else -> "Server error ($code). Please try again."
            }
            return NetworkResult.Error(message = message, code = code)
        }

        try {
            val json = JSONObject(errorBody)

            // Format 1: { "success": false, "message": "..." }
            if (json.has("message") && !json.isNull("message")) {
                val msg = json.getString("message")
                val errorCode = if (json.has("code")) json.optString("code") else null
                return NetworkResult.Error(message = msg, code = code, errorCode = errorCode)
            }

            // Format 2: { "success": false, "error": "...", "code": "..." }
            if (json.has("error") && !json.isNull("error")) {
                val err = json.getString("error")
                val errorCode = if (json.has("code")) json.optString("code") else null
                return NetworkResult.Error(message = err, code = code, errorCode = errorCode)
            }

            // Format 3: { "success": false, "errors": [{ "msg": "..." }] }
            if (json.has("errors") && !json.isNull("errors")) {
                val errorsArray = json.getJSONArray("errors")
                if (errorsArray.length() > 0) {
                    val firstErr = errorsArray.getJSONObject(0)
                    val msg = firstErr.optString("msg", firstErr.optString("message", "Validation error"))
                    return NetworkResult.Error(message = msg, code = code)
                }
            }
        } catch (_: Exception) {
            // fallback if not valid JSON
        }

        return NetworkResult.Error(
            message = "Request failed ($code). Please try again.",
            code = code
        )
    }
}
