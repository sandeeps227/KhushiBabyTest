package com.example.khushibabytest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.ui.viewmodels.PatientViewModel
import com.example.khushibabytest.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PatientRegistrationScreen(
    patientViewModel: PatientViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToVisitDetails: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var healthId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val genderOptions = listOf("Male", "Female")
    // Get the status bar height
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Registration") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                modifier = Modifier.padding(top = statusBarHeight) // Adjust for status bar height
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name*") },
                isError = errorMessage != null && name.isEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = MaterialTheme.colors.error
                )
            )

            // Age Field
            OutlinedTextField(
                value = age,
                onValueChange = { newValue ->
                    // Ensure only numeric input
                    val filteredValue = newValue.filter { it.isDigit() }

                    // Limit to 2 digits
                    val limitedValue = if (filteredValue.length > 2) {
                        filteredValue.take(2)
                    } else {
                        filteredValue
                    }

                    // Convert to int, default to 0 if empty
                    age = (limitedValue.toIntOrNull() ?: 0).toString()
                },
                label = { Text("Age*") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = errorMessage != null || !isValidAge(age),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = MaterialTheme.colors.error
                )
            )

            // Gender Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = if (gender.isEmpty()) "Male" else gender,
                    onValueChange = {},
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            content = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }


            // Contact Number Field
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text("Contact Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            // Health ID Field
            OutlinedTextField(
                value = healthId,
                onValueChange = { healthId = it },
                label = { Text("Health ID/Unique Identifier*") },
                isError = errorMessage != null && healthId.isEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = MaterialTheme.colors.error
                )
            )

            // Register Button
            Button(
                onClick = {
                    if (name.isEmpty() || age.isEmpty() || healthId.isEmpty()) {
                        errorMessage = context.getString(R.string.registration_error_msg)
                    } else {
                        val patient = Patient(
                            name = name,
                            age = age.toInt(),
                            gender = gender,
                            contactNumber = contactNumber.ifEmpty { null },
                            location = location,
                            healthId = healthId
                        )
                        coroutineScope.launch {
                            patientViewModel.insertPatient(patient)
                            Toast.makeText(
                                context,
                                "Patient Registered Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateToVisitDetails(healthId)
                            errorMessage = null
                            name = ""
                            age = ""
                            gender = ""
                            contactNumber = ""
                            location = ""
                            healthId = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Register")
            }

            // Error Message Display
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        }
    }
}

fun isValidAge(age: String): Boolean {
    return age.isNotEmpty() && age.all { it.isDigit() } && age.toInt() in 1..120
}
