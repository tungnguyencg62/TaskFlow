package online.futuret.taskflow.data.repository

import online.futuret.taskflow.data.local.dao.TaskDao
import online.futuret.taskflow.data.local.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks() = taskDao.getAll()

    suspend fun insertTask(task: TaskEntity) = taskDao.insert(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.update(task)

}