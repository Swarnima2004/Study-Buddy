package com.example.studybuddy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.studybuddy.domain.model.Subjects
import kotlinx.coroutines.flow.Flow


@Dao
interface SubjectDAO {

    @Upsert
    suspend fun upsertSubject(subject : Subjects)

    @Query("SELECT COUNT(*) FROM Subjects")
    fun getTotalSubjectCount(): Flow<Int>


    @Query("SELECT SUM(goalHours) FROM Subjects")
    fun getTotalGoalHours() : Flow<Float>


    @Query("SELECT * FROM Subjects WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId : Int) : Subjects?


    @Query("DELETE FROM Subjects WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId : Int)

    @Query("SELECT * FROM Subjects")
    fun getAllSubjects(): Flow<List<Subjects>>


}