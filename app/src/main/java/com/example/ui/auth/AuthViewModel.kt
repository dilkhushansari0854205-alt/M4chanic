package com.example.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.remote.NetworkResult
import com.example.data.repository.AuthRepository
import com.example.data.session.SessionManager
import com.example.domain.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class OtpSent(val phoneNumber: String, val fullName: String, val sessionId: String) : AuthUiState
    data class Success(val user: User, val isNewUser: Boolean = false) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _resendCountdown = MutableStateFlow(30)
    val resendCountdown: StateFlow<Int> = _resendCountdown.asStateFlow()

    private var countdownJob: Job? = null

    // Cache challenge and nonce
    private var cachedChallengeId: String? = null
    private var cachedNonce: String? = null

    fun sendOtp(phoneNumber: String, fullName: String = "Mechanic Partner") {
        val cleanPhone = phoneNumber.filter { it.isDigit() }
        val normalizedPhone = if (cleanPhone.length == 12 && cleanPhone.startsWith("91")) {
            cleanPhone.substring(2)
        } else {
            cleanPhone
        }

        if (normalizedPhone.length != 10 || !normalizedPhone.matches(Regex("^[6-9]\\d{9}$"))) {
            _uiState.value = AuthUiState.Error("Please enter a valid 10-digit Indian mobile number")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // 1. Get challenge
            val challengeResult = authRepository.getChallenge()
            if (challengeResult is NetworkResult.Error) {
                _uiState.value = AuthUiState.Error(challengeResult.message)
                return@launch
            }

            val (challengeId, nonce) = (challengeResult as NetworkResult.Success).data
            cachedChallengeId = challengeId
            cachedNonce = nonce

            // 2. Send OTP
            val sendResult = authRepository.sendOtp(
                phoneNumber = normalizedPhone,
                challengeId = challengeId,
                nonce = nonce
            )

            when (sendResult) {
                is NetworkResult.Success -> {
                    val sessionId = sendResult.data
                    _uiState.value = AuthUiState.OtpSent(
                        phoneNumber = normalizedPhone,
                        fullName = fullName,
                        sessionId = sessionId
                    )
                    startCountdown()
                }
                is NetworkResult.Error -> {
                    _uiState.value = AuthUiState.Error(sendResult.message)
                }
                else -> {}
            }
        }
    }

    fun verifyOtp(
        phoneNumber: String,
        otp: String,
        sessionId: String,
        fullName: String = "Mechanic Partner"
    ) {
        val cleanOtp = otp.filter { it.isDigit() }
        if (cleanOtp.length != 6) {
            _uiState.value = AuthUiState.Error("Please enter all 6 digits of the OTP")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val verifyResult = authRepository.verifyOtp(
                phoneNumber = phoneNumber,
                otp = cleanOtp,
                otpSessionId = sessionId,
                fullName = fullName
            )

            when (verifyResult) {
                is NetworkResult.Success -> {
                    _uiState.value = AuthUiState.Success(verifyResult.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = AuthUiState.Error(verifyResult.message)
                }
                else -> {}
            }
        }
    }

    fun resendOtp(phoneNumber: String, fullName: String) {
        if (_resendCountdown.value > 0) return
        sendOtp(phoneNumber, fullName)
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _resendCountdown.value = 30
        countdownJob = viewModelScope.launch {
            while (_resendCountdown.value > 0) {
                delay(1000)
                _resendCountdown.value -= 1
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository, sessionManager) as T
    }
}
