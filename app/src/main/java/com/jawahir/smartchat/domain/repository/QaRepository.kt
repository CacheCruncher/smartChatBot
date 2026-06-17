package com.jawahir.smartchat.domain.repository

import com.jawahir.smartchat.domain.model.QaEntry
import kotlinx.coroutines.flow.Flow

interface QaRepository {
    val allEntries: Flow<List<QaEntry>>

    fun search(keyword: String): Flow<List<QaEntry>>

    suspend fun seedIfEmpty()

    suspend fun markHelpful(id: Int)

    suspend fun markNotHelpful(id: Int)
}