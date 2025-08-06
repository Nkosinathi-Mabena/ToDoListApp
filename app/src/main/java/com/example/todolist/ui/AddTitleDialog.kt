package com.example.todolist.ui
import androidx.compose.material3.*
import androidx.compose.runtime.Composable









@Composable
fun AddTitleDialog(
    showDialog: Boolean,
    newTitleInput: String,
    onInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Enter New Title") },
        text = {
            OutlinedTextField(
                value = newTitleInput,
                onValueChange = onInputChange,
                label = { Text("Title Name") }
            )
        }
    )
}
