package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.MockData.TestweatherAPI
import com.example.todolist.Repositories.NetworkWeatherRepository
import com.example.todolist.Model.TaskEntity
import com.example.todolist.ViewModel.TaskViewModel
import com.example.todolist.ViewModel.WeatherViewModel
import com.example.todolist.ViewModel.WeatherViewModelProvider
import com.example.todolist.ViewModel.WeatherUiState
import com.example.todolist.network.WeatherAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalMaterial3Api::class) //Lets try material 3
@Composable
fun HomeScreenView(

    tasks: List<TaskEntity>,
    weather: TestweatherAPI,
    viewModel: TaskViewModel
) {


    val json = Json { ignoreUnknownKeys = true }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()



    val api = retrofit.create(WeatherAPI::class.java)
    val repository = NetworkWeatherRepository(api)
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelProvider(repository)
    )
    val state = weatherViewModel.weatherUiState as? WeatherUiState.Success ?: return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var  newTitleInput by remember { mutableStateOf("") }
    var taskInput by remember { mutableStateOf("") }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedPriority by remember { mutableStateOf("") }
    var selectedTitle by remember { mutableStateOf("") }
    val titleData = viewModel.titlesSummaries.collectAsState()
    val titles = viewModel.allTitles.collectAsState()

    val fullTitleList = remember(titles.value, selectedTitle) {
        if (selectedTitle.isNotBlank() && selectedTitle !in titles.value) {
            titles.value + selectedTitle
        } else {
            titles.value
        }
    }
    val priorities = tasks.map { it.priority }.distinct()
    var selectedPriorityFilter by remember { mutableStateOf<String?>(null) }
    var selectedCompletionFilter by remember { mutableStateOf<String>("All") }
    var selectedTitleFilter by remember { mutableStateOf<String?>(null) } // test

    val filteredTasks = tasks.filter { task ->
        val matchesPriority = selectedPriorityFilter?.let { task.priority == it } ?: true
        val matchesTitle = selectedTitleFilter?.let { task.title == it } ?: true
        val matchesCompletion = when (selectedCompletionFilter) {
            "Completed" -> task.isCompleted
            "Incomplete" -> !task.isCompleted
            else -> true
        }


        matchesPriority && matchesCompletion && matchesTitle


    }





    Scaffold(
        //FLOATING ACTION BUTTONS TO ADD TASK
        floatingActionButton = {
            TaskFabSection(
                priorities = priorities,
                onAddClick = {
                    selectedTask = null
                    taskInput = ""
                    selectedPriority = ""
                    selectedTitle = ""
                    selectedDate = null
                    showSheet = true
                },
                onFilterSelect = {
                    selectedPriorityFilter = it
                    selectedTitleFilter = null
                },
                onCompletionFilterSelect = { selectedCompletionFilter = it }
            )



        }
    ) { padding -> // the inner padding elements of the scaffold the padding values of the scaffold to avoid overlapping, such as like FAB's covering content of taskcard
       //COLUMN THAT CONTAINS WEATHER CONTENT/API
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .padding(16.dp)
        ) {
            Text("WEATHER", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 130.dp), fontFamily = FontFamily.Serif)

            Spacer(Modifier.height(12.dp))

            WeatherCard(location = weather.location, state = state)

            Spacer(Modifier.height(12.dp))

            TitleListSection(summaries = titleData.value,
                onTitleClick = {selectedTitleFilter = it})


            Spacer(Modifier.height(16.dp))

            //LAZY COLUMN TO DISPLAY TASK INFORMATION

            TaskListSection(
                tasks = filteredTasks,
                viewModel = viewModel,
                onTaskClick = {
                    selectedTask = it
                    taskInput = it.description
                    selectedTitle = it.title
                    selectedPriority = it.priority
                    selectedDate = it.deadline
                    showSheet = true
                }
            )


        }


    }

    if (showSheet) {
        TaskModalSheet(
            taskInput = taskInput,
            onTaskInputChange = { taskInput = it },
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            selectedPriority = selectedPriority,
            onPriorityChange = { selectedPriority = it },
            selectedTitle = selectedTitle,
            onTitleChange = { selectedTitle = it },
            fullTitleList = fullTitleList,
            selectedTask = selectedTask,
            onSave = {
                if (selectedTask == null) viewModel.addTask(it)
                else viewModel.updateTask(it)
                selectedTask = null
            },
            onDelete = {
                viewModel.deleteTask(selectedTask!!)
                selectedTask = null
            },
            onDismiss = {
                taskInput = ""
                selectedTitle = ""
                selectedPriority = ""
                selectedDate = null
                showSheet = false
            }
        )
    }

    AddTitleDialog(
        showDialog = showDialog,
        newTitleInput = newTitleInput,
        onInputChange = { newTitleInput = it },
        onConfirm = {
            if (newTitleInput.isNotBlank()) {
                selectedTitle = newTitleInput
            }
            newTitleInput = ""
            showDialog = false
        },
        onDismiss = {
            newTitleInput = ""
            showDialog = false
        }
    )
}






