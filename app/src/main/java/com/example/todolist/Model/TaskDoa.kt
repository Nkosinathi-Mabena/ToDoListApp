package com.example.todolist.Model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao { //Function to interact with the tasks table in the database
    //In Room the DOA must always be interface or abstract class as it is part of Rooms design pattern where Room generates the actual code behind the scenes. This keeps
    //your code clean and focuses only on declaring what should happen, like instance @Insert will generate in the background all you have to use the annotation

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace so if the task exists already its overwritten
    suspend fun insertTask(task: TaskEntity) // functions are suspended so they can be asynchronous and can be paused with blocking the main thread/process. Lets say insert task may take time
    //its important for android to keep the UI responsive

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT title, COUNT(*) as taskCount FROM tasks GROUP BY title") //categories before
    fun getTitleSummaries(): Flow<List<TitleSummary>>

    @Query("SELECT DISTINCT title FROM tasks")
    fun getAllTitles(): Flow<List<String>>

    @Query("SELECT * FROM tasks WHERE priority = :priority")
    fun getTasksByPriority(priority: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<TaskEntity>>



    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    fun getIncompleteTasks(): Flow<List<TaskEntity>>




}
data class TitleSummary(val title: String, val taskCount: Int) //data class needed because the query selects only some columns and scheme doesnt match TaskEntity
// it will only return title and count column and leave out the rest which doesnt match the TaskEntity as it missing idm description and other columns
