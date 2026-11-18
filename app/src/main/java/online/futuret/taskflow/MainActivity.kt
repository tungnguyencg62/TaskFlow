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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.theme.TaskFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskFlowHomeScreen(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskFlowHomeScreen(modifier: Modifier = Modifier) {
    var taskList by remember { mutableStateOf(listOf<Task>(
        Task(id = 1, title = "Học Jetpack Compose"),
        Task(id = 2, title = "Thiết kế Room Database"),
        Task(id = 3, title = "Code màn hình Kanban", isDone = true)
    )) }

    Column(modifier=Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "TaskFlow", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = taskList, key = {it.id}){task ->
                TaskCard(task = task, onToggleDone = {
                    taskList = taskList.map {
                        if (it.id == task.id) it.copy(isDone = !it.isDone) else it
                    }
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newTask = Task(
                id = taskList.size + 1,
                title = "Task mới #${taskList.size + 1}"
            )
            taskList = taskList + newTask

        }) {
            Text("Thêm Task test")
        }
    }
}

@Composable
fun TaskCard(task: Task,onToggleDone: () ->Unit){
    Card(modifier = Modifier.fillMaxSize().clickable(onClick = onToggleDone)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isDone, onCheckedChange = {onToggleDone()})
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskFlowHomeScreenPreview() {
    TaskFlowTheme {
        TaskFlowHomeScreen()
    }
}