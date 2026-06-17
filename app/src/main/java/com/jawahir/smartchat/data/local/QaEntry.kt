package com.jawahir.smartchat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jawahir.smartchat.domain.model.QaEntry

@Entity(tableName = "qa_entries")
data class QaEntryEntity(
    @PrimaryKey val id: Int,
    val question: String,
    val answer: String,
    // starts at 1.0
    val weight: Float = 1.0f
)

// convert to domain model
fun QaEntryEntity.toDomain() = QaEntry(
    id = id,
    question = question,
    answer = answer,
    weight = weight
)