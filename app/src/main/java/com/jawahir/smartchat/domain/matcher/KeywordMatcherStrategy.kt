package com.jawahir.smartchat.domain.matcher

import com.jawahir.smartchat.domain.model.QaEntry
import javax.inject.Inject

class KeywordMatcherStrategy @Inject constructor() : MatcherStrategy {

    override fun findBestMatch(query: String, entries: List<QaEntry>): QaEntry? {
        if (query.isBlank() || entries.isEmpty()) return null

        val words = query.lowercase().split(" ")

        fun matches(entry: QaEntry) = words.any { word ->
            entry.question.lowercase().contains(word)
        }

        return entries
            .filter { matches(it) }
            .maxByOrNull { it.weight }
    }
}