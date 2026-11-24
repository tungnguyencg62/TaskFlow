package online.futuret.taskflow.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import online.futuret.taskflow.data.local.entity.ProjectEntity
import online.futuret.taskflow.data.local.entity.TaskEntity

data class ProjectWithTasks(
    @Embedded
    val project: ProjectEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<TaskEntity>
)
