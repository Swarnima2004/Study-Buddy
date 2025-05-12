package com.example.studybuddy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session

sealed class DashboardEvent {

    data object SaveSubject : DashboardEvent()

    data object deleteSession : DashboardEvent()

//if the value depends on the user section, then use class
    data class onDeleteSessionButtonClick(val session: session) : DashboardEvent()

    data class onTaskIsCompleteChange(val task: Task) : DashboardEvent()

    data class onSubjectCardColorChange(val colors : List<Color>) : DashboardEvent()

    data class onSubjectNameChange(val name: String) : DashboardEvent()

    data class onGoalHoursChange(val hours: String) : DashboardEvent()

}