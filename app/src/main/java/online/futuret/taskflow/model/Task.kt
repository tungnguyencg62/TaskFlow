package online.futuret.taskflow.model

data class Task(
    val id: Int,
    val columnId: Int,
    val position: Int,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false
)
