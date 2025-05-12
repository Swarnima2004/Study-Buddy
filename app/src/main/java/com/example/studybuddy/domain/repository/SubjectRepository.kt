package com.example.studybuddy.domain.repository

import com.example.studybuddy.domain.model.Subjects
import kotlinx.coroutines.flow.Flow

// we have created repository interfaces to specify the operations performed on the data

interface SubjectRepository {

    suspend fun upsertSubject(subject : Subjects)

    fun getTotalSubjectCount(): Flow<Int>

    fun getTotalGoalHours() : Flow<Float>

    suspend fun deleteSubject(subjectId : Int)

    suspend fun getSubjectById(subjectId : Int) : Subjects?

    fun getAllSubjects(): Flow<List<Subjects>>

}