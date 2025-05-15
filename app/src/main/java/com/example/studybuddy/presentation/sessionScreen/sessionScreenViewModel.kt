package com.example.studybuddy.presentation.sessionScreen


import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.domain.repository.SessionRepository
import com.example.studybuddy.domain.repository.SubjectRepository
import com.example.studybuddy.util.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class sessionScreenViewModel @Inject constructor(
    subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository

) : ViewModel() {
    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects(),
        sessionRepository.getAllSessions()
    ) { state, subjects, sessions ->
        state.copy(
            subjects = subjects,
            sessions = sessions
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionState()
    )

    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()


    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectIdAndRelatedSubject -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.relatedToSubject,
                        subjectId = event.subjectId
                    )
                }
            }
            SessionEvent.NotifyToUpdateSubject -> notifyToUpdateSubject()
            SessionEvent.deleteSession -> deleteSession()
            is SessionEvent.onDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SessionEvent.onRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }
        }
    }



    private fun notifyToUpdateSubject() {
        viewModelScope.launch {
            if(state.value.relatedToSubject == null || state.value.subjectId == null){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Please select subject related to the session ",

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

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < 36) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Session should be at least 36 seconds",
                        duration = SnackbarDuration.Long
                    )
                )
                return@launch
            }
            try {
                sessionRepository.insertSession(
                    session = session(
                        sessionSubjectId = state.value.subjectId ?: -1,
                        relatedToSubject = state.value.relatedToSubject ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration
                    )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Session saved successfully",
                        duration = SnackbarDuration.Long
                    )
                )

            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnacker(
                        message = "Couldn't update. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }

    }
}