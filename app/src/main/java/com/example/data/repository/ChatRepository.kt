package com.example.data.repository

import com.example.data.dto.SendMessageRequest
import com.example.data.remote.ApiService
import com.example.data.remote.ErrorParser
import com.example.data.remote.NetworkResult
import com.example.data.session.SessionManager
import com.example.data.socket.SocketManager
import com.example.domain.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRepository(
    private val apiService: ApiService,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) {

    suspend fun getChatHistory(orderId: String): NetworkResult<List<ChatMessage>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getChatHistory(orderId)
            if (response.isSuccessful && response.body()?.success == true) {
                val currentUserId = sessionManager.currentUser.value?.id ?: ""
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                val messages = response.body()?.messages?.map { dto ->
                    val timestamp = dto.timestamp ?: System.currentTimeMillis()
                    ChatMessage(
                        id = dto.getEffectiveId(),
                        orderId = dto.orderId ?: orderId,
                        senderId = dto.senderId ?: "",
                        receiverId = dto.receiverId ?: "",
                        message = dto.message ?: "",
                        timestamp = timestamp,
                        timeFormatted = timeFormat.format(Date(timestamp)),
                        isFromMe = dto.senderId == currentUserId,
                        isRead = dto.isRead ?: true
                    )
                } ?: emptyList()

                NetworkResult.Success(messages)
            } else {
                ErrorParser.parseHttpError(response.code(), response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ErrorParser.parseError(e)
        }
    }

    suspend fun sendMessage(
        orderId: String,
        receiverId: String,
        text: String
    ): NetworkResult<ChatMessage> = withContext(Dispatchers.IO) {
        if (text.isBlank()) {
            return@withContext NetworkResult.Error("Message cannot be empty")
        }
        if (text.length > 2000) {
            return@withContext NetworkResult.Error("Message exceeds 2,000 characters limit")
        }

        try {
            // Also emit via socket
            socketManager.sendChatMessage(orderId, text, receiverId)

            val request = SendMessageRequest(
                orderId = orderId,
                message = text.trim(),
                receiverId = receiverId
            )
            val response = apiService.sendMessage(request)
            val currentUserId = sessionManager.currentUser.value?.id ?: ""
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val now = System.currentTimeMillis()

            if (response.isSuccessful) {
                val dto = response.body()?.data
                val chatMessage = ChatMessage(
                    id = dto?.getEffectiveId() ?: "msg_$now",
                    orderId = orderId,
                    senderId = currentUserId,
                    receiverId = receiverId,
                    message = text.trim(),
                    timestamp = now,
                    timeFormatted = timeFormat.format(Date(now)),
                    isFromMe = true,
                    isRead = true
                )
                NetworkResult.Success(chatMessage)
            } else {
                // If REST returns error but socket was emitted, create local message
                val chatMessage = ChatMessage(
                    id = "msg_$now",
                    orderId = orderId,
                    senderId = currentUserId,
                    receiverId = receiverId,
                    message = text.trim(),
                    timestamp = now,
                    timeFormatted = timeFormat.format(Date(now)),
                    isFromMe = true,
                    isRead = true
                )
                NetworkResult.Success(chatMessage)
            }
        } catch (_: Exception) {
            // Fallback optimistic message
            val currentUserId = sessionManager.currentUser.value?.id ?: ""
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val now = System.currentTimeMillis()
            val chatMessage = ChatMessage(
                id = "msg_$now",
                orderId = orderId,
                senderId = currentUserId,
                receiverId = receiverId,
                message = text.trim(),
                timestamp = now,
                timeFormatted = timeFormat.format(Date(now)),
                isFromMe = true,
                isRead = true
            )
            NetworkResult.Success(chatMessage)
        }
    }
}
