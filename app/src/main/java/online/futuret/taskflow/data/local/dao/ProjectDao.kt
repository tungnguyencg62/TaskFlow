package online.futuret.taskflow.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.local.relation.ProjectTaskCountRow


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

    @Query(
        """
    SELECT 
        projects.id AS id,
        projects.name AS name,
        COUNT(tasks.id) AS taskCount,
        SUM(CASE WHEN tasks.isDone = 1 THEN 1 ELSE 0 END) AS doneCount
    FROM projects
    LEFT JOIN boards ON boards.projectId = projects.id
    LEFT JOIN columns ON columns.boardId = boards.id
    LEFT JOIN tasks ON tasks.columnId = columns.id
    GROUP BY projects.id
    ORDER BY projects.name ASC
    """
    )
    fun getProjectsWithTaskCounts(): Flow<List<ProjectTaskCountRow>>
}