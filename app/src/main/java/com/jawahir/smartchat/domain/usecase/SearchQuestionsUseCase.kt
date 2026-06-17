package com.jawahir.smartchat.domain.usecase

import com.jawahir.smartchat.domain.repository.QaRepository
import com.jawahir.smartchat.domain.model.QaEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchQuestionsUseCase @Inject constructor(
    private val repository: QaRepository
) {
    operator fun invoke(keyword: String): Flow<List<QaEntry>> = repository.search(keyword)
}