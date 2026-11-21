package online.futuret.taskflow.ui.navigation

object Routes {
    const val PROJECT_LIST = "project_list"
    const val TASK_LIST = "task_list"
    const val TASK_LIST_ARG_PROJECT_ID = "projectId"

    const val TASK_LIST_ROUTE = "$TASK_LIST/{$TASK_LIST_ARG_PROJECT_ID}"
    fun taskListRoute(projectId: Int): String = "$TASK_LIST/$projectId"
    const val TASK_DETAIL = "task_detail"
    const val TASK_DETAIL_ARG_ID = "taskId"
    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL/{$TASK_DETAIL_ARG_ID}"

    fun taskDetailRoute(taskId: Int): String = "$TASK_DETAIL/$taskId"
}