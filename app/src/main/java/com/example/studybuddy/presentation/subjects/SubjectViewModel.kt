package com.example.studybuddy.presentation.subjects

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.repository.SessionRepository
import com.example.studybuddy.domain.repository.SubjectRepository
import com.example.studybuddy.domain.repository.TaskRepository
import com.example.studybuddy.presentation.navArgs
import com.example.studybuddy.util.SnackbarEvent
import com.example.studybuddy.util.toHours
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
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository,
    //this class will help us in getting the navigation arguments
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    private val navArgs: SubjectScreenNavArgs = savedStateHandle.navArgs()


    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        taskRepository.getCompletedTasksForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionDurationBySubject(navArgs.subjectId)
    ) { state, upcomingTasks, completedTask, recentSessions, totalSessionDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTask,
            recentSessions = recentSessions,
            studiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )


    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColor = event.color
                    )
                }
            }

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.subjectName)
                }
            }

            is SubjectEvent.OnDeleteSessionButtonClick -> {}
            is SubjectEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            SubjectEvent.DeleteSession -> {}
            SubjectEvent.DeleteSubject -> deleteSubject()
            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.updateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)

                    )
                }
            }
        }
    }

    private fun updateSubject() {

        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subjects(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColor.map { it.toArgb() }
                    )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker("Subject Updated Successfully")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Couldn't update the subject. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository
                .getSubjectById(navArgs.subjectId)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectName = subject.name,
                            goalStudyHours = subject.goalHours.toString(),
                            subjectCardColor = subject.colors.map { Color(it) },
                            currentSubjectId = subject.subjectId
                        )
                    }
                }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {

            try {
                val currentSubjetcId = state.value.currentSubjectId
                if (currentSubjetcId != null) {
                    withContext(Dispatchers.IO) {

                        subjectRepository.deleteSubject(subjectId = currentSubjetcId)
                    }
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker("Subject Deleted Successfully")
                    )
                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
                } else {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker("No Subject to delete")
                    )
                }


            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Couldn't delete the subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(
                        isCompleted = !task.isCompleted
                    )

                )
                if (task.isCompleted) {

                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker(
                            " Saved in upcoming tasks"
                        )
                    )
                } else {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnacker(
                            "Saved in completed tasks"
                        )
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Couldn't update. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }
}