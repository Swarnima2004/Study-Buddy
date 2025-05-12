package com.example.studybuddy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.session

data class DashboardState (
    val totalSubjectCount : Int =0 ,
    val totalStudiedHours : Float =0f ,
    val totalGoalStudyHours: Float =0f,
    val subjects : List<Subjects> = emptyList(),
    val subjectName : String = "" ,
    val goalStudyHours : String= "" ,
    val subjectCardColor: List<Color> = Subjects.subjectCardColors.random(),
    val session : session? =  null
)