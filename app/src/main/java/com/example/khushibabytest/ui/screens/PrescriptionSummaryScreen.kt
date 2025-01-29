package com.example.khushibabytest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khushibabytest.ui.viewmodels.PatientVisitViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PrescriptionSummaryScreen(
    visitId: String,
    patientVisitViewModel: PatientVisitViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val visitDetails by patientVisitViewModel.visitDetails
    val context = LocalContext.current

    LaunchedEffect(visitId) {
        patientVisitViewModel.fetchVisitDetails(visitId)
    }

    // Get the status bar height
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prescription Summary") },
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
                // Check if visitDetails is null and show loading or content
                if (visitDetails != null) {
                    val visit = visitDetails!!

                    // Patient Details Section
                    Text(
                        text = "Patient Details",
                        style = MaterialTheme.typography.h6
                    )
                    Text("Name: ${visit.patientName}", style = MaterialTheme.typography.body1)
                    Text("Patient Id: ${visit.patientId}", style = MaterialTheme.typography.body1)
                    Text(
                        "Visit Date: ${SimpleDateFormat("dd-MM-yyyy").format(Date(visit.visitDate))}",
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Visit Details Section
                    Text(
                        text = "Visit Details",
                        style = MaterialTheme.typography.h6
                    )
                    Text("Symptoms: ${visit.symptoms}", style = MaterialTheme.typography.body1)
                    Text("Diagnosis: ${visit.diagnosis}", style = MaterialTheme.typography.body1)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prescription Section
                    Text(
                        text = "Prescription",
                        style = MaterialTheme.typography.h6
                    )
                    Text("Medicine: ${visit.medicineName}", style = MaterialTheme.typography.body1)
                    Text("Dosage: ${visit.dosage}", style = MaterialTheme.typography.body1)
                    Text("Frequency: ${visit.frequency}", style = MaterialTheme.typography.body1)
                    Text("Duration: ${visit.duration}", style = MaterialTheme.typography.body1)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Print Prescription (mock action)",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Print Prescription")
                    }

                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Share Prescription (mock action)",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Share via WhatsApp/SMS")
                    }

                    Button(
                        onClick = {
                            patientVisitViewModel.markVisitAsCompleted(visit.id)
                            Toast.makeText(
                                context,
                                "Visit marked as completed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Mark Visit as Completed")
                    }
                } else {
                    // Loading State
                    Text("Loading visit details...", style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}