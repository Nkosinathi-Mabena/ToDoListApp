package com.example.todolist.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,//set default value to 0 so room autogenerates, if its not 0 room know ID has been inserted manually, but this is dangerous as this may cause conflicts
    val description: String, //title
    val title: String, //changes from categories
    val deadline: Long,
    val priority: String,
    val isCompleted: Boolean = false
)
//test