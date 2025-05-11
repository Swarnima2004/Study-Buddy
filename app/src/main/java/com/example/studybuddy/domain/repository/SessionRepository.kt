package com.example.studybuddy.domain.repository

import com.example.studybuddy.domain.model.session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun insertSession(session: session)

    suspend fun deleteSession(session: session)

    fun getAllSessions(): Flow<List<session>>

    fun getRecentFiveSessions() : Flow<List<session>>

    fun getRecentTenSessionsForSubject(subjectId : Int) :Flow<List<session>>

    fun getTotalSessionDuration(): Flow<Long>

    fun getTotalSessionDurationBySubjectId(subjectId : Int) : Flow<Long>

}