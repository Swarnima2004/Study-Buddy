package com.example.studybuddy.di

import com.example.studybuddy.data.repo.SessionRepositoryImpl
import com.example.studybuddy.data.repo.SubjectRepositoryImpl
import com.example.studybuddy.data.repo.TaskRepositoryImpl
import com.example.studybuddy.domain.repository.SessionRepository
import com.example.studybuddy.domain.repository.SubjectRepository
import com.example.studybuddy.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImpl
    ) : SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl): SessionRepository
}