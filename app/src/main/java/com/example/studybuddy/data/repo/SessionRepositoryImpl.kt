package com.example.studybuddy.data.repo

import com.example.studybuddy.data.local.SessionDao
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.domain.repository.SessionRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl@Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun insertSession(session: session) {
       sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: session) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSessions(): Flow<List<session>> {
        return sessionDao.getAllSessions().map{ session->
            session.sortedByDescending { it.date }
        }
    }

    override fun getRecentFiveSessions(): Flow<List<session>> {
        return sessionDao.getAllSessions()
            .map{ session->
                session.sortedByDescending { it.date }
            }
            .take(count = 5)
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<session>> {
       return sessionDao.getRecentSessionForSubject(subjectId)
           .map{ session->
               session.sortedByDescending { it.date }
           }
           .take(count =10)
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        return sessionDao.getTotalSessionDuration()
    }

    override fun getTotalSessionDurationBySubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionDurationBySubject(subjectId)
    }


}