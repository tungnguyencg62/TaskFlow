package online.futuret.taskflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import online.futuret.taskflow.data.local.entity.TaskEntity


@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

//    @Query("SELECT * FROM tasks ORDER BY id DESC")
//    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE columnId = :columnId ORDER BY position ASC")
    fun getByColumn(columnId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity?

    @Query("SELECT COALESCE(MAX(position), -1) FROM tasks WHERE columnId = :columnId")
    suspend fun getMaxPosition(columnId: Int):Int
}













