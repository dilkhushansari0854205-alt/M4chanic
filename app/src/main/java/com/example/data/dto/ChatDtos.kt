package com.example.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @Json(name = "orderId") val orderId: String,
    @Json(name = "message") val message: String,
    @Json(name = "receiverId") val receiverId: String
)

@JsonClass(generateAdapter = true)
data class ChatHistoryResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "messages") val messages: List<ChatMessageDto>? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class SendMessageResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String? = null,
    @Json(name = "data") val data: ChatMessageDto? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class ChatMessageDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "_id") val underscoreId: String? = null,
    @Json(name = "orderId") val orderId: String? = null,
    @Json(name = "senderId") val senderId: String? = null,
    @Json(name = "receiverId") val receiverId: String? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "createdAt") val createdAt: String? = null,
    @Json(name = "timestamp") val timestamp: Long? = null,
    @Json(name = "isRead") val isRead: Boolean? = null
) {
    fun getEffectiveId(): String = id ?: underscoreId ?: "msg_${timestamp ?: System.currentTimeMillis()}"
}
