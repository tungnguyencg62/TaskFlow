package online.futuret.taskflow


import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import online.futuret.taskflow.data.local.AppDatabase
import online.futuret.taskflow.data.repository.ProjectRepository
import online.futuret.taskflow.data.repository.TaskRepository
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.navigation.Routes
import online.futuret.taskflow.ui.screen.ProjectListScreen
import online.futuret.taskflow.ui.screen.TaskDetailScreen
import online.futuret.taskflow.ui.screen.TaskListScreen
import online.futuret.taskflow.ui.theme.TaskFlowTheme
import online.futuret.taskflow.ui.viewmodel.ProjectViewModel
import online.futuret.taskflow.ui.viewmodel.TaskDetailViewModel
import online.futuret.taskflow.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo tầng dữ liệu — tạm thời đặt ở đây, sẽ chuyển ra Application class sau
        val database = AppDatabase.getInstance(applicationContext)
        val taskRepository = TaskRepository(database.taskDao())
        val projectRepository = ProjectRepository(database.projectDao())
        val projectViewModelFactory = ProjectViewModel.Factory(projectRepository)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.PROJECT_LIST
                    ) {
                        composable(Routes.PROJECT_LIST){
                            ProjectListScreen(viewModelFactory = projectViewModelFactory,navController = navController)
                        }
                        composable(Routes.TASK_LIST_ROUTE,
                            arguments = listOf(navArgument(Routes.TASK_LIST_ARG_PROJECT_ID){
                                type = NavType.IntType
                            })) {backStackEntry ->
                            val projectId = backStackEntry.arguments?.getInt(Routes.TASK_LIST_ARG_PROJECT_ID) ?: 0
                            TaskListScreen(modifier = Modifier.padding(innerPadding),
                                projectId = projectId,
                                viewModelFactory = TaskViewModel.Factory(taskRepository,projectId),
                                navController = navController)
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