package com.example.studybuddy.presentation.subjects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.presentation.Task.TaskScreenNavArg
import com.example.studybuddy.presentation.components.AddSubjectDialog
import com.example.studybuddy.presentation.components.CountCard
import com.example.studybuddy.presentation.components.DeleteDialog
import com.example.studybuddy.presentation.components.StudySessionList
import com.example.studybuddy.presentation.components.tasksList
import com.example.studybuddy.presentation.destinations.TaskScreenRouterDestination
import com.example.studybuddy.sessions
import com.example.studybuddy.tasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class SubjectScreenNavArgs(
    val subjectId : Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRouter(
    navigator: DestinationsNavigator
) {

    val viewModel : SubjectViewModel = hiltViewModel()

    SubjectScreen(
        onBackButtonCLick = {
          navigator.navigateUp()
        },
        onAddTaskButtonClick = {
            val navArg = TaskScreenNavArg(taskId = null ,subjectId = -1)
            navigator.navigate(TaskScreenRouterDestination(navArgs = navArg))
        },
        onTaskCardClick = {taskId->
            val navArg = TaskScreenNavArg(taskId = taskId ,subjectId = null)
            navigator.navigate(TaskScreenRouterDestination(navArgs = navArg))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    onBackButtonCLick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }

    //input from the user
    var subjectName by remember { mutableStateOf("") }
    var goalHours by remember{ mutableStateOf("") }
    var selectedColor by remember{ mutableStateOf(Subjects.subjectCardColors.random()) }

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        subjectName = subjectName ,
        goalHours = goalHours,
        onSubjectNameChange ={subjectName = it},
        onGoalHoursChange ={goalHours = it},
        selectedColor = selectedColor,
        onColorChange ={selectedColor =it},
        onDismissRequest = { isEditSubjectDialogOpen = false },
        onConfirmButtonClick = {
            isEditSubjectDialogOpen = false
        }

    )
    //delete subject box

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session",
        bodyText = "Are you sure you want to delete this session? Your study hours will be reduced"
                +"The action can nat be undo",
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {isDeleteSessionDialogOpen = false }
    )
    //delete subject box

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure you want to delete the subject ? All the related study sessions will be deleted"
                +"The action can not be undone",
        onDismissRequest = {isDeleteSubjectDialogOpen = false},
        onConfirmButtonClick = {isDeleteSubjectDialogOpen= false }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            SubjectScreenTopBar(
                title = "English",
                onBackButtonCLick = onBackButtonCLick,
                onDeleteButtonClick = {isDeleteSubjectDialogOpen = true},
                onEditButtonClick = {isEditSubjectDialogOpen = true},
                scrollbehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                text = { Text(text = "Add Task") },
                expanded = isFabExpanded
            )
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)

        ) {
            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = "100",
                    goalHours = "20",
                    progress = 0.5f
                )
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks. \n" + "Click the + in the subject screen to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            tasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyListText = "You don't have any completed tasks. \n" + "Click the + in the subject screen to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            StudySessionList(
                sectionTitle = "STUDY SESSIONS",
                emptyListText = "You don't have any recent session. \n" +
                        "Start session to track of your session.",
                Sessions = sessions,
                onDeleteIconClick = {isDeleteSessionDialogOpen =true}

            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title: String,
    onBackButtonCLick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    scrollbehavior : TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollbehavior ,
        navigationIcon = {
            IconButton(onClick = onBackButtonCLick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigation Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall

            )

        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subjects"
                )
            }
            IconButton(onClick = onEditButtonClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit subjects"
                )
            }
        }
    )

}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float
) {
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = " Study Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = { 1f },
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = { progress },

                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")

        }
    }
}