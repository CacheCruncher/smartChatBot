package com.jawahir.smartchat.data

import android.content.Context
import com.jawahir.smartchat.data.local.QaDao
import com.jawahir.smartchat.data.local.QaEntryEntity
import com.jawahir.smartchat.data.local.toDomain
import com.jawahir.smartchat.data.model.QaEntryDto
import com.jawahir.smartchat.domain.model.QaEntry
import com.jawahir.smartchat.domain.repository.QaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class QaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: QaDao
) : QaRepository {

    override val allEntries: Flow<List<QaEntry>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun search(keyword: String): Flow<List<QaEntry>> =
        dao.search(keyword).map { list -> list.map { it.toDomain() } }

    override suspend fun seedIfEmpty() {
        // read the bundled JSON asset file as a string
        val json = context.assets
            .open("qa_data.json")
            .bufferedReader()
            .use { it.readText() }

        val dtos: List<QaEntryDto> = Json.decodeFromString(json)

        val entities = dtos.map {
            QaEntryEntity(id = it.id, question = it.question, answer = it.answer)
        }
        dao.insertAll(entities)
    }

    override suspend fun markHelpful(id: Int) = dao.boostWeight(id)

    override suspend fun markNotHelpful(id: Int) = dao.reduceWeight(id)
}