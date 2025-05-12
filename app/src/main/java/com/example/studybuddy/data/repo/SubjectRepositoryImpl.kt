package com.example.studybuddy.data.repo

import com.example.studybuddy.data.local.SessionDao
import com.example.studybuddy.data.local.SubjectDAO
import com.example.studybuddy.data.local.TaskDao
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.repository.SubjectRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl@Inject constructor(
    private val subjectDAO: SubjectDAO,
    private val taskDao : TaskDao,
    private val sessionDao : SessionDao

): SubjectRepository {
    override suspend fun upsertSubject(subject: Subjects) {
        subjectDAO.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
       return subjectDAO.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
       return subjectDAO.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectId: Int) {
         taskDao.deleteTasksBySubjectId(subjectId)
        sessionDao.deleteSessionBySubjectId(subjectId)
        subjectDAO.deleteSubject(subjectId)
    }

    override suspend fun getSubjectById(subjectId: Int): Subjects? {
        return subjectDAO.getSubjectById(subjectId)
    }

    override fun getAllSubjects(): Flow<List<Subjects>> {
       return subjectDAO.getAllSubjects()
    }


}