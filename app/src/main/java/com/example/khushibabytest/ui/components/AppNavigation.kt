package com.example.khushibabytest.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khushibabytest.ui.screens.FetchPatientDetailsScreen
import com.example.khushibabytest.ui.screens.HomeScreen
import com.example.khushibabytest.ui.screens.PatientRegistrationScreen
import com.example.khushibabytest.ui.screens.PatientVisitDetailsScreen
import com.example.khushibabytest.ui.screens.PrescriptionSummaryScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToPatientRegistration = { navController.navigate("patientRegistration") },
                onNavigateToFetchPatientDetails = { navController.navigate("fetchPatientDetails") }
            )
        }

        composable("fetchPatientDetails") {
            FetchPatientDetailsScreen(
                onNavigateToPatientRegistration = { navController.navigate("patientRegistration") },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVisitDetails = { patientId ->
                    navController.navigate("patientVisitDetails/$patientId")
                }
            )
        }

        composable("patientRegistration") {
            PatientRegistrationScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVisitDetails = { patientId ->
                    navController.navigate("patientVisitDetails/$patientId")
                }
            )
        }
        composable("patientVisitDetails/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientVisitDetailsScreen(
                patientIdFromPrev = patientId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSummary = {
                    navController.navigate("prescriptionSummary/$patientId") }
            )
        }
        composable("prescriptionSummary/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PrescriptionSummaryScreen(
                visitId = patientId,
                onNavigateBack = { navController.popBackStack() })
        }
    }
}
