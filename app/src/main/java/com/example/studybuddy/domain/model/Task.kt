package com.example.studybuddy.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title : String,
    val description : String,
    val dueDate : Long,
    val priority : Int,
    val relatedToSubject : String,
    val isCompleted : Boolean,
    val taskSubjectId : Int,
    @PrimaryKey(autoGenerate = true)
    val taskId : Int? =null

)