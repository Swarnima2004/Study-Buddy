package com.example.studybuddy.presentation.sessionScreen

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.studybuddy.MainActivity
import com.example.studybuddy.util.Constants.CLICK_REQUEST_CODE

object ServiceAssist {

    fun clickPendingIntent(context: Context): PendingIntent{
        // we are creating the link to open the particular screen
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "study_buddy://dashboard/sessions".toUri(),
            context,
            MainActivity::class.java
        )
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                CLICK_REQUEST_CODE,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StudyTimerService::class.java).apply{
            this.action = action
            context.startService(this)
        }
    }
}