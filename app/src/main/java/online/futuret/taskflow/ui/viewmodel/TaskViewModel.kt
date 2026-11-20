package online.futuret.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import online.futuret.taskflow.data.local.entity.TaskEntity
import online.futuret.taskflow.data.repository.TaskRepository
import online.futuret.taskflow.model.Task

class TaskViewModel(
    private val repository: TaskRepository
): ViewModel() {
    val tasks = repository.getAllTasks()
        .map { entities -> entities.map { it.toTask() } }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5000)
        )
    fun addTask(title: String){
        viewModelScope.launch {
            repository.insertTask(TaskEntity(title = title))
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task.toEntity())
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task.toEntity())
        }
    }
    // Factory để tạo ViewModel kèm Repository (vì chưa dùng Hilt)
    class Factory(private val repository: TaskRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(repository) as T
        }
    }
}

// Hàm mapping: TaskEntity (data layer) -> Task (UI layer)
private fun TaskEntity.toTask() = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    isDone = this.isDone
)
private fun Task.toEntity() = TaskEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    isDone = this.isDone
)