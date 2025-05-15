package com.example.studybuddy

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.example.studybuddy.domain.model.Subjects
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.session
import com.example.studybuddy.presentation.NavGraphs
import com.example.studybuddy.presentation.destinations.SessionScreenRouterDestination
import com.example.studybuddy.presentation.sessionScreen.SessionScreenRouter
import com.example.studybuddy.presentation.sessionScreen.StudyTimerService
import com.example.studybuddy.presentation.theme.StudyBuddyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency


import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudyTimerService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            p0: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as StudyTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudyTimerService::class.java).also { intent ->
            bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           if(isBound){
               StudyBuddyTheme {
                   DestinationsNavHost(navGraph = NavGraphs.root,
                       dependenciesContainerBuilder = {
                           dependency(SessionScreenRouterDestination){timerService}
                       })

               }
           }
        }
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0

            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

