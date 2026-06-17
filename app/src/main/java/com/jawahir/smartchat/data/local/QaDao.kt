package com.jawahir.smartchat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QaDao {

    // insert all 50 entries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entries: List<QaEntryEntity>)

    @Query("SELECT * FROM qa_entries")
    fun getAll(): Flow<List<QaEntryEntity>>

    // search bar — return matched keyword
    @Query("""
        SELECT * FROM qa_entries
        WHERE question LIKE '%' || :keyword || '%'
        ORDER BY question ASC
    """)
    fun search(keyword: String): Flow<List<QaEntryEntity>>

    // positive feedback — add 0.1 to weight
    @Query("UPDATE qa_entries SET weight = weight + 0.1 WHERE id = :id")
    suspend fun boostWeight(id: Int)

    // negative feedback — subtract 0.1 from weight
    @Query("UPDATE qa_entries SET weight = weight - 0.1 WHERE id = :id")
    suspend fun reduceWeight(id: Int)
}