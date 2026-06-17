package com.jawahir.smartchat.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jawahir.smartchat.domain.model.QaEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ChatUiEffect.ShowNoMatchSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = "Sorry no match. Try again."
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Offline Chatbot") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // search bar at the top
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = {
                    viewModel.onEvent(ChatUiEvent.OnSearchQueryChanged(it))
                }
            )

            // show search result
            if (uiState.searchQuery.isNotBlank()) {
                SearchResultsList(
                    results = uiState.searchResults,
                    modifier = Modifier.weight(1f)
                )
            } else {
                ChatMessagesList(
                    messages = uiState.messages,
                    onFeedback = { entryId, helpful ->
                        viewModel.onEvent(ChatUiEvent.OnFeedback(entryId, helpful))
                    },
                    modifier = Modifier.weight(1f)
                )
                ChatInput(
                    onSend = { text ->
                        viewModel.onEvent(ChatUiEvent.OnSendMessage(text))
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        placeholder = { Text("Search ...") }
    )
}

@Composable
private fun SearchResultsList(
    results: List<QaEntry>,
    modifier: Modifier = Modifier
) {
    if (results.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No results found")
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results) { entry ->
            SearchResultCard(entry = entry)
        }
    }
}

@Composable
private fun SearchResultCard(entry: QaEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = entry.question)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = entry.answer)
        }
    }
}

@Composable
private fun ChatMessagesList(
    messages: List<ChatMessage>,
    onFeedback: (entryId: Int, helpful: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { message ->
            when (message) {
                is ChatMessage.UserMessage -> UserBubble(text = message.text)
                is ChatMessage.BotMessage -> BotBubble(
                    message = message,
                    onFeedback = onFeedback
                )
            }
        }
    }
}

@Composable
private fun UserBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun BotBubble(
    message: ChatMessage.BotMessage,
    onFeedback: (entryId: Int, helpful: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier.widthIn(max = 280.dp)) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            FeedbackButtons(
                onHelpful = { onFeedback(message.entry.id, true) },
                onNotHelpful = { onFeedback(message.entry.id, false) }
            )
        }
    }
}


@Composable
private fun FeedbackButtons(
    onHelpful: () -> Unit,
    onNotHelpful: () -> Unit
) {
    var feedbackGiven by rememberSaveable { mutableStateOf(false) }

    if (feedbackGiven) {
        Text(
            text = "Thanks for the feedback!",
            style = MaterialTheme.typography.labelSmall
        )
    } else {
        Row {
            TextButton(
                onClick = {
                    onHelpful()
                    feedbackGiven = true
                }
            ) {
                Text("Helpful")
            }
            TextButton(
                onClick = {
                    onNotHelpful()
                    feedbackGiven = true
                }
            ) {
                Text("Not helpful")
            }
        }
    }
}


@Composable
private fun ChatInput(
    onSend: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ask ...") }
        )

        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onSend(text)
                    text = ""
                }
            }
        ) {
            Icon(Icons.AutoMirrored.Default.Send, "Send")
        }
    }
}