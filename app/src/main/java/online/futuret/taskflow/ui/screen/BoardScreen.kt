package online.futuret.taskflow.ui.screen

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import online.futuret.taskflow.model.ColumnUi
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.components.ReorderableTaskList
import online.futuret.taskflow.ui.navigation.Routes
import online.futuret.taskflow.ui.viewmodel.BoardViewModel
import online.futuret.taskflow.ui.viewmodel.TaskViewModel


@Composable
fun BoardScreen(
    boardId: Int,
    boardViewModelFactory: BoardViewModel.Factory,
    taskViewModelFactoryProvider: (columnId: Int) -> TaskViewModel.Factory,
    navController: NavController
) {

    val viewModel: BoardViewModel = viewModel(key = "board_vm_$boardId", factory = boardViewModelFactory)
    val columns by viewModel.columns.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kanban Board") },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {innerPadding ->
        LazyRow(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = columns, key = { it.id}){ column ->
                ColumnCard(
                    column = column,
                    taskViewModelFactoryProvider = taskViewModelFactoryProvider,
                    onDeleteColumn = { viewModel.deleteColumn(column.id) },
                    navController = navController
                )
            }
            item(key = "add_column_button"){
                AddColumnButton(
                    onAdd = { name ->
                       viewModel.addColumn(name)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddColumnButton(
    onAdd: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    OutlinedButton(
        onClick = { showDialog = true },
        modifier = Modifier.width(280.dp).fillMaxHeight()
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Column")
        Spacer(Modifier.width(4.dp))
        Text("Add Column")
    }

    if (showDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Column") },
            text = {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Column Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.isNotBlank()){
                            onAdd(name)
                            showDialog = false
                        }

                    },
                    enabled = name.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ColumnCard(
    column: ColumnUi,
    taskViewModelFactoryProvider: (Int) -> TaskViewModel.Factory,
    onDeleteColumn: () -> Unit,
    navController: NavController
) {
    val taskViewModel = viewModel<TaskViewModel>(key = "task_vm_${column.id}",
        factory = taskViewModelFactoryProvider(column.id))
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Text(
            text = "${column.name} (${column.tasks.size})",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = { showDeleteConfirm = true}
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Column", tint = MaterialTheme.colorScheme.error)
        }
        ReorderableTaskList(
            tasks= column.tasks,
        onReorder={},
        onTaskClick={ task -> editingTask = task },
        onTaskDelete={ task -> taskViewModel.deleteTask(task) },
        onOpenDetail={ task -> navController.navigate(Routes.taskDetailRoute(task.id)) },
            modifier = Modifier.weight(1f)
        )
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(items = column.tasks, key = { it.id }) { task ->
//                SwipeToDismissTaskItem(
//                    task = task,
//                    onClick = { editingTask = task },
//                    onDelete = { taskViewModel.deleteTask(task) },
//                    onOpenDetail = {navController.navigate(Routes.taskDetailRoute(task.id))}
//                )
//            }
//        }

        TextButton(
            onClick = {editingTask = Task(id = 0, title = "", description = "", columnId = column.id, position = 0)}
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
            Spacer(Modifier.width(4.dp))
            Text("Add Task")
        }
    }
    editingTask?.let { task ->
        TaskFormDialog(
            initialTitle = task.title,
            isEditing = task.id != 0,
            onDismiss = { editingTask = null },
            onConfirm = { title ->
               if (task.id == 0) {
                   taskViewModel.addTask(title)
               } else {
                   taskViewModel.updateTask(task.copy(title = title))

               }
                editingTask = null
            }
        )
    }
    if (showDeleteConfirm){
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Column") },
            text = { Text(
                if(column.tasks.isEmpty()){
                    "Are you sure you want to delete this column?"
                } else {
                    "This column has ${column.tasks.size} tasks. Deleting it will also delete all tasks in this column. Are you sure you want to delete this column?"
                }
            ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteColumn()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirm = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
























