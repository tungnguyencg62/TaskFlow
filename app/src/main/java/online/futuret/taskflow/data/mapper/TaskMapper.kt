package online.futuret.taskflow.data.mapper

import online.futuret.taskflow.data.local.entity.TaskEntity

import online.futuret.taskflow.model.Task

// Hàm mapping: TaskEntity (data layer) -> Task (UI layer)
 fun TaskEntity.toTask() = Task(
    id = this.id,
    columnId = this.columnId,
   position = this.position,
    title = this.title,
    description = this.description,
    isDone = this.isDone
)
 fun Task.toEntity() = TaskEntity(
    id = this.id,
    columnId = this.columnId,
    position = this.position,
    title = this.title,
    description = this.description,
    isDone = this.isDone
)

