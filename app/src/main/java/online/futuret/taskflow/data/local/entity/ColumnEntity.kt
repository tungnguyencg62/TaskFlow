package online.futuret.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "columns",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = BoardEntity::class,
            parentColumns = ["id"],
            childColumns = ["boardId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["boardId"])
    ]
)
data class ColumnEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val boardId: Int,
    val name: String,
    val position: Int
)
