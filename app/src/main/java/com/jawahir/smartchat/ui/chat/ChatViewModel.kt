package com.jawahir.smartchat.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawahir.smartchat.domain.repository.QaRepository
import com.jawahir.smartchat.domain.usecase.FindBestMatchUseCase
import com.jawahir.smartchat.domain.usecase.SearchQuestionsUseCase
import com.jawahir.smartchat.domain.usecase.SeedDatabaseUseCase
import com.jawahir.smartchat.domain.usecase.SubmitFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val seedDatabaseUseCase: SeedDatabaseUseCase,
    private val findBestMatchUseCase: FindBestMatchUseCase,
    private val searchQuestionsUseCase: SearchQuestionsUseCase,
    private val submitFeedbackUseCase: SubmitFeedbackUseCase,
    private val repository: QaRepository
) : ViewModel() {

    // single state the UI observes
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ChatUiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        seedDatabase()
        observeSearchQuery()
    }

    // single entry point
    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is ChatUiEvent.OnSendMessage -> onSendMessage(event.query)
            is ChatUiEvent.OnFeedback -> onFeedback(event.entryId, event.helpful)
        }
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            seedDatabaseUseCase()
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        _uiState.flatMapLatest { state -> searchQuestionsUseCase(state.searchQuery) }
            .onEach { results -> _uiState.update { it.copy(searchResults = results) } }
            .launchIn(viewModelScope)
    }

    private fun onSendMessage(query: String) {
        if (query.isBlank()) return

        _uiState.update { state ->
            state.copy(messages = state.messages + ChatMessage.UserMessage(query))
        }

        viewModelScope.launch {
            val entries = repository.allEntries.first()
            val match = findBestMatchUseCase(query, entries)

            if (match != null) {
                val botMessage = ChatMessage.BotMessage(
                    entry = match,
                    text = match.answer
                )
                _uiState.update { state ->
                    state.copy(messages = state.messages + botMessage)
                }
            } else {
                _effect.emit(ChatUiEffect.ShowNoMatchSnackbar)
            }
        }
    }

    private fun onFeedback(entryId: Int, helpful: Boolean) {
        viewModelScope.launch {
            submitFeedbackUseCase(entryId, helpful)
        }
    }
}