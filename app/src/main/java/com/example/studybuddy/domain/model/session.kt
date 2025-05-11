package com.example.studybuddy.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class session(
    val sessionSubjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration : Long,
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? =null
)
