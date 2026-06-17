package com.jawahir.smartchat.domain.matcher

import com.jawahir.smartchat.domain.model.QaEntry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KeywordMatcherStrategyTest {

    private lateinit var matcher: KeywordMatcherStrategy

    private val entries = listOf(
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
        matcher = KeywordMatcherStrategy()
    }

    @Test
    fun `returns correct match for keyword in query`() {
        val result = matcher.findBestMatch("What is the weather", entries)
        assertEquals(1, result?.id)
    }
}