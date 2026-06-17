package com.jawahir.smartchat.ui.chat

import com.jawahir.smartchat.domain.matcher.KeywordMatcherStrategy
import com.jawahir.smartchat.domain.model.QaEntry
import com.jawahir.smartchat.domain.repository.QaRepository
import com.jawahir.smartchat.domain.usecase.FindBestMatchUseCase
import com.jawahir.smartchat.domain.usecase.SearchQuestionsUseCase
import com.jawahir.smartchat.domain.usecase.SeedDatabaseUseCase
import com.jawahir.smartchat.domain.usecase.SubmitFeedbackUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: ChatViewModel

    private val repository = mockk<QaRepository>()
    private val seedUseCase = mockk<SeedDatabaseUseCase>()
    private val searchUseCase = mockk<SearchQuestionsUseCase>()
    private val submitFeedbackUseCase = mockk<SubmitFeedbackUseCase>()
    private val findBestMatchUseCase = FindBestMatchUseCase(KeywordMatcherStrategy())

    private val fakeEntries = listOf(
        QaEntry(
            id = 1,
            question = "What is the weather like today?",
            answer = "Check a weather app.",
            weight = 1.0f
        ),
        QaEntry(
            id = 2,
            question = "How do I fix a slow internet connection?",
            answer = "Restart your router.",
            weight = 1.0f
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { seedUseCase() } returns Unit
        every { repository.allEntries } returns flowOf(fakeEntries)
        every { searchUseCase(any()) } returns flowOf(emptyList())

        viewModel = ChatViewModel(
            seedDatabaseUseCase = seedUseCase,
            findBestMatchUseCase = findBestMatchUseCase,
            searchQuestionsUseCase = searchUseCase,
            submitFeedbackUseCase = submitFeedbackUseCase,
            repository = repository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sending a message adds user bubble then bot bubble`() = runTest {
        viewModel.onEvent(ChatUiEvent.OnSendMessage("weather today"))

        val messages = viewModel.uiState.value.messages
        assertTrue(messages[0] is ChatMessage.UserMessage)
        assertEquals("Check a weather app.", (messages[1] as ChatMessage.BotMessage).text)
    }

    @Test
    fun `sending blank message does not add anything to chat`() = runTest {
        viewModel.onEvent(ChatUiEvent.OnSendMessage(""))

        assertTrue(viewModel.uiState.value.messages.isEmpty())
    }
}