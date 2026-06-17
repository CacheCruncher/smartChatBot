package com.jawahir.smartchat.di

import com.jawahir.smartchat.data.QaRepositoryImpl
import com.jawahir.smartchat.domain.matcher.KeywordMatcherStrategy
import com.jawahir.smartchat.domain.matcher.MatcherStrategy
import com.jawahir.smartchat.domain.repository.QaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMatcherStrategy(impl: KeywordMatcherStrategy): MatcherStrategy

    @Binds
    @Singleton
    abstract fun bindQaRepository(impl: QaRepositoryImpl): QaRepository
}