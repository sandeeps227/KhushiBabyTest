package com.example.khushibabytest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onNavigateToPatientRegistration: () -> Unit,
               onNavigateToFetchPatientDetails:()->Unit
               ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Management") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onNavigateToPatientRegistration() },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("New Patient")
            }
            Button(
                onClick = {onNavigateToFetchPatientDetails()  },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Returning/Existing Patient")
            }
        }
    }
}