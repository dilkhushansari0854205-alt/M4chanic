package com.example.data.socket

import android.util.Log
import com.example.data.session.SessionManager
import com.example.domain.model.ChatMessage
import com.example.domain.model.IncomingOrderEvent
import com.example.domain.model.LocationPoint
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SocketManager(
    private val sessionManager: SessionManager
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var socket: Socket? = null

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _incomingOrders = MutableSharedFlow<IncomingOrderEvent>(extraBufferCapacity = 10)
    val incomingOrders: SharedFlow<IncomingOrderEvent> = _incomingOrders.asSharedFlow()

    private val _incomingMessages = MutableSharedFlow<ChatMessage>(extraBufferCapacity = 50)
    val incomingMessages: SharedFlow<ChatMessage> = _incomingMessages.asSharedFlow()

    private val _customerLocationUpdates = MutableSharedFlow<LocationPoint>(extraBufferCapacity = 20)
    val customerLocationUpdates: SharedFlow<LocationPoint> = _customerLocationUpdates.asSharedFlow()

    private val _typingEvents = MutableSharedFlow<Pair<String, Boolean>>(extraBufferCapacity = 10)
    val typingEvents: SharedFlow<Pair<String, Boolean>> = _typingEvents.asSharedFlow()

    private val _statusChangeEvents = MutableSharedFlow<String>(extraBufferCapacity = 5)
    val statusChangeEvents: SharedFlow<String> = _statusChangeEvents.asSharedFlow()

    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fun connect() {
        val token = sessionManager.getToken()
        if (token.isNullOrBlank()) {
            Log.d("SocketManager", "Cannot connect socket: No JWT token found")
            return
        }

        if (socket?.connected() == true) {
            return
        }

        try {
            val options = IO.Options.builder()
                .setAuth(mapOf("token" to token))
                .setTransports(arrayOf("websocket", "polling"))
                .setReconnection(true)
                .setReconnectionAttempts(Int.MAX_VALUE)
                .setReconnectionDelay(2000)
                .setReconnectionDelayMax(10000)
                .setTimeout(15000)
                .build()

            socket = IO.socket(URI.create("https://m4chanic-app-production.up.railway.app"), options).apply {
                on(Socket.EVENT_CONNECT) {
                    Log.d("SocketManager", "Connected to Socket.IO backend")
                    _isConnected.value = true
                    // Rejoin active order if any
                    val activeOrderId = sessionManager.getActiveOrderId()
                    if (!activeOrderId.isNullOrBlank()) {
                        joinOrder(activeOrderId)
                    }
                }

                on(Socket.EVENT_DISCONNECT) {
                    Log.d("SocketManager", "Disconnected from Socket.IO")
                    _isConnected.value = false
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    val err = args.firstOrNull()?.toString()
                    Log.w("SocketManager", "Socket connect error: $err")
                    _isConnected.value = false
                }

                // 1. New Order event broadcast to mechanic
                on("new_order") { args ->
                    try {
                        val json = args.firstOrNull() as? JSONObject ?: return@on
                        Log.d("SocketManager", "Received new_order: $json")

                        val orderId = json.optString("orderId", json.optString("id", ""))
                        val custId = json.optString("customerId", "")
                        val custName = json.optString("customerName", "Customer")
                        val serviceType = json.optString("serviceType", "Roadside Assistance")
                        val distance = json.optDouble("distance", 2.5)
                        val eta = json.optInt("eta", 10)
                        val timestamp = json.optLong("timestamp", System.currentTimeMillis())

                        var lat = 25.7800
                        var lng = 87.4700
                        var addr = "Customer Location"

                        if (json.has("customerLocation")) {
                            val locObj = json.optJSONObject("customerLocation")
                            if (locObj != null) {
                                lat = locObj.optDouble("lat", 25.7800)
                                lng = locObj.optDouble("lng", 87.4700)
                                addr = locObj.optString("address", "Customer Location")
                            }
                        } else if (json.has("location")) {
                            val locObj = json.optJSONObject("location")
                            if (locObj != null) {
                                lat = locObj.optDouble("lat", 25.7800)
                                lng = locObj.optDouble("lng", 87.4700)
                                addr = locObj.optString("address", "Customer Location")
                            }
                        }

                        val event = IncomingOrderEvent(
                            orderId = orderId,
                            customerId = custId,
                            customerName = custName,
                            serviceType = serviceType,
                            customerLocation = LocationPoint(lat, lng, addr),
                            distance = distance,
                            eta = eta,
                            timestamp = timestamp
                        )

                        scope.launch {
                            _incomingOrders.emit(event)
                        }
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error parsing new_order", e)
                    }
                }

                // 2. Customer Location Update
                on("customer_location_update") { args ->
                    try {
                        val json = args.firstOrNull() as? JSONObject ?: return@on
                        val loc = json.optJSONObject("location")
                        val lat = loc?.optDouble("lat") ?: json.optDouble("lat", 0.0)
                        val lng = loc?.optDouble("lng") ?: json.optDouble("lng", 0.0)
                        if (lat != 0.0 && lng != 0.0) {
                            scope.launch {
                                _customerLocationUpdates.emit(LocationPoint(lat, lng, "Customer"))
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error parsing customer_location_update", e)
                    }
                }

                // 3. New Message
                on("new_message") { args ->
                    try {
                        val json = args.firstOrNull() as? JSONObject ?: return@on
                        val msgId = json.optString("id", json.optString("_id", "msg_${System.currentTimeMillis()}"))
                        val orderId = json.optString("orderId", "")
                        val senderId = json.optString("senderId", "")
                        val receiverId = json.optString("receiverId", "")
                        val messageText = json.optString("message", "")
                        val timestamp = json.optLong("timestamp", System.currentTimeMillis())

                        val myUserId = sessionManager.currentUser.value?.id ?: ""
                        val isFromMe = (senderId == myUserId)

                        val chatMessage = ChatMessage(
                            id = msgId,
                            orderId = orderId,
                            senderId = senderId,
                            receiverId = receiverId,
                            message = messageText,
                            timestamp = timestamp,
                            timeFormatted = timeFormat.format(Date(timestamp)),
                            isFromMe = isFromMe
                        )

                        scope.launch {
                            _incomingMessages.emit(chatMessage)
                        }
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error parsing new_message", e)
                    }
                }

                // 4. Typing Events
                on("typing_start") { args ->
                    val json = args.firstOrNull() as? JSONObject
                    val orderId = json?.optString("orderId") ?: ""
                    scope.launch {
                        _typingEvents.emit(Pair(orderId, true))
                    }
                }

                on("typing_end") { args ->
                    val json = args.firstOrNull() as? JSONObject
                    val orderId = json?.optString("orderId") ?: ""
                    scope.launch {
                        _typingEvents.emit(Pair(orderId, false))
                    }
                }

                // 5. Mechanic Approved / Status Change
                on("mechanic_approved") {
                    sessionManager.saveMechanicStatus("approved")
                    scope.launch {
                        _statusChangeEvents.emit("approved")
                    }
                }

                on("mechanic_status_change") { args ->
                    val json = args.firstOrNull() as? JSONObject
                    val newStatus = json?.optString("status") ?: "approved"
                    sessionManager.saveMechanicStatus(newStatus)
                    scope.launch {
                        _statusChangeEvents.emit(newStatus)
                    }
                }

                connect()
            }
        } catch (e: Exception) {
            Log.e("SocketManager", "Error establishing socket connection", e)
        }
    }

    fun joinOrder(orderId: String) {
        if (socket?.connected() == true && orderId.isNotBlank()) {
            try {
                val data = JSONObject().apply {
                    put("orderId", orderId)
                }
                socket?.emit("join_order", data)
                Log.d("SocketManager", "Emitted join_order for: $orderId")
            } catch (e: Exception) {
                Log.e("SocketManager", "Failed to emit join_order", e)
            }
        }
    }

    fun updateMechanicLocation(lat: Double, lng: Double, orderId: String? = null) {
        if (socket?.connected() == true) {
            try {
                val activeId = orderId ?: sessionManager.getActiveOrderId()
                val data = JSONObject().apply {
                    put("lat", lat)
                    put("lng", lng)
                    if (!activeId.isNullOrBlank()) {
                        put("orderId", activeId)
                    }
                }
                socket?.emit("update_location", data)
            } catch (e: Exception) {
                Log.e("SocketManager", "Failed to emit update_location", e)
            }
        }
    }

    fun sendChatMessage(orderId: String, message: String, receiverId: String) {
        if (socket?.connected() == true) {
            try {
                val data = JSONObject().apply {
                    put("orderId", orderId)
                    put("message", message)
                    put("receiverId", receiverId)
                }
                socket?.emit("send_message", data)
            } catch (e: Exception) {
                Log.e("SocketManager", "Failed to emit send_message", e)
            }
        }
    }

    fun sendTypingStart(orderId: String, receiverId: String) {
        if (socket?.connected() == true) {
            try {
                val data = JSONObject().apply {
                    put("orderId", orderId)
                    put("receiverId", receiverId)
                }
                socket?.emit("typing_start", data)
            } catch (_: Exception) {}
        }
    }

    fun sendTypingEnd(orderId: String, receiverId: String) {
        if (socket?.connected() == true) {
            try {
                val data = JSONObject().apply {
                    put("orderId", orderId)
                    put("receiverId", receiverId)
                }
                socket?.emit("typing_end", data)
            } catch (_: Exception) {}
        }
    }

    fun disconnect() {
        try {
            socket?.disconnect()
            socket?.off()
            socket = null
            _isConnected.value = false
        } catch (e: Exception) {
            Log.e("SocketManager", "Error disconnecting socket", e)
        }
    }
}
