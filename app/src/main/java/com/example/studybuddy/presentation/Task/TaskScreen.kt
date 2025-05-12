package com.example.studybuddy.presentation.Task


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studybuddy.presentation.components.DeleteDialog
import com.example.studybuddy.presentation.components.SubjectListBottomSheet
import com.example.studybuddy.presentation.components.TaskCheckBox
import com.example.studybuddy.presentation.components.TaskDatePicker
import com.example.studybuddy.util.Priority
import com.example.studybuddy.util.SnackbarEvent
import com.example.studybuddy.util.changeMillisToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

data class TaskScreenNavArg(
    val taskId: Int?,
    val subjectId :Int?
)

@Destination(navArgsDelegate = TaskScreenNavArg::class)
@Composable
fun TaskScreenRouter(
    navigator: DestinationsNavigator
) {

    val viewModel: TaskViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    TaskScreen(
        state = state,
        snackbarEvent = viewModel.snackbarEventFlow,
        onEvent = viewModel::onEvent,
        onBackbuttomClick = {
            navigator.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun TaskScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onBackbuttomClick: () -> Unit
) {
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()

    )
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var isdeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    titleError = when {
        state.title.isBlank() -> "Title cannot be empty"
        state.title.length > 30 -> "Title cannot exceed 20 characters"
        state.title.length < 3 -> "Title cannot be less than 3 characters"
        else -> null
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(key1 = true ) {
        snackbarEvent.collectLatest {
                event ->
            when(event){
                is SnackbarEvent.ShowSnacker -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )

                }

                SnackbarEvent.NavigateUp -> { onBackbuttomClick() }
            }
        }
    }


    DeleteDialog(
        isOpen =isdeleteDialogOpen ,
        title = "Delete Task ?",
        bodyText = "Are you sure.you want to delete this task?"+
        "This action cannot be undone . ",
        onDismissRequest = { isdeleteDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(TaskEvent.OnDeleteTask)
            isdeleteDialogOpen = false
        }
    )

    TaskDatePicker(
        isOpen= isDatePickerDialogOpen,
        onDismissRequest = {isDatePickerDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(TaskEvent.OnDueDateChange(millis = datePickerState.selectedDateMillis))
            isDatePickerDialogOpen = false
        }
    )
    SubjectListBottomSheet(
        sheetState = sheetState ,
        isOpen = isBottomSheetOpen,
        subjects = state.subjects,
        onSubjectClicked = {subject ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
            onEvent(TaskEvent.OnRelatedToSubjectSelect(subject))
        },
        onDismissRequest = {
            isBottomSheetOpen = false
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TaskScreenTopBar(
                isTaskExist = state.currentTaskId != null,
                isCompleted = state.isTaskCompleted,
                checkBoxBorderColor = state.priority.color,
                onBackbuttoClick = onBackbuttomClick,
                onDeleteButtonClick = { isdeleteDialogOpen = true },
                onCheckBoxClick = {
                    onEvent(TaskEvent.OnIsCompletedChange)
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .padding(paddingValue)
                .padding(horizontal = 14.dp)

        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { onEvent(TaskEvent.OnTaskTitleChange(it)) },
                label = { Text(text = "Title") },
                singleLine = true,
                isError = titleError != null && state.title.isNotBlank(),
                supportingText = { Text(text = titleError.orEmpty()) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { onEvent(TaskEvent.OnTaskDescriptionChange(it))},
                label = { Text(text = "Description") }

            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Due Date", style = MaterialTheme.typography.bodySmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isDatePickerDialogOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Due Date"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Priority",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority == state.priority)
                            Color.White
                        else Color.Transparent,
                        labelColor = if (priority == state.priority) {
                            Color.White
                        } else Color.White.copy(alpha = 0.7f),
                        onClick = {
                            onEvent(TaskEvent.OnPriorityChange(priority))
                        }

                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Related To Subject", style = MaterialTheme.typography.bodySmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val firstSubject = state.subjects.firstOrNull()?.name ?: " "
                Text(
                    text = state.relatedToSubject?: firstSubject,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = {isBottomSheetOpen = true}) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Subject"
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 22.dp),
                enabled = titleError == null,
                onClick = {onEvent(TaskEvent.OnSaveTask) }
            ){
              Text(text = "Save")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExist: Boolean,
    isCompleted: Boolean,
    checkBoxBorderColor: Color,
    onBackbuttoClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckBoxClick: () -> Unit,

    ) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackbuttoClick) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow"
                )
            }
        },
        title = { Text(text = "Task", style = MaterialTheme.typography.headlineSmall) },
        actions = {
            if (isTaskExist) {
                TaskCheckBox(
                    isCompleted = isCompleted,
                    borderColor = checkBoxBorderColor,
                    onCheckBoxClick = onCheckBoxClick

                )
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon"
                    )
                }
            }
        }
    )

}

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit,

    ) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = labelColor)
    }

}