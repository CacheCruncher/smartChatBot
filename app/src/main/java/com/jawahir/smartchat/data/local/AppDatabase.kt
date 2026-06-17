package com.jawahir.smartchat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QaEntryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun qaDao(): QaDao
}