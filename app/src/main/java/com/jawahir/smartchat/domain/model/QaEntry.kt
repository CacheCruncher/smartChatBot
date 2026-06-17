package com.jawahir.smartchat.domain.model

data class QaEntry(
    val id: Int,
    val question: String,
    val answer: String,
    val weight: Float = 1.0f
)