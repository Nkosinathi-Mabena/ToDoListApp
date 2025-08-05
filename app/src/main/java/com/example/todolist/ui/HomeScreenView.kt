package com.example.todolist.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.Model.TitleSummary
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Boy oh boy😪
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
    val state = weatherViewModel.weatherUiState as? WeatherUiState.Successs ?: return

    var expanded by remember { mutableStateOf(false) }
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






    Scaffold(
        //FLOATING ACTION BUTTONS TO ADD TASK
        floatingActionButton = {
            Box(modifier = Modifier.fillMaxSize()) {

                FloatingActionButton(
                    onClick = {

                        selectedTask = null
                        taskInput = ""
                        selectedPriority = ""
                        selectedTitle = ""
                        selectedDate = null
                        //when the add task action button is clicked this will make the text fields empty
                        showSheet = true},
                    modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }


                //FLOATING ACTION TO FILTER FOR TASK

                FloatingActionButton(
                    onClick = { expanded = true },
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 30.dp, bottom = 16.dp)
                ) {
                    Text("🔍")
                }
                //DROPDOWN MENU ITEM

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }, //If i tap anywhere outside the expansion of the dropdown will close
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 30.dp, bottom = 10.dp)
                ) {
                    priorities.forEach {
                        DropdownMenuItem(
                            text = { Text("Priority: $it") },
                            onClick = {
                                selectedPriorityFilter = it
                                selectedCompletionFilter = "All"
                                expanded = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Show All Tasks") },
                        onClick = {
                            selectedCompletionFilter = "All"
                            selectedTitleFilter = null //testing
                            selectedPriorityFilter = null
                            expanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Show Completed") },
                        onClick = {
                            selectedCompletionFilter = "Completed"
                            expanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Show Incomplete") },
                        onClick = {
                            selectedCompletionFilter = "Incomplete"
                            expanded = false
                        }
                    )

                }
            }




        }
    ) { padding -> // the inner padding elements of the scaffold the padding values of the scaffold to avoid overlapping, such as like FAB's covering content of taskcard
       //COLUMN THAT CONTAINS WEATHER CONTENT/API
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .padding(16.dp)
        ) {
            Text("WEATHER", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 130.dp), fontFamily = FontFamily.Serif)
            Spacer(Modifier.height(12.dp))
            Column(Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(weather.location, fontSize = 30.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("${state.weatherInfo.current.temp}", fontSize = 25.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("🌡", fontSize = 20.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🌤️", fontSize = 23.sp, modifier = Modifier.padding(start = 50.dp))
                            Text("🌥️", fontSize = 23.sp,modifier = Modifier.padding(end = 50.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sunrise - ${state.astronomyInfo.astronomy.astro.sunrise}", fontSize = 15.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
                            Spacer(Modifier.height(12.dp))
                            Text("Sunset - ${state.astronomyInfo.astronomy.astro.sunset}", fontSize = 15.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("TITLES", fontSize = 18.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
            Spacer(Modifier.height(8.dp))

            //LAZY ROW TO DISPLAY TITLES OF TASKS
            LazyRow(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(titleData.value) { summary ->
                    TitleCard(summary = summary, onClick = { //Layout composable
                        selectedTitleFilter = summary.title
                    })
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("TASKS", fontSize = 18.sp, fontWeight = FontWeight.Bold,fontFamily = FontFamily.Serif)
            Spacer(Modifier.height(8.dp))

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

            //LAZY COLUMN TO DISPLAY TASK INFORMATION
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(task = task, viewModel = viewModel) {
                        selectedTask = task
                        taskInput = task.description
                        selectedTitle = task.title
                        selectedPriority = task.priority
                        selectedDate = task.deadline
                        showSheet = true
                    }
                }
            }
        }


    }

    if (showSheet) {
        ModalBottomSheet( //new : A ModalBottomSheet is a panel that will slide from the bottom of the screen and overlap content shown,
            //its called because it blocks content and prevents certain interaction
            //this has been experimented on in Material 3, so...
            onDismissRequest = { showSheet = false },// New: This should handle the visibility of the sheet
            sheetState = sheetState // This will handle how the state behaves
        ) {



            Column(modifier = Modifier.padding(16.dp)) {
                Text("Create Task", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),value = taskInput,
                    onValueChange = { taskInput = it }, label = { Text("Enter Task Description") }
                )

                Spacer(Modifier.height(8.dp))
                Column(Modifier.fillMaxWidth()) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Deadline", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(8.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        DatePickerFieldToModal(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            initialDate = selectedDate,
                            onDateSelected = { date ->
                                selectedDate = date
                            }
                        )


                    }
                }

                Spacer(Modifier.height(12.dp))

                Text("Priority")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Low", "Medium", "High").forEach {
                        AssistChip(
                            onClick = { selectedPriority = it },
                            label = {
                                Text(
                                    it,
                                    color = if (selectedPriority == it) Color.White else Color.Black
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selectedPriority == it) Color.DarkGray else Color.White
                            )
                        )

                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Title")

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    item {
                        AssistChip(
                            onClick = { showDialog = true },
                            label = { Text("+ Add New Subject") }
                        )
                    }
                    items(fullTitleList) { title -> //pull data object of list of titles
                        AssistChip( //assists chips better for when i have multiple option and want to change appearance of the option i have selected; assists chips👍🏿
                            //i need store the value of the selected title in mutable state
                            onClick = { selectedTitle = title }, label = { Text(title,color = if (selectedTitle == title) Color.White else Color.Black)}, //this assist chip will store title name
                            colors = AssistChipDefaults.assistChipColors(
                                //so if the assist chip is select it will be grey else it should/will be white
                                containerColor = if (selectedTitle == title) Color.DarkGray else Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = {
                    val task = TaskEntity(
                        id = selectedTask?.id ?: 0, //If selected task is null reuse its ID
                        description = taskInput,
                        title = selectedTitle,
                        deadline = selectedDate ?: System.currentTimeMillis(),
                        priority = selectedPriority,
                        isCompleted = selectedTask?.isCompleted ?: false //this will keep the exist
                    )

                    if (selectedTask == null) viewModel.addTask(task)
                    else viewModel.updateTask(task)

                    // Reset
                    selectedTask = null
                    taskInput = ""
                    selectedTitle = ""
                    selectedPriority = ""
                    selectedDate = null
                    showSheet = false
                }) {
                    Text(if (selectedTask == null) "Set Task" else "Update Task")
                }

                if (selectedTask != null) {
                    Button(onClick = {
                        viewModel.deleteTask(selectedTask!!)
                        selectedTask = null
                        taskInput = ""
                        selectedTitle = ""
                        selectedPriority = ""
                        selectedDate = null
                        showSheet = false
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
    if (showDialog) { //AlERT DIALOG TO ENTER NEW TITLE
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    if (newTitleInput.isNotBlank()) {
                        selectedTitle = newTitleInput
                    }
                    showDialog = false
                    newTitleInput = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = { TextButton(onClick = {
                    showDialog = false
                    newTitleInput = ""
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("Enter New Title") },
            text = {
                OutlinedTextField(
                    value = newTitleInput, onValueChange = { newTitleInput = it }, label = { Text("Title Name") }
                )
            }
        )
    }

}


//TITLE CARD COMPOSABLE FOR LAZY ROW IN SCAFFOLD
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






//TITLE CARD COMPOSABLE FOR LAZY COLUMN IN SCAFFOLD
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
//Test If UI displays test weatherAPI mock data
data class TestweatherAPI( val location: String, val sunrise: String, val sunset: String, val temperature: String)

//MOCK DATA TO USE WHEN I WAS DESIGNING UI LAYOUT

//data class Task(val title: String, val dueDate: String, val priority: String, val category:String,val taskCount:Int)
/*@Preview(showBackground = true)
@Composable
fun PreviewCategoriesScreen() {

    val weather = weatherAPI("Sandton", "6:23","17:30", "34°C"  )
/* mock data
    val tasks = listOf(
        Task("Add Navigation Component Graphs and also the winners circle", "Sep 19, 2022", "Low", "Interview",2),
        Task("Maybe prepare for interviews", "Sep 20, 2022" ,"Medium","Interview",10),
        Task("Listen to Anne Marie", "Sep 21, 2022", "Medium", "Song",3),
        Task("Start preparing research paper", "Sep 21, 2022", "High", "HighTech",20),
        Task("Maintain specialist in Codeforces", "Sep 23, 2022", "Low","School",3),
        Task("Add Navigation Component Graphs", "Sep 19, 2022", "Medium","School",1),
        Task("Maybe prepare for interviews", "Sep 20, 2022", "Medium","HighTech",1),
        Task("Listen to Anne Marie", "Sep 21, 2022", "Medium","Song",5),
        Task("Start preparing research paper", "Sep 21, 2022", "High","Interview",22),
        Task("Maintain specialist in Codeforces", "Sep 23, 2022", "Low","HighTech",200),

    )
    //HomeScreenView(tasks, weather, onAddTask = {})

 */




}

 */



