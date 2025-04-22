package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studybuddy.presentation.dashboard.DashboardScreen
import com.example.studybuddy.presentation.theme.StudyBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyBuddyTheme {
                DashboardScreen()

            }
        }
    }
}