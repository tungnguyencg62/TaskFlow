package online.futuret.taskflow.data.repository

import online.futuret.taskflow.data.local.dao.TaskDao
import online.futuret.taskflow.data.local.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {
    fun getTasksByColumn(columnId: Int) = taskDao.getByColumn(columnId)


    suspend fun getTaskById(taskId: Int) = taskDao.getTaskById(taskId)
    suspend fun insertTask(task: TaskEntity) = taskDao.insert(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.update(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.delete(task)

    suspend fun getNextPosition(columnId: Int) = taskDao.getMaxPosition(columnId) + 1

}