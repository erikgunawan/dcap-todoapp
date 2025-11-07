package id.erikgunawan.todoapp.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.erikgunawan.todoapp.data.Task
import id.erikgunawan.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun insertTask(title: String, description: String, dueDateMillis: Long) {
        viewModelScope.launch {
            val newTask = Task(
                title = title,
                description = description,
                dueDateMillis = dueDateMillis,
                isCompleted = false
            )
            taskRepository.insertTask(newTask)
        }
    }
}


