package com.example.studybuddy.presentation.Task

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.repository.SubjectRepository
import com.example.studybuddy.domain.repository.TaskRepository
import com.example.studybuddy.presentation.navArgs
import com.example.studybuddy.util.Priority
import com.example.studybuddy.util.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val navArgs: TaskScreenNavArg = savedStateHandle.navArgs()
    private val _state = MutableStateFlow(TaskState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects()
    ) { state, subjects ->
        state.copy(
            subjects = subjects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TaskState()
    )

    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    init {
        fetchTask()
        fetchSubjects()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnTaskTitleChange -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }

            is TaskEvent.OnTaskDescriptionChange -> {
                _state.update {
                    it.copy(description = event.description)
                }
            }

            is TaskEvent.OnDueDateChange -> {
                _state.update {
                    it.copy(dueDate = event.millis)
                }
            }

            is TaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }

            TaskEvent.OnIsCompletedChange -> {
                _state.update {
                    it.copy(isTaskCompleted = !_state.value.isTaskCompleted)
                }
            }

            is TaskEvent.OnRelatedToSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            TaskEvent.OnSaveTask -> SaveTask()


            TaskEvent.OnDeleteTask -> deleteTask()
        }

    }

    private fun deleteTask() {
        viewModelScope.launch {

            try{
                val currentTaskId = state.value.currentTaskId
                if(currentTaskId != null){
                    withContext(Dispatchers.IO){

                        taskRepository.deleteTask(taskId = currentTaskId)
                    }
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker("Task Deleted Successfully")
                    )
                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
                }else{
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker("No Task to delete" )
                    )
                }


            }catch (e:Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Couldn't delete the task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun SaveTask() {
        viewModelScope.launch {
            val state = _state.value
            if (state.subjectId == null || state.relatedToSubject == null) {

                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Please select subject related to the task",
                        SnackbarDuration.Long
                    )
                )

                return@launch
            }
            try {
                taskRepository.upsertTask(
                    task = Task(
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate ?: Instant.now().toEpochMilli(),
                        relatedToSubject = state.relatedToSubject,
                        priority = state.priority.value,
                        isCompleted = state.isTaskCompleted,
                        taskSubjectId = state.subjectId,
                        taskId = state.currentTaskId
                    )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Task Added Successfully",
                        SnackbarDuration.Long
                    )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.NavigateUp
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Couldn't save the task. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            navArgs.taskId?.let { id ->
                taskRepository.getTaskById(id)?.let { task ->
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            dueDate = task.dueDate,
                            isTaskCompleted = task.isCompleted,
                            relatedToSubject = task.relatedToSubject,
                            priority = Priority.fromInt(task.priority),
                            subjectId = task.taskSubjectId,
                            currentTaskId = task.taskId
                        )
                    }
                }
            }
        }
    }

    private fun fetchSubjects() {
        viewModelScope.launch {
            navArgs.subjectId?.let{id->
                subjectRepository.getSubjectById(id)?.let {subject->
                    _state.update{
                        it.copy(
                            relatedToSubject = subject.name,
                            subjectId = subject.subjectId
                        )

                    }
            }
        }
    }
}}