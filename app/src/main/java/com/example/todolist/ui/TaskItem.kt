package com.example.todolist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.Model.TaskEntity
import com.example.todolist.ViewModel.TaskViewModel

@Composable
fun TaskItem(task: TaskEntity, viewModel: TaskViewModel, onClick: () -> Unit) { //shorthand syntax
    val checked = task.isCompleted

    val priorityColor = when (task.priority) {
        "High" -> Color.Red
        "Medium" -> Color(0xFFFFA500)
        else -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },//make the card click to be able to load data
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { //if the checkbox is changed
                        viewModel.updateTask(task.copy(isCompleted = it)) //this will create copy of task but update isCompleted to whatever the user clicked
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = priorityColor,
                        uncheckedColor = priorityColor,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.description, fontWeight = FontWeight.Medium)
                    Text("Due: ${convertMillisToDate(task.deadline)}", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                }

                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = 60.dp, height = 24.dp)
                        .background(priorityColor.copy(alpha = 0.1f),RoundedCornerShape(8.dp))

                ){
                    Text(task.priority, fontSize = 12.sp, color = priorityColor, maxLines = 1)

                }
            }
        }
    }
}


