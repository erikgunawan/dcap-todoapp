package id.erikgunawan.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface TaskDao {

    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): PagingSource<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY dueDate ASC LIMIT 1")
    suspend fun getNearestActiveTask(): Task?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg tasks: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    suspend fun deleteTask(task: Task) {
        deleteTask(task.id)
    }

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTasksCount(): Int
    
}
