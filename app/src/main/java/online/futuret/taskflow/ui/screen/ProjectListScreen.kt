package online.futuret.taskflow.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import online.futuret.taskflow.ui.navigation.Routes
import online.futuret.taskflow.ui.viewmodel.ProjectViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    viewModelFactory: ProjectViewModel.Factory,
    navController: NavController
) {

    val viewModel: ProjectViewModel = viewModel(factory = viewModelFactory)
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Projects") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Project")
                    }
                }
            )
        }
    ) { innerPadding  ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = projects, key = {it.id}){ project ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                    onClick = {
                        viewModel.openProject(project.id){boardId ->
                            navController.navigate(Routes.boardRoute(boardId))}
                        }

                )
                ){
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(project.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${project.doneCount}/${project.taskCount} task hoàn thành",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

    }
    if(showAddDialog){
        ProjectFormDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                viewModel.addProject(name)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ProjectFormDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit){
    var name by remember { mutableStateOf("") }

    AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Project") },
    text = {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Project Name") },
            singleLine = true
        )
    },
    confirmButton = {
        TextButton(onClick = { if (name.isNotBlank()) onConfirm(name.trim()) },
            enabled = name.isNotBlank()) {
            Text("Add")
        }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

















