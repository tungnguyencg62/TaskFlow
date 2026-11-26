package online.futuret.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import online.futuret.taskflow.data.local.entity.ColumnEntity
import online.futuret.taskflow.data.local.relation.ColumnWithTasks
import online.futuret.taskflow.data.mapper.toTask
import online.futuret.taskflow.data.repository.ColumnRepository
import online.futuret.taskflow.model.ColumnUi

class BoardViewModel(
    private val columnRepository: ColumnRepository,
    private val boardId: Int
): ViewModel() {
    val columns = columnRepository.getColumnsWithTasks(boardId)
        .map { list ->
            list.map { it.toColumnUi() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addColumn(name: String){
        viewModelScope.launch {
            val nextPosition = columnRepository.getNextPosition(boardId)
            columnRepository.insertColumn(ColumnEntity(boardId = boardId, name = name, position = nextPosition))
        }
    }

    fun deleteColumn(columnId: Int){
        viewModelScope.launch {
            columnRepository.deleteColumnById(columnId)
        }
    }

    class Factory(
        private val columnRepository: ColumnRepository,
        private val boardId: Int
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BoardViewModel(columnRepository, boardId) as T
        }
    }
}

private fun ColumnWithTasks.toColumnUi(): ColumnUi = ColumnUi(
    id = column.id,
    name = column.name,
    tasks = tasks.map { it.toTask() }
)