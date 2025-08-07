package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.Model.TaskEntity
import com.example.todolist.ViewModel.TaskViewModel

@Composable
fun TaskListSection(
    tasks: List<TaskEntity>,
    viewModel: TaskViewModel,
    onTaskClick: (TaskEntity) -> Unit
) {
    Column {
        Text("TASKS", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(task = task, viewModel = viewModel) {
                    onTaskClick(task)
                }
            }
        }
    }
}
