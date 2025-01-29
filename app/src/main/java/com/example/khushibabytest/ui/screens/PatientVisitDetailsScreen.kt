package com.example.khushibabytest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khushibabytest.R
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.ui.event.UIResult
import com.example.khushibabytest.ui.viewmodels.PatientVisitViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PatientVisitDetailsScreen(
    patientIdFromPrev: String,
    patientVisitViewModel: PatientVisitViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSummary: (String) -> Unit
) {
    var visitDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(Date())) }
    var symptoms by remember { mutableStateOf("") }
    val patientId by remember { mutableStateOf(patientIdFromPrev) }
    var patientName by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val patientDetails by patientVisitViewModel.patientDetails.collectAsState()

    LaunchedEffect(patientIdFromPrev) {
        patientVisitViewModel.fetchPatientDetails(patientIdFromPrev)
    }

    // Get the status bar height
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Visit Details") },
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Patient Name and ID Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (patientDetails) {
                        is UIResult.Success -> {
                            val patient = (patientDetails as UIResult.Success).data
                            patientName =patient!!.name
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Patient Name:", style = MaterialTheme.typography.body1)
                                Text(text = patient!!.name, style = MaterialTheme.typography.body2)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Patient ID:", style = MaterialTheme.typography.body1)
                                Text(text = patient!!.healthId, style = MaterialTheme.typography.body2)
                            }
                        }
                        is UIResult.Failure -> {
                            val exception = (patientDetails as UIResult.Failure).exception
                            // Display error message
                            Text(text = "Error fetching patient details: ${exception.message}")
                        }
                    }
                }
            }

            // Visit Date Field
            item {
                OutlinedTextField(
                    value = visitDate,
                    onValueChange = { visitDate = it },
                    label = { Text("Visit Date") },
                    enabled = false, // Prefilled with today's date
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            // Symptoms Field
            item {
                OutlinedTextField(
                    value = symptoms,
                    onValueChange = { symptoms = it },
                    label = { Text("Symptoms") },
                    isError = errorMessage != null && symptoms.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 3
                )
            }

            // Diagnosis Field
            item {
                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis") },
                    isError = errorMessage != null && diagnosis.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 3
                )
            }

            // Medicine Name Field
            item {
                OutlinedTextField(
                    value = medicineName,
                    onValueChange = { medicineName = it },
                    label = { Text("Medicine Name") },
                    isError = errorMessage != null && medicineName.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 2
                )
            }

            // Dosage Field
            item {
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    isError = errorMessage != null && dosage.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 2
                )
            }

            // Frequency Field
            item {
                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    isError = errorMessage != null && frequency.isEmpty(),
                    label = { Text("Frequency") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 1
                )
            }

            // Duration Field
            item {
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration") },
                    isError = errorMessage != null && duration.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    maxLines = 1
                )
            }

            // Add a Spacer before the buttons to prevent overlap
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Save Visit Details Button
            item {
                Button(
                    onClick = {
                        if (symptoms.isEmpty() || diagnosis.isEmpty() || medicineName.isEmpty()
                            ||dosage.isEmpty() || frequency.isEmpty() || duration.isEmpty()) {
                            errorMessage = context.getString(R.string.registration_error_msg)
                        } else {
                            val visit = PatientVisit(
                            patientName = patientName,
                            patientId = patientId,
                            visitDate = System.currentTimeMillis(),
                            symptoms = symptoms,
                            diagnosis = diagnosis,
                            medicineName = medicineName,
                            dosage = dosage,
                            frequency = frequency,
                            duration = duration
                        )
                        patientVisitViewModel.saveVisitLocally(visit)
                        Toast.makeText(
                            context,
                            "Visit Details Saved Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToSummary(patientId)
                    }},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save Visit Details")
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
}