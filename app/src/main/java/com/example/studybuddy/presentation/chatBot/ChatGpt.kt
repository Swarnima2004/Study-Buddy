package com.example.studybuddy.presentation.chatBot

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//navigation


@Destination
@Composable
fun ChatGptRouter(navigator: DestinationsNavigator) {
    val viewModel: ChatbotViewModel = hiltViewModel()
    Chat(navigator, viewModel)
}
@Composable
fun Chat(navigator: DestinationsNavigator, viewModel: ChatbotViewModel) {
    val reply by viewModel.response.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        AppHeader(navigator = navigator)

        Text(
            text = reply,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        MessageInput(onMessageSent = {userMessage ->
            viewModel.sendMessage(userMessage)
        })
    }
}

@Composable
fun MessageInput(onMessageSent: (String)-> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message ,
            onValueChange = { message =it
            },
            label = { Text("Type your message...") }
        )
        IconButton(onClick = {
            if (message.isNotBlank()) {
                onMessageSent(message)
                message = ""
            }
        }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription ="Send")
        }
    }
}

@Composable
fun AppHeader(navigator: DestinationsNavigator) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan)
    ) {
        Text(
            modifier = Modifier.padding(top = 26.dp, bottom=16.dp),

            text = "StudyBuddy ",

            fontSize = 24.sp

        )

    }
}