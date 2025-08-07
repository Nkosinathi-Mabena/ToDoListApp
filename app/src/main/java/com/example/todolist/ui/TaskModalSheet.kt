package com.example.todolist.ui

import androidx.compose.ui.text.font.FontWeight
import com.example.todolist.Model.TaskEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskModalSheet(
    taskInput: String,
    onTaskInputChange: (String) -> Unit,
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    selectedTitle: String,
    onTitleChange: (String) -> Unit,
    fullTitleList: List<String>,
    selectedTask: TaskEntity?,
    onSave: (TaskEntity) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                if (selectedTask == null) "Create Task" else "Edit Task",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = taskInput,
                onValueChange = onTaskInputChange,
                label = { Text("Enter Task Description") }
            )

            Spacer(Modifier.height(8.dp))

            Text("Select Deadline", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            DatePickerFieldToModal(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                initialDate = selectedDate,
                onDateSelected = onDateSelected
            )

            Spacer(Modifier.height(12.dp))

            Text("Priority")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Low", "Medium", "High").forEach {
                    AssistChip(
                        onClick = { onPriorityChange(it) },
                        label = {
                            Text(
                                it,
                                color = if (selectedPriority == it) Color.White else Color.Black
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selectedPriority == it) Color.DarkGray else Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Title")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fullTitleList) { title ->
                    AssistChip(
                        onClick = { onTitleChange(title) },
                        label = {
                            Text(
                                title,
                                color = if (selectedTitle == title) Color.White else Color.Black
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selectedTitle == title) Color.DarkGray else Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                onSave(
                    TaskEntity(
                        id = selectedTask?.id ?: 0,
                        description = taskInput,
                        title = selectedTitle,
                        deadline = selectedDate ?: System.currentTimeMillis(),
                        priority = selectedPriority,
                        isCompleted = selectedTask?.isCompleted ?: false
                    )
                )
                onDismiss()
            }) {
                Text(if (selectedTask == null) "Set Task" else "Update Task")
            }

            if (selectedTask != null) {
                Button(onClick = {
                    onDelete()
                    onDismiss()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}
