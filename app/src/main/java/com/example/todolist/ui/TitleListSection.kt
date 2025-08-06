package com.example.todolist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.Model.TitleSummary
import androidx.compose.foundation.lazy.items


@Composable
fun TitleListSection(
    summaries: List<TitleSummary>,
    onTitleClick: (String) -> Unit
){
    Text("TITLES", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(8.dp))

    LazyRow(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(summaries) { summary ->
            TitleCard(summary = summary){
                onTitleClick(summary.title)
            }
        }
    }
}