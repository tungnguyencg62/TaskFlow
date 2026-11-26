package online.futuret.taskflow.data.local.relation

data class ProjectTaskCountRow(
    val id: Int,
    val name: String,
    val taskCount: Int,
    val doneCount: Int
)
