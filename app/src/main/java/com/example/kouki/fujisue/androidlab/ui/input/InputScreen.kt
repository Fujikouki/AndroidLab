package com.example.kouki.fujisue.androidlab.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen() {

    var textValue by remember { mutableStateOf("") }
    var outlinedTextValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isSwitched by remember { mutableStateOf(false) }
    val radioOptions = listOf("Option 1", "Option 2", "Option 3")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf("未選択") }

    // Time Picker State
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var selectedTime by remember { mutableStateOf("未選択") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Examples") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TextField
            Text("TextField", style = MaterialTheme.typography.titleMedium)
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("お名前") },
                modifier = Modifier.fillMaxWidth()
            )

            // OutlinedTextField
            Text("OutlinedTextField", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = outlinedTextValue,
                onValueChange = { outlinedTextValue = it },
                label = { Text("メールアドレス") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // Password TextField
            Text("Password Field", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                label = { Text("パスワード") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            // Checkbox
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("利用規約に同意する")
            }

            // Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = isSwitched,
                    onCheckedChange = { isSwitched = it }
                )
                Text("通知を${if (isSwitched) "ON" else "OFF"}")
            }

            // RadioButton
            Text("RadioButton", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // RowのonClickで処理するためnull
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // Slider
            Text("Slider", style = MaterialTheme.typography.titleMedium)
            Column {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..100f,
                    steps = 9 // 10段階に分割
                )
                Text(
                    text = "現在の値: ${sliderPosition.roundToInt()}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Date Picker
            Text("Date Picker", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("選択された日付: $selectedDate")
                Spacer(Modifier.weight(1f))
                Button(onClick = { showDatePicker = true }) {
                    Text("日付を選択")
                }
            }

            // Time Picker
            Text("Time Picker", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("選択された時刻: $selectedTime")
                Spacer(Modifier.weight(1f))
                Button(onClick = { showTimePicker = true }) {
                    Text("時刻を選択")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                            datePickerState.selectedDateMillis?.let {
                                selectedDate = sdf.format(Date(it))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("キャンセル")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            Dialog(onDismissRequest = { showTimePicker = false }) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 6.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimePicker(state = timePickerState)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showTimePicker = false }) {
                                Text("キャンセル")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    selectedTime = String.format(
                                        "%02d:%02d",
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )
                                    showTimePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputScreenPreview() {
    AndroidLabTheme {
        InputScreen()
    }
}
