package com.example.todolist.ui

import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskFabSection(
    priorities: List<String>,
    onAddClick: () -> Unit,
    onFilterSelect: (String?) -> Unit,
    onCompletionFilterSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }

        FloatingActionButton(
            onClick = { expanded = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 30.dp, bottom = 16.dp)
        ) {
            Text("🔍")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 30.dp, bottom = 10.dp)
        ) {
            priorities.forEach {
                DropdownMenuItem(
                    text = { Text("Priority: $it") },
                    onClick = {
                        onFilterSelect(it)
                        onCompletionFilterSelect("All")
                        expanded = false
                    }
                )
            }

            DropdownMenuItem(
                text = { Text("Show All Tasks") },
                onClick = {
                    onCompletionFilterSelect("All")
                    onFilterSelect(null)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Show Completed") },
                onClick = {
                    onCompletionFilterSelect("Completed")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Show Incomplete") },
                onClick = {
                    onCompletionFilterSelect("Incomplete")
                    expanded = false
                }
            )
        }
    }
}
