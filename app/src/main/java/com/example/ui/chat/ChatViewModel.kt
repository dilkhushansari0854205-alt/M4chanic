package com.example.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.remote.NetworkResult
import com.example.data.repository.ChatRepository
import com.example.data.session.SessionManager
import com.example.data.socket.SocketManager
import com.example.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    fun loadChat(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = chatRepository.getChatHistory(orderId)
            _isLoading.value = false
            if (result is NetworkResult.Success) {
                _messages.value = result.data
            }
        }

        // Listen to live socket messages
        viewModelScope.launch {
            socketManager.incomingMessages.collect { newMsg ->
                if (newMsg.orderId == orderId) {
                    val currentUserId = sessionManager.currentUser.value?.id ?: ""
                    val formattedMsg = newMsg.copy(isFromMe = newMsg.senderId == currentUserId)
                    if (_messages.value.none { it.id == formattedMsg.id }) {
                        _messages.value = _messages.value + formattedMsg
                    }
                }
            }
        }
    }

    fun sendMessage(orderId: String, receiverId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isSending.value = true
            val result = chatRepository.sendMessage(orderId, receiverId, text)
            _isSending.value = false
            if (result is NetworkResult.Success) {
                val sent = result.data
                if (_messages.value.none { it.id == sent.id }) {
                    _messages.value = _messages.value + sent
                }
            }
        }
    }
}

class ChatViewModelFactory(
    private val chatRepository: ChatRepository,
    private val socketManager: SocketManager,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(chatRepository, socketManager, sessionManager) as T
    }
}
