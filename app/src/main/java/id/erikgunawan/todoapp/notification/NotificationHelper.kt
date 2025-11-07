package id.erikgunawan.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.data.Task
import id.erikgunawan.todoapp.utils.NOTIFICATION_CHANNEL_ID

object NotificationHelper {

    fun createNotificationChannel(context: Context, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.pref_notify_summary)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(
        context: Context,
        task: Task,
        pendingIntent: android.app.PendingIntent
    ): android.app.Notification {
        val contentText = context.getString(
            R.string.notify_content,
            id.erikgunawan.todoapp.utils.DateConverter.convertMillisToString(task.dueDateMillis)
        )

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(task.title)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }
}

