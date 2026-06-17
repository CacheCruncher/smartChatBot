package com.jawahir.smartchat.domain.matcher

import com.jawahir.smartchat.domain.model.QaEntry

// Future:  implementations could alos include Fuzzy or other strategy etc.
interface MatcherStrategy {
    fun findBestMatch(query: String, entries: List<QaEntry>): QaEntry?
}