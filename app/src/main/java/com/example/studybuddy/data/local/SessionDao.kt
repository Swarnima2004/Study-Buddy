package com.example.studybuddy.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.studybuddy.domain.model.session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: session)

    @Delete
    suspend fun deleteSession(session: session)

    @Query("SELECT * FROM session")
    fun getAllSessions(): Flow<List<session>>


    @Query("SELECT * FROM session WHERE sessionSubjectId = :subjectId")
    fun getRecentSessionForSubject(subjectId : Int) :Flow<List<session>>


    @Query("SELECT SUM(duration) FROM session")
     fun getTotalSessionDuration(): Flow<Long>


     @Query("SELECT SUM(duration) FROM session WHERE sessionSubjectId = :subjectId")
    fun getTotalSessionDurationBySubjectId(subjectId : Int) : Flow<Long>


    @Query("DELETE FROM session WHERE sessionSubjectId = :subjectId")
    fun deleteSessionBySubjectId(subjectId : Int)

}