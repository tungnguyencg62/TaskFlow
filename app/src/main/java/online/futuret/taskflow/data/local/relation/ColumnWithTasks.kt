package online.futuret.taskflow.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import online.futuret.taskflow.data.local.entity.ColumnEntity
import online.futuret.taskflow.data.local.entity.TaskEntity

data class ColumnWithTasks(
    @Embedded
    val column: ColumnEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "columnId"
    )
    val tasks: List<TaskEntity>,
)
