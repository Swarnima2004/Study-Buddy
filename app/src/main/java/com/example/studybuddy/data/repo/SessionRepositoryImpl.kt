package com.example.studybuddy.data.repo

import com.example.studybuddy.data.local.SessionDao
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.domain.repository.SessionRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl@Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun insertSession(session: session) {
       sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: session) {
        TODO("Not yet implemented")
    }

    override fun getAllSessions(): Flow<List<session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentFiveSessions(): Flow<List<session>> {
        return sessionDao.getAllSessions().take(count = 5)
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        return sessionDao.getTotalSessionDuration()
    }

    override fun getTotalSessionDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }


}