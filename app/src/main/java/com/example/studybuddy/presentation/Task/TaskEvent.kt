package com.example.studybuddy.presentation.Task

import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.util.Priority

sealed class TaskEvent {

    data class OnTaskTitleChange(val title: String) : TaskEvent()

    data class OnTaskDescriptionChange(val description: String) : TaskEvent()

    data class OnDueDateChange(val millis: Long?) : TaskEvent()

    data class OnPriorityChange(val priority: Priority) : TaskEvent()

    data class OnRelatedToSubjectSelect(val subject: Subjects) : TaskEvent()

    data object OnIsCompletedChange : TaskEvent()

    data object OnSaveTask : TaskEvent()

    data object OnDeleteTask : TaskEvent()

}