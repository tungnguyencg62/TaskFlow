package online.futuret.taskflow.model

data class Project(
    val id: Int,
    val name: String,
    val description: String = ""
)

data class ProjectUi (
    val id: Int,
    val name: String,
    val taskCount: Int,
    val doneCount: Int
)