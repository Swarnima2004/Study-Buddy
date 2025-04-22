package com.example.studybuddy.domain.model

data class session(
    val sessionSubjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration : Long,
    val sessionId: Int
)
