package com.example.studybuddy.domain.model

import androidx.compose.ui.graphics.Color
import com.example.studybuddy.ui.theme.gradient1
import com.example.studybuddy.ui.theme.gradient2
import com.example.studybuddy.ui.theme.gradient3
import com.example.studybuddy.ui.theme.gradient4
import com.example.studybuddy.ui.theme.gradient5

data class Subjects(
    val name: String,
    val goalHours : Float,
    val colors : List<Color>
){
    companion object{
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
