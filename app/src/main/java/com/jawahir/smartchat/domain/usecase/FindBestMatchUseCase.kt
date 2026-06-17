package com.jawahir.smartchat.domain.usecase

import javax.inject.Inject
import com.jawahir.smartchat.domain.matcher.MatcherStrategy
import com.jawahir.smartchat.domain.model.QaEntry

class FindBestMatchUseCase @Inject constructor(
    private val matcher: MatcherStrategy  // strategy is swappable
) {
    operator fun invoke(query: String, entries: List<QaEntry>): QaEntry? =
        matcher.findBestMatch(query, entries)
}