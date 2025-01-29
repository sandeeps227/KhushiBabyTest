package com.example.khushibabytest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import com.example.khushibabytest.ui.event.UIResult
import com.example.khushibabytest.ui.viewmodels.PatientVisitViewModel

@Composable
fun FetchPatientDetailsScreen(
    onNavigateToPatientRegistration: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PatientVisitViewModel = hiltViewModel(),
    onNavigateToVisitDetails: (String) -> Unit
) {
    var healthId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val patientDetails by viewModel.patientDetails.collectAsState()
    var isLoading by remember { mutableStateOf(false) } // Loading state
    var isButtonClicked by remember { mutableStateOf(false) } // Track button click

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Returning/Existing Patient") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Button(
                onClick = {
                    if (healthId.isEmpty()) {
                        errorMessage = context.getString(R.string.registration_error_msg)
                    } else {
                        isLoading = true // Set loading state
                        isButtonClicked = true
                        viewModel.fetchPatientDetails(healthId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Fetch Details")
            }

            // Handle the result after the button click

            LaunchedEffect(patientDetails, isButtonClicked) {
                if (isButtonClicked) {
                    when (patientDetails) {
                        is UIResult.Success -> {
                            isLoading = false // Reset loading state
                            Toast.makeText(
                                context,
                                context.getString(R.string.fetch_successful),
                                Toast.LENGTH_LONG
                            ).show()
                            onNavigateToVisitDetails(healthId)
                        }

                        is UIResult.Failure -> {
                            isLoading = false // Reset loading state
                            val exception = (patientDetails as UIResult.Failure).exception
                            // Display error message
                                Toast.makeText(
                                    context,
                                    "No Details Found: ${exception.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                    }
                }
            }

            // Show loading indicator if fetching
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}