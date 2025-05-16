package com.example.studybuddy.presentation.sessionScreen

import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.session

data class SessionState(
    val subjects : List<Subjects> = emptyList(),
    val sessions : List<session> = emptyList(),
    val relatedToSubject : String? = null,
    val subjectId : Int? = null,
    val session: session? = null
    )
