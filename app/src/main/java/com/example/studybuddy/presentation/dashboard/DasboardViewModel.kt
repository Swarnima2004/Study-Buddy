package com.example.studybuddy.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.studybuddy.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DasboardViewModel@Inject constructor(
    private val subjectRepository: SubjectRepository
): ViewModel() {



}