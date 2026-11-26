package online.futuret.taskflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import online.futuret.taskflow.data.local.entity.ColumnEntity
import online.futuret.taskflow.data.local.relation.ColumnWithTasks

@Dao
interface ColumnDao {
    @Insert
    suspend fun insert(column: ColumnEntity): Long

    @Update
    suspend fun update(column: ColumnEntity)

    @Delete
    suspend fun delete(column: ColumnEntity)

    @Query("SELECT * FROM columns WHERE boardId = :boardId ORDER BY position ASC")
    fun getByBoard(boardId: Int): Flow<List<ColumnEntity>>

    @Query("SELECT COALESCE(MAX(position), -1) FROM columns WHERE boardId = :boardId")
    suspend fun getMaxPosition(boardId: Int): Int

    @Transaction
    @Query("SELECT * FROM columns WHERE boardId = :boardId ORDER BY position ASC")
    fun getColumnsWithTasks(boardId: Int): Flow<List<ColumnWithTasks>>

    @Query("SELECT COUNT(*) FROM columns WHERE boardId = :boardId")
    suspend fun countByBoard(boardId: Int): Int


    @Query("DELETE FROM columns WHERE id = :columnId")
    suspend fun deleteById(columnId: Int)
}













