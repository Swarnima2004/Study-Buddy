package com.example.studybuddy.presentation.dashboard

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.domain.repository.SessionRepository
import com.example.studybuddy.domain.repository.SubjectRepository
import com.example.studybuddy.domain.repository.TaskRepository
import com.example.studybuddy.util.SnackbarEvent
import com.example.studybuddy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DasboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine (
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionDuration()
    ){
        state, subjectCount , goalHours , subjects ,totalsessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalsessionDuration.toHours()

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    val tasks : StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentSession : StateFlow<List<session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()


    fun onEvent(event: DashboardEvent){
        when(event){
            DashboardEvent.SaveSubject -> saveSubject()
            DashboardEvent.deleteSession -> deleteSession()

            is DashboardEvent.onDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is DashboardEvent.onGoalHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }
            is DashboardEvent.onSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColor = event.colors)
                }

            }
            is DashboardEvent.onSubjectNameChange ->{
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }
            is DashboardEvent.onTaskIsCompleteChange -> {
                updateTask(event.task)
            }
        }

    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try{
                taskRepository.upsertTask(
                    task = task.copy(
                        isCompleted = !task.isCompleted
                    )

                )

                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        " Saved in completed tasks"
                    )
                )
            }
            catch (e : Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Couldn't update. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    sessionRepository.deleteSession(it)
                }
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Session deleted successfully",
                        duration = SnackbarDuration.Long
                    )
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Couldn't  delete. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }

    }


    private fun saveSubject() {
        viewModelScope.launch {
            try{
                subjectRepository.upsertSubject(
                    subject = Subjects(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull()?: 1f,
                        colors = state.value.subjectCardColor.map{it.toArgb()}
                    )
                )
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColor = Subjects.subjectCardColors.random()
                    )
                }
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        "Subject Saved Successfully"
                    )
                )
            }
            catch (e : Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                       "Couldn't save the subject. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }
}