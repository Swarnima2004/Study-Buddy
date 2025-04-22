package com.example.studybuddy.presentation.dashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studybuddy.R
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.presentation.components.CountCard
import com.example.studybuddy.presentation.components.StudySessionList
import com.example.studybuddy.presentation.components.SubjectCard
import com.example.studybuddy.presentation.components.tasksList


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreen() {

    val subjectList = listOf(
        Subjects(name = "Maths", goalHours = 10f, colors = Subjects.subjectCardColors[0], subjectId = 0),
        Subjects(name = "DSA", goalHours = 10f, colors = Subjects.subjectCardColors[1], subjectId = 0),
        Subjects(name = "Development", goalHours = 10f, colors = Subjects.subjectCardColors[2], subjectId = 0),
        Subjects(name = "Physics", goalHours = 10f, colors = Subjects.subjectCardColors[3], subjectId = 0),
        Subjects(name = "Chemistry", goalHours = 10f, colors = Subjects.subjectCardColors[4], subjectId = 0),
        Subjects(name = "English", goalHours = 10f, colors = Subjects.subjectCardColors[0], subjectId = 0)
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

    Scaffold(
        topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 5,
                    studiedHours = "10",
                    goalStudyHours = "12"
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjectList
                )
            }
            item {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Studying")
                }
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks. \n" + "Click the + in the subject screen to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item{
                Spacer(modifier = Modifier.height(20.dp))
            }

          StudySessionList(
              sectionTitle =  "STUDY SESSIONS",
              emptyListText = "You don't have any recent session. \n" +
                      "Start session to track of your session.",
              Sessions = sessions,
              onDeleteIconClick = {}

          )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Study Buddy",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalStudyHours: String
) {
    Row(
        modifier = modifier
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"

        )
        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalStudyHours
        )
    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subjects>,
    emptyListText: String = "You don't have any subjects.\n Click on + to add new subject."
) {
    Column(
        modifier = modifier


    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center

            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors,
                    onClick = { }
                )
            }
        }
    }
}


