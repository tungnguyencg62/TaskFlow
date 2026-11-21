package online.futuret.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.repository.ProjectRepository
import online.futuret.taskflow.model.Project

class ProjectViewModel(
    private val repository: ProjectRepository
): ViewModel() {
    val projects = repository.getAllProjects()
        .map { entities -> entities.map { it.toProject() } }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5000)
        )
    fun addProject(name: String){
        viewModelScope.launch {
            repository.insertProject(ProjectEntity(name = name))
        }
    }

    fun updateProject(project: Project){
        viewModelScope.launch {
            repository.updateProject(project.toEntity())
        }
    }

    fun deleteProject(project: Project){
        viewModelScope.launch {
            repository.deleteProject(project.toEntity())
        }
    }

    class Factory(private val repository: ProjectRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProjectViewModel(repository) as T
        }
    }
}

private fun ProjectEntity.toProject(): Project =
    Project(id = this.id, name = this.name, description = this.description)

private fun Project.toEntity(): ProjectEntity =
    ProjectEntity(id = this.id, name = this.name, description = this.description)