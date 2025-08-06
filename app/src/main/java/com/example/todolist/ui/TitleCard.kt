package com.example.todolist.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.Model.TitleSummary


@Composable
fun TitleCard(summary: TitleSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(120.dp).height(80.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text("Tasks: ${summary.taskCount} ", fontSize = 15.sp)
                Text(summary.title, fontWeight = FontWeight.Bold)
            }
        }
    }
}

