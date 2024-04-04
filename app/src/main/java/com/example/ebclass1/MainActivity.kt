package com.example.ebclass1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TodoApp()
            }
        }
    }
}

@Composable
fun TodoApp() {
    var text by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<Task>() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "TODO_APP",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter a task") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    tasks.add(Task(text, false))
                    text = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Add")
        }
        TaskList(tasks)
    }
}

@Composable
fun TaskList(tasks: SnapshotStateList<Task>) {
    LazyColumn {
        items(items = tasks, key = { task -> task.id }) { task ->
            TaskItem(task = task, onTaskChanged = { updatedTask ->
                tasks[tasks.indexOf(task)] = updatedTask
            })
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskChanged: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { isChecked ->
                onTaskChanged(task.copy(isDone = isChecked))
            }
        )
        Text(
            text = task.description,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = if (task.isDone) {
                TextStyle(textDecoration = TextDecoration.LineThrough, fontSize = 18.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
            } else {
                TextStyle(fontSize = 18.sp)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoApp() {
    MaterialTheme {
        TodoApp()
    }
}

data class Task(
    val description: String,
    val isDone: Boolean,
    val id: Long = System.currentTimeMillis()
)
