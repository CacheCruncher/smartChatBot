package com.jawahir.smartchat.di

import android.content.Context
import androidx.room.Room
import com.jawahir.smartchat.data.local.AppDatabase
import com.jawahir.smartchat.data.local.QaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "chatbot_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQaDao(db: AppDatabase): QaDao = db.qaDao()
}