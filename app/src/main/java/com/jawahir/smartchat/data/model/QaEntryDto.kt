package com.jawahir.smartchat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QaEntryDto(
    val id: Int,
    val question: String,
    val answer: String
)