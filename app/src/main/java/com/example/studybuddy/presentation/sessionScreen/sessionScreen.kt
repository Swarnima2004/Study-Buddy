package com.example.studybuddy.presentation.sessionScreen


import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.presentation.components.DeleteDialog
import com.example.studybuddy.presentation.components.StudySessionList
import com.example.studybuddy.presentation.components.SubjectListBottomSheet
import com.example.studybuddy.presentation.theme.Red
import com.example.studybuddy.sessions
import com.example.studybuddy.subjectList
import com.example.studybuddy.util.Constants.ACTION_SERVICE_CANCEL
import com.example.studybuddy.util.Constants.ACTION_SERVICE_START
import com.example.studybuddy.util.Constants.ACTION_SERVICE_STOP
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "study_buddy://dashboard/sessions"
        )
    ]
)
@Composable
fun SessionScreenRouter(
    navigator: DestinationsNavigator,
    timerService: StudyTimerService
) {
    val viewModel: sessionScreenViewModel = hiltViewModel()
    sessionScreen(
        onBackButtonClick = {
            navigator.navigateUp()
        },
        timerService = timerService
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

private fun sessionScreen(
    onBackButtonClick: () -> Unit,
    timerService: StudyTimerService
) {
    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val currentTimerState by timerService.currentTimerState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var isdeleteDialogOpen by remember { mutableStateOf(false) }


    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = subjectList,
        onSubjectClicked = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        },
        onDismissRequest = {
            isBottomSheetOpen = false
        }
    )


    DeleteDialog(
        isOpen = isdeleteDialogOpen,
        title = "Delete Session ?",
        bodyText = "Are you sure.you want to delete this task?" +
                "This action cannot be undone . ",
        onDismissRequest = { isdeleteDialogOpen = false },
        onConfirmButtonClick = {
            isdeleteDialogOpen = false
        }
    )


    Scaffold(
        topBar = { SessionScreenTopBar(onBackButtonClick = onBackButtonClick) }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue)
        ) {
            item {
                Timer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }
            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    relatedToSubject = "Hindi",
                    selectSubjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                ButtonSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startSessionButtonClick = {
                        ServiceAssist.triggerForegroundService(
                            context = context,
                            action = if (currentTimerState == TimerState.STARTED) {
                                ACTION_SERVICE_STOP
                            } else {
                                ACTION_SERVICE_START
                            }
                        )
                    },
                    cancelButtonClick = {
                        ServiceAssist.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    },
                    finishButtonClick = {},
                    timerState = currentTimerState,
                    seconds = seconds
                )
            }
            StudySessionList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                emptyListText = "You don't have any recent session. \n" +
                        "Start session to track of your session.",
                Sessions = sessions,
                onDeleteIconClick = { isdeleteDialogOpen = true }

            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(text = "Study Sessions", style = MaterialTheme.typography.headlineSmall)
        }
    )
}

@Composable
private fun Timer(
    modifier: Modifier,
    hours: String,
    minutes: String,
    seconds: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )
        Row {
            AnimatedContent(
                targetState = hours,
                label = hours,
                transitionSpec = { timerTextAnimation() })
            { hours ->
                Text(
                    text = "$hours:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = minutes,
                label = minutes,
                transitionSpec = { timerTextAnimation() }) { minutes ->
                Text(
                    text = "$minutes:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = seconds,
                label = seconds,
                transitionSpec = { timerTextAnimation() }) { seconds ->
                Text(
                    text = seconds,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
        }
    }
}

@Composable
private fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Related To Subject",
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = selectSubjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }

}

@Composable
private fun ButtonSection(
    modifier: Modifier,
    startSessionButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String

) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Button(
            onClick = cancelButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel"

            )
        }
        Button(
            onClick = startSessionButtonClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = if (timerState == TimerState.STARTED) Red
                else MaterialTheme.colorScheme.primary,
                containerColor = Color.White
            )
        )
        {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = when (timerState) {
                    TimerState.STARTED -> "Stop"
                    TimerState.STOPPED -> "Resume"
                    else -> "Start"
                }
            )
        }
        Button(
            onClick = finishButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}

private fun timerTextAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight } + fadeIn(
        animationSpec = tween(duration)
    ) togetherWith slideOutVertically(animationSpec = tween(duration)) { fullHeight -> -fullHeight } + fadeOut(
        animationSpec = tween(duration)
    )
}