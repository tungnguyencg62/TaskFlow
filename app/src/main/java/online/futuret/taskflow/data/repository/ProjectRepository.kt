package online.futuret.taskflow.data.repository

import online.futuret.taskflow.data.local.dao.ProjectDao
import online.futuret.taskflow.data.local.entity.ProjectEntity

class ProjectRepository(private val projectDao: ProjectDao) {
    fun getAllProjects() = projectDao.getAll()

    suspend fun insertProject(projectEntity: ProjectEntity) = projectDao.insert(projectEntity)

    suspend fun updateProject(projectEntity: ProjectEntity) = projectDao.update(projectEntity)

    suspend fun deleteProject(projectEntity: ProjectEntity) = projectDao.delete(projectEntity)

    suspend fun getProjectById(projectId: Int) = projectDao.getById(projectId)
}