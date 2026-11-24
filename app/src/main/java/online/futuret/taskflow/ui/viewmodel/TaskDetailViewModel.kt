package online.futuret.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.futuret.taskflow.data.mapper.toTask
import online.futuret.taskflow.data.repository.TaskRepository
import online.futuret.taskflow.model.Task

class TaskDetailViewModel(
    private val repository: TaskRepository,
    taskId: Int
): ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()

    init {
        viewModelScope.launch {
            _task.value =  repository.getTaskById(taskId)?.toTask()
        }
    }

    class Factory(
        private val repository: TaskRepository,
        private val taskId: Int
    ): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskDetailViewModel(repository,taskId) as T
    }
    }
}















