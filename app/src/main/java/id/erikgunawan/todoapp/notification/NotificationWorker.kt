package id.erikgunawan.todoapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import id.erikgunawan.todoapp.data.TaskRepository
import id.erikgunawan.todoapp.ui.detail.DetailTaskActivity
import id.erikgunawan.todoapp.utils.NOTIFICATION_CHANNEL_ID
import id.erikgunawan.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID) ?: "Task Reminder"

    private fun getPendingIntent(task: id.erikgunawan.todoapp.data.Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(task.id, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(task.id, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        return try {
            NotificationHelper.createNotificationChannel(applicationContext, channelName)
            val repository = TaskRepository.getInstance(applicationContext)
            val nearestTask = kotlinx.coroutines.runBlocking {
                repository.getNearestActiveTask()
            }
            
            nearestTask?.let { task ->
                val pendingIntent = getPendingIntent(task)
                pendingIntent?.let {
                    val notification = NotificationHelper.createNotification(
                        applicationContext,
                        task,
                        it
                    )
                    
                    val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(task.id, notification)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}
