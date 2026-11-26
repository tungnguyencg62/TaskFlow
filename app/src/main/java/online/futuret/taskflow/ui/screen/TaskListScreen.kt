package online.futuret.taskflow.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.navigation.Routes
import online.futuret.taskflow.ui.viewmodel.TaskViewModel

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TaskListScreen(modifier: Modifier = Modifier,
//                   projectId: Int,
//                   viewModelFactory: TaskViewModel.Factory,
//                   navController: NavController
//) {
//    val viewModel: TaskViewModel = viewModel(
//        key = "task_vm_$projectId",
//        factory = viewModelFactory)
//    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
//
//    var editingTask by remember { mutableStateOf<Task?>(null) }
////    var showAddDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("TaskFlow") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                editingTask = Task(id = 0, title = "", description = "", isDone = false)
//            }) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm task")
//            }
//        }
//    ) { innerPadding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(horizontal = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(items = tasks, key = { it.id }) { task ->
//                SwipeToDismissTaskItem(task = task,
//                    onClick = {editingTask = task},
//                    onDelete = {
//                        viewModel.deleteTask(task)
//                    },
//                    onOpenDetail = { navController.navigate(Routes.taskDetailRoute(task.id)) })
//            }
//        }
//
//    }
//    editingTask?.let {task ->
//        TaskFormDialog(
//            initialTitle = task.title,
//            isEditing = task.id != 0,
//            onDismiss = { editingTask = null },
//            onConfirm = { newTitle ->
//                if(task.id == 0){
//                    viewModel.addTask(newTitle)
//                }else {
//                    viewModel.updateTask(task.copy(title = newTitle))
//                }
//                editingTask = null
//            }
//        )
//    }
//}

@Composable
fun TaskFormDialog(
    initialTitle: String,
    isEditing: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit) {
    var title by remember { mutableStateOf(initialTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Sửa task" else "Task mới") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tên task") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


@Composable
fun SwipeToDismissTaskItem(
    task: Task,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onOpenDetail: () -> Unit
){
    val dismissState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        onDismiss = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart){
                onDelete()
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isDone, onCheckedChange = null, enabled = false)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
            IconButton(onClick = onOpenDetail) {
                Icon(Icons.Default.Info, contentDescription = "Xem chi tiết")
            }
        }
    }
    }
}