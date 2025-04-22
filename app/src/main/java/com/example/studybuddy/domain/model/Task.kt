package com.example.studybuddy.domain.model

data class Task(
    val title : String,
    val description : String,
    val dueDate : Long,
    val priority : Int,
    val relatedToSubject : String,
    val isCompleted : Boolean,
    val taskSubjectId : Int,
    val taskId : Int

)
