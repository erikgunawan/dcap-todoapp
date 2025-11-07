package id.erikgunawan.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.erikgunawan.todoapp.utils.FilterUtils
import id.erikgunawan.todoapp.utils.TasksFilterType

class TaskRepository(private val tasksDao: TaskDao) {

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDatabase.getInstance(context)
                    instance = TaskRepository(database.taskDao())
                }
                return instance as TaskRepository
            }

        }
    }

    //TODO 4 : Use FilterUtils.getFilteredQuery to create filterable query
    //TODO 5 : Build PagingData with configuration
    fun getTasks(filter: TasksFilterType): LiveData<PagingData<Task>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = PLACEHOLDERS
            ),
            pagingSourceFactory = {
                val query = FilterUtils.getFilteredQuery(filter)
                tasksDao.getTasks(query)
            }
        ).liveData
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return tasksDao.getTaskById(taskId)
    }

    suspend fun getNearestActiveTask(): Task? {
        return tasksDao.getNearestActiveTask()
    }

    suspend fun insertTask(newTask: Task): Long{
        return tasksDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }

    suspend fun completeTask(task: Task, isCompleted: Boolean) {
        tasksDao.updateCompleted(task.id, isCompleted)
    }
}