package com.example.studybuddy.presentation.subjects

import androidx.lifecycle.ViewModel
import com.example.studybuddy.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
): ViewModel() {
}