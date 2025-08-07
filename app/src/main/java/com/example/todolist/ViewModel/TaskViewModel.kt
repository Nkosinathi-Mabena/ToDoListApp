package com.example.todolist.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.TitleSummary
import com.example.todolist.Model.TaskEntity
import com.example.todolist.Repositories.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .map { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()) // delays sharing data with subscriber for 5 seconds

    val titlesSummaries: StateFlow<List<TitleSummary>> = repository.titleSummaries.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    val allTitles: StateFlow<List<String>> = repository.allTitles.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )


    fun addTask(task: TaskEntity) = viewModelScope.launch {
        repository.insert(task)
    }

    fun updateTask(task: TaskEntity) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.delete(task)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(repository) as T
    }
}
