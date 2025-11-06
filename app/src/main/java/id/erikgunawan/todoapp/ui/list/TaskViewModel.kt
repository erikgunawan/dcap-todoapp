package id.erikgunawan.todoapp.ui.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.data.Task
import id.erikgunawan.todoapp.data.TaskRepository
import id.erikgunawan.todoapp.utils.Event
import id.erikgunawan.todoapp.utils.TasksFilterType
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _filter = MutableLiveData<TasksFilterType>()

    val tasks: LiveData<PagingData<Task>> = _filter.switchMap {
        taskRepository.getTasks(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    init {
        _filter.value = TasksFilterType.ALL_TASKS
    }

    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) {
            _snackbarText.value = Event(R.string.task_marked_complete)
        } else {
            _snackbarText.value = Event(R.string.task_marked_active)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}