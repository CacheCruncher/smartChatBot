package com.jawahir.smartchat.ui.chat

import com.jawahir.smartchat.domain.model.QaEntry

// single source of truth for the entire screen
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<QaEntry> = emptyList(),
)

// every possible message type in the chat list
sealed class ChatMessage {
    data class UserMessage(val text: String) : ChatMessage()
    data class BotMessage(
        val entry: QaEntry,
        val text: String
    ) : ChatMessage()
}
// ui events
sealed class ChatUiEvent {
    data class OnSearchQueryChanged(val query: String) : ChatUiEvent()
    data class OnSendMessage(val query: String) : ChatUiEvent()
    data class OnFeedback(val entryId: Int, val helpful: Boolean) : ChatUiEvent()
}

sealed class ChatUiEffect {
    data object ShowNoMatchSnackbar : ChatUiEffect()
}