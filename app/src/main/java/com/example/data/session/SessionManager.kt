package com.example.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("m4chanic_mechanic_prefs", Context.MODE_PRIVATE)

    private val _sessionExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpiredEvent: SharedFlow<Unit> = _sessionExpiredEvent.asSharedFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _activeOrderId = MutableStateFlow<String?>(null)
    val activeOrderId: StateFlow<String?> = _activeOrderId.asStateFlow()

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _mechanicStatus = MutableStateFlow("pending")
    val mechanicStatus: StateFlow<String> = _mechanicStatus.asStateFlow()

    init {
        // Load initial state
        val token = getToken()
        if (!token.isNullOrBlank()) {
            val id = prefs.getString(KEY_USER_ID, "") ?: ""
            val name = prefs.getString(KEY_USER_NAME, "Mechanic") ?: "Mechanic"
            val phone = prefs.getString(KEY_USER_PHONE, "") ?: ""
            val email = prefs.getString(KEY_USER_EMAIL, "") ?: ""
            val role = prefs.getString(KEY_USER_ROLE, "customer") ?: "customer"
            val rating = prefs.getFloat(KEY_USER_RATING, 4.8f).toDouble()
            _currentUser.value = User(
                id = id,
                phoneNumber = phone,
                fullName = name,
                email = email,
                role = role,
                rating = rating
            )
        }
        _activeOrderId.value = prefs.getString(KEY_ACTIVE_ORDER_ID, null)
        _isOnline.value = prefs.getBoolean(KEY_IS_ONLINE, false)
        _mechanicStatus.value = prefs.getString(KEY_MECHANIC_STATUS, "pending") ?: "pending"
    }

    fun getDeviceId(): String {
        var deviceId = prefs.getString(KEY_DEVICE_ID, null)
        if (deviceId.isNullOrBlank()) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
        }
        return deviceId
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrBlank()
    }

    fun saveUser(user: User) {
        prefs.edit()
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.fullName)
            .putString(KEY_USER_PHONE, user.phoneNumber)
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_USER_ROLE, user.role)
            .putFloat(KEY_USER_RATING, user.rating.toFloat())
            .apply()
        _currentUser.value = user
    }

    fun saveRole(role: String) {
        prefs.edit().putString(KEY_USER_ROLE, role).apply()
        val current = _currentUser.value
        if (current != null) {
            _currentUser.value = current.copy(role = role)
        }
    }

    fun getRole(): String {
        return prefs.getString(KEY_USER_ROLE, _currentUser.value?.role ?: "customer") ?: "customer"
    }

    fun saveMechanicStatus(status: String) {
        prefs.edit().putString(KEY_MECHANIC_STATUS, status).apply()
        _mechanicStatus.value = status
    }

    fun getMechanicStatus(): String {
        return prefs.getString(KEY_MECHANIC_STATUS, "pending") ?: "pending"
    }

    fun saveMechanicRecordId(id: String) {
        prefs.edit().putString(KEY_MECHANIC_RECORD_ID, id).apply()
    }

    fun getMechanicRecordId(): String? {
        return prefs.getString(KEY_MECHANIC_RECORD_ID, null)
    }

    fun saveActiveCustomerUserId(customerId: String?) {
        if (customerId != null) {
            prefs.edit().putString(KEY_ACTIVE_CUSTOMER_ID, customerId).apply()
        } else {
            prefs.edit().remove(KEY_ACTIVE_CUSTOMER_ID).apply()
        }
    }

    fun getActiveCustomerUserId(): String? {
        return prefs.getString(KEY_ACTIVE_CUSTOMER_ID, null)
    }

    fun setActiveOrderId(orderId: String?) {
        if (orderId != null) {
            prefs.edit().putString(KEY_ACTIVE_ORDER_ID, orderId).apply()
        } else {
            prefs.edit().remove(KEY_ACTIVE_ORDER_ID).apply()
        }
        _activeOrderId.value = orderId
    }

    fun getActiveOrderId(): String? {
        return _activeOrderId.value ?: prefs.getString(KEY_ACTIVE_ORDER_ID, null)
    }

    fun setOnlineStatus(online: Boolean) {
        prefs.edit().putBoolean(KEY_IS_ONLINE, online).apply()
        _isOnline.value = online
    }

    fun getOnlineStatus(): Boolean {
        return _isOnline.value
    }

    fun notifySessionExpired() {
        clearSession()
        _sessionExpiredEvent.tryEmit(Unit)
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_PHONE)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_ROLE)
            .remove(KEY_USER_RATING)
            .remove(KEY_ACTIVE_ORDER_ID)
            .remove(KEY_ACTIVE_CUSTOMER_ID)
            .remove(KEY_IS_ONLINE)
            .remove(KEY_MECHANIC_STATUS)
            .remove(KEY_MECHANIC_RECORD_ID)
            .apply()
        _currentUser.value = null
        _activeOrderId.value = null
        _isOnline.value = false
        _mechanicStatus.value = "pending"
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_RATING = "user_rating"
        private const val KEY_ACTIVE_ORDER_ID = "active_order_id"
        private const val KEY_ACTIVE_CUSTOMER_ID = "active_customer_id"
        private const val KEY_IS_ONLINE = "is_online"
        private const val KEY_MECHANIC_STATUS = "mechanic_status"
        private const val KEY_MECHANIC_RECORD_ID = "mechanic_record_id"
    }
}
