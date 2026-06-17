package com.jawahir.smartchat.domain.usecase

import com.jawahir.smartchat.domain.repository.QaRepository
import javax.inject.Inject

class SubmitFeedbackUseCase @Inject constructor(
    private val repository: QaRepository
) {
    suspend operator fun invoke(id: Int, helpful: Boolean) {
        if (helpful) repository.markHelpful(id)
        else repository.markNotHelpful(id)
    }
}