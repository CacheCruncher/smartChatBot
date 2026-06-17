package com.jawahir.smartchat.domain.usecase

import com.jawahir.smartchat.domain.repository.QaRepository
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val repository: QaRepository
) {
    suspend operator fun invoke() = repository.seedIfEmpty()
}