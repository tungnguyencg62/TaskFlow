package online.futuret.taskflow


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import online.futuret.taskflow.data.local.AppDatabase
import online.futuret.taskflow.data.repository.BoardRepository
import online.futuret.taskflow.data.repository.ColumnRepository
import online.futuret.taskflow.data.repository.ProjectRepository
import online.futuret.taskflow.data.repository.TaskRepository
import online.futuret.taskflow.ui.navigation.Routes
import online.futuret.taskflow.ui.screen.BoardScreen
import online.futuret.taskflow.ui.screen.DragPlaygroundScreen
import online.futuret.taskflow.ui.screen.ProjectListScreen
import online.futuret.taskflow.ui.screen.TaskDetailScreen
import online.futuret.taskflow.ui.theme.TaskFlowTheme
import online.futuret.taskflow.ui.viewmodel.BoardViewModel
import online.futuret.taskflow.ui.viewmodel.ProjectViewModel
import online.futuret.taskflow.ui.viewmodel.TaskDetailViewModel
import online.futuret.taskflow.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo tầng dữ liệu — tạm thời đặt ở đây, sẽ chuyển ra Application class sau
        val database = AppDatabase.getInstance(applicationContext)
        val taskRepository = TaskRepository(database.taskDao())
        val boardRepository = BoardRepository(database.boardDao())
        val columnRepository = ColumnRepository(database.columnDao())
        val projectRepository = ProjectRepository(database.projectDao())
        val projectViewModelFactory =
            ProjectViewModel.Factory(projectRepository, boardRepository, columnRepository)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.PROJECT_LIST
                    ) {
                        composable(Routes.PROJECT_LIST) {
                            ProjectListScreen(
                                viewModelFactory = projectViewModelFactory,
                                navController = navController
                            )
                        }
                        composable(
                            Routes.BOARD_ROUTE,
                            arguments = listOf(navArgument(Routes.BOARD_ARG_BOARD_ID) {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val boardId =
                                backStackEntry.arguments?.getInt(Routes.BOARD_ARG_BOARD_ID) ?: 0
                            BoardScreen(
                                boardId = boardId,
                                boardViewModelFactory = BoardViewModel.Factory(
                                    columnRepository,
                                    boardId
                                ),
                                taskViewModelFactoryProvider = { columnId ->
                                    TaskViewModel.Factory(
                                        taskRepository,
                                        columnId
                                    )
                                },
                                navController = navController
                            )
                        }

                        composable(Routes.TASK_DETAIL_ROUTE,
                            arguments = listOf(navArgument(Routes.TASK_DETAIL_ARG_ID) {
                                type = NavType.IntType
                            })
                        ) {backStackEntry ->
                            val taskId = backStackEntry.arguments?.getInt(Routes.TASK_DETAIL_ARG_ID) ?: 0
                            TaskDetailScreen(
                                viewModelFactory = TaskDetailViewModel.Factory(taskRepository,taskId),
                                navController = navController)
                        }
                        composable("drag_playground") {
                            DragPlaygroundScreen()
                        }
                    }
                }
            }
        }
    }
}


//@Composable
//fun TaskCard(task: Task,onClick: () -> Unit) {
//    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Checkbox(checked = task.isDone, onCheckedChange = null, enabled = false)
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = task.title,
//                style = MaterialTheme.typography.bodyLarge,
//                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
//            )
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun TaskFlowHomeScreenPreview() {
//    TaskFlowTheme {
//        TaskFlowHomeScreen(task: [])
//    }
//}