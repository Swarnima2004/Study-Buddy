package com.example.studybuddy.presentation.Task

import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.util.Priority

data class TaskState(
    val title :String = "",
    val description :String = "",
    val dueDate : Long? = null,
    val isTaskCompleted : Boolean = false,
    val priority: Priority = Priority.LOW,
    val relatedToSubject : String? = null,
    val subjects : List<Subjects> = emptyList(),
    val subjectId : Int? = null,
    val currentTaskId : Int? = null
)
