package com.example.studybuddy.presentation.sessionScreen

import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.session

sealed class SessionEvent {

    data class onRelatedSubjectChange(val subject : Subjects) : SessionEvent()

    data class SaveSession(val duration : Long) : SessionEvent()

    data class onDeleteSessionButtonClick(val session: session) : SessionEvent()

    data object deleteSession : SessionEvent()

    data object NotifyToUpdateSubject : SessionEvent()

    data class UpdateSubjectIdAndRelatedSubject(val subjectId : Int?, val relatedToSubject : String?) : SessionEvent()

}