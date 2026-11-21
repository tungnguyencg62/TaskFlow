package online.futuret.taskflow.model

data class Task(
    val id: Int,
    val projectId: Int = 1,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false
)
