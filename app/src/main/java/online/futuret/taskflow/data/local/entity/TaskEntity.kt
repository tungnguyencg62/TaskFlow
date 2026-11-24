package online.futuret.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ColumnEntity::class,
            parentColumns = ["id"],
            childColumns = ["columnId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["columnId"])
    ])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val columnId: Int,
    val position: Int,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false
)
