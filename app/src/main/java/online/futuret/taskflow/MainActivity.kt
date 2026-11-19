package online.futuret.taskflow

import android.R.attr.name
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import online.futuret.taskflow.data.local.AppDatabase
import online.futuret.taskflow.data.repository.TaskRepository
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.theme.TaskFlowTheme
import online.futuret.taskflow.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo tầng dữ liệu — tạm thời đặt ở đây, sẽ chuyển ra Application class sau
        val database = AppDatabase.getInstance(applicationContext)
        val repository = TaskRepository(database.taskDao())
        val viewModelFactory = TaskViewModel.Factory(repository)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskFlowHomeScreen(

                        modifier = Modifier.padding(innerPadding),
                                viewModelFactory = viewModelFactory
                    )
                }
            }
        }
    }
}

@Composable
fun TaskFlowHomeScreen(modifier: Modifier = Modifier,viewModelFactory: TaskViewModel.Factory) {
    val viewModel: TaskViewModel = viewModel(factory = viewModelFactory)
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var newTaskTitle by remember { mutableStateOf("") }

//    var taskList by remember { mutableStateOf(listOf<Task>(
//        Task(id = 1, title = "Học Jetpack Compose"),
//        Task(id = 2, title = "Thiết kế Room Database"),
//        Task(id = 3, title = "Code màn hình Kanban", isDone = true)
//    )) }

    Column(modifier=Modifier
        .fillMaxSize()
        .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "TaskFlow", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = tasks, key = {it.id}){task ->
                TaskCard(task = task)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                modifier = Modifier.weight(1f),
                label = { Text("Nhập tên task") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if(newTaskTitle.isNotBlank()){
                    viewModel.addTask(newTaskTitle)
                    newTaskTitle = ""
                }

            }) {
                Text("Thêm Task test")
            }
        }
    }
}

@Composable
fun TaskCard(task: Task){
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isDone, onCheckedChange = null, enabled = false)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TaskFlowHomeScreenPreview() {
//    TaskFlowTheme {
//        TaskFlowHomeScreen(task: [])
//    }
//}