package com.example.todolist.Repositories

import com.example.todolist.Model.TaskDao
import com.example.todolist.Model.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) { // This repository that acts as a middle layer between your viewModel and Room database (DOA)
    //It will provide a clean API for accessing task data, so your viewModel or UI doesnt directly talk to the database, it has reference to taskDOA
    // this is good for SoC keeps database implementation hidden from viewModel and keeps UI clean and it also helps with Unit testing you can mock the repository in tests
    //like instead of using a real Task repository that talks to the actual database you can create a fake or mock version that returns predefined data

    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val titleSummaries = taskDao.getTitleSummaries()
    val allTitles = taskDao.getAllTitles()


    suspend fun insert(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}