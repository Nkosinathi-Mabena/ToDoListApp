package com.example.todolist.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    initialDate: Long? = null,
    onDateSelected: (Long) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = {},
        label = { Text("Pick a date") },
        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select date") },
        modifier = modifier
            .pointerInput(Unit) {//used to listen to touch/pointer event clicks
            awaitEachGesture { //coroutine block that waits for gesture
                awaitFirstDown(pass = PointerEventPass.Initial)
                val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                //waits for gesture to completed by release this is cool because say the
                //the user drags after touching the field, it will be seen as cancellation, so this up value need a quick normal touch and release to work properly
                //not a press and hold
                if (up != null) showModal = true
            }
        }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
                onDateSelected(it)
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val state = rememberDatePickerState()
    AlertDialog(
        // alert dialog is similar to prompt pop up or showDialog in c#, this will be better to prompt users to select date better than DatePicker

        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let(onDateSelected)
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            DatePicker(state = state, showModeToggle = false)
            //showModeToggle gives people user options (if true) between picture or typing mode
            //but mine is false so the user only can select using picture mode
        }
    )
}


//this will convert computer language🤖 to human readable language format of the date
@SuppressLint("SimpleDateFormat")
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
