package online.futuret.taskflow.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import online.futuret.taskflow.data.local.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: ProjectEntity): Long

    @Update
    suspend fun update(project: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAll(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getById(projectId: Int): ProjectEntity?
}