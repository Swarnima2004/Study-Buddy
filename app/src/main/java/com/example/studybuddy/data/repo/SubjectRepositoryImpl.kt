package com.example.studybuddy.data.repo

import com.example.studybuddy.data.local.SubjectDAO
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.repository.SubjectRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl@Inject constructor(
    private val subjectDAO: SubjectDAO
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

    override suspend fun deleteSubject(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectInt: Int): Subjects? {
        TODO("Not yet implemented")
    }

    override fun getAllSubjects(): Flow<List<Subjects>> {
       return subjectDAO.getAllSubjects()
    }


}