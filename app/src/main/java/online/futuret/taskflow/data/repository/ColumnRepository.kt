package online.futuret.taskflow.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.futuret.taskflow.data.local.dao.ColumnDao
import online.futuret.taskflow.data.local.entity.ColumnEntity
import online.futuret.taskflow.data.local.relation.ColumnWithTasks

class ColumnRepository(
    private val columnDao: ColumnDao
) {
    suspend fun insertColumn(column: ColumnEntity) = columnDao.insert(column)
    suspend fun updateColumn(column: ColumnEntity) = columnDao.update(column)
    suspend fun deleteColumn(column: ColumnEntity) = columnDao.delete(column)

    suspend fun getNextPosition(boardId: Int) = columnDao.getMaxPosition(boardId) + 1

    fun getColumnsWithTasks(boardId: Int): Flow<List<ColumnWithTasks>> {
        return columnDao.getColumnsWithTasks(boardId).map { columnsWithTasks ->
            columnsWithTasks.map { cwt ->
                cwt.copy(tasks = cwt.tasks.sortedBy { it.position })
            }
        }
    }

    suspend fun ensureDefaultColumns(boardId: Int){

        if (columnDao.countByBoard(boardId) == 0) {
            listOf("To Do", "In Progress", "Done").forEachIndexed { index, name ->
                columnDao.insert(ColumnEntity(boardId = boardId, name = name, position = index))
            }
        }
    }

    suspend fun deleteColumnById(columnId: Int) = columnDao.deleteById(columnId)
}