package com.example.studybuddy.presentation.subjects

import androidx.compose.ui.graphics.Color
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session


sealed class SubjectEvent{

    data object UpdateSubject : SubjectEvent()

    data object DeleteSubject : SubjectEvent()

    data object DeleteSession : SubjectEvent()

    data object updateProgress : SubjectEvent()

    data class OnTaskIsCompleteChange(val task : Task):SubjectEvent()

    data class OnSubjectCardColorChange(val color : List<Color>) : SubjectEvent()

    data class OnSubjectNameChange(val subjectName : String) : SubjectEvent()

    data class OnGoalStudyHoursChange(val hours : String) : SubjectEvent()

    data class OnDeleteSessionButtonClick(val session: session) : SubjectEvent()
}
