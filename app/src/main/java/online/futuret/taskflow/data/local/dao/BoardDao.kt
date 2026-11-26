package online.futuret.taskflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import online.futuret.taskflow.data.local.entity.BoardEntity


@Dao
interface BoardDao {
    @Insert
    suspend fun insert(board: BoardEntity): Long

    @Update
    suspend fun update(board: BoardEntity)

    @Delete
    suspend fun delete(board: BoardEntity)

    @Query("SELECT * FROM boards WHERE projectId = :projectId")
    fun getByProject(projectId: Int): Flow<List<BoardEntity>>

    @Query("SELECT * FROM boards WHERE id = :boardId")
    suspend fun getById(boardId: Int): BoardEntity?

    @Query("SELECT * FROM boards WHERE projectId = :projectId LIMIT 1")
    suspend fun getFirstByProject(projectId: Int): BoardEntity?
}













