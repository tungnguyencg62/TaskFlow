package online.futuret.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.local.relation.ProjectTaskCountRow
import online.futuret.taskflow.data.repository.BoardRepository
import online.futuret.taskflow.data.repository.ColumnRepository
import online.futuret.taskflow.data.repository.ProjectRepository
import online.futuret.taskflow.model.Project
import online.futuret.taskflow.model.ProjectUi

class ProjectViewModel(
    private val repository: ProjectRepository,
    private val boardRepository: BoardRepository,
    private val columnRepository: ColumnRepository
): ViewModel() {
    val projects = repository.getProjectsWithTaskCounts()
        .map { entities -> entities.map { it.toProjectUi() } }
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

    fun openProject(projectId: Int,onBoardReady: (Int) -> Unit){
        viewModelScope.launch {
            val boardId = boardRepository.getOrCreateDefaultBoard(projectId)
            columnRepository.ensureDefaultColumns(boardId)
            onBoardReady(boardId)
        }
    }

    class Factory(private val projectRepository: ProjectRepository,
                  private val boardRepository: BoardRepository,
                  private val columnRepository: ColumnRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProjectViewModel(projectRepository,boardRepository,columnRepository) as T
        }
    }
}

private fun ProjectEntity.toProject(): Project =
    Project(id = this.id, name = this.name, description = this.description)

private fun Project.toEntity(): ProjectEntity =
    ProjectEntity(id = this.id, name = this.name, description = this.description)

private fun ProjectTaskCountRow.toProjectUi(): ProjectUi = ProjectUi(
    id = id,
    name = name,
    taskCount = taskCount,
    doneCount = doneCount
)