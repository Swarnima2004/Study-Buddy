package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.presentation.dashboard.DashboardScreen
import com.example.studybuddy.presentation.subjects.SubjectScreen
import com.example.studybuddy.presentation.theme.StudyBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyBuddyTheme {
                SubjectScreen()

            }
        }
    }
}

val subjectList = listOf(
    Subjects(
        name = "Maths",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[0],
        subjectId = 0
    ),
    Subjects(
        name = "DSA",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[1],
        subjectId = 0
    ),
    Subjects(
        name = "Development",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[2],
        subjectId = 0
    ),
    Subjects(
        name = "Physics",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[3],
        subjectId = 0
    ),
    Subjects(
        name = "Chemistry",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[4],
        subjectId = 0
    ),
    Subjects(
        name = "English",
        goalHours = 10f,
        colors = Subjects.subjectCardColors[0],
        subjectId = 0
    )
)

val sessions = listOf(
    session(
        sessionSubjectId = 0,
        relatedToSubject = "Maths",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    session(
        sessionSubjectId = 0,
        relatedToSubject = "dsa",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    session(
        sessionSubjectId = 0,
        relatedToSubject = "hindi",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    session(
        sessionSubjectId = 0,
        relatedToSubject = "physics",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    session(
        sessionSubjectId = 0,
        relatedToSubject = "chemistry",
        date = 0L,
        duration = 2,
        sessionId = 0
    )
)

val tasks = listOf(
    Task(
        title = "Prepare for exam",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "Maths",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare for dsa",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Maths",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare for development",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "Maths",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare for school",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "Maths",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare for coaching",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "Maths",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare for cn",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Maths",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    )
)