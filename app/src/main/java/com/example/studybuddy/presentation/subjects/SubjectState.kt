package com.example.studybuddy.presentation.subjects

import androidx.compose.ui.graphics.Color
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session

data class SubjectState(
    val currentSubjectId :Int?= null,
    val subjectName :String = "",
    val goalStudyHours :String = "",
    val studiedHours :Float =0f,
    val subjectCardColor : List<Color> = Subjects.subjectCardColors.random(),
    val recentSessions : List<session> = emptyList(),
    val upcomingTasks : List<Task> = emptyList(),
    val completedTasks : List<Task> = emptyList(),
    val session : session? = null,
    val progress : Float = 0f

)
