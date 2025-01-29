package com.example.khushibabytest.data.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.data.local.PatientDao
import com.example.khushibabytest.data.remote.VisitApiService
import com.example.khushibabytest.ui.event.UIResult
import com.example.khushibabytest.work.SyncVisitsWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val dao: PatientDao,
    private val apiService: VisitApiService,
    private val workManager: WorkManager
) {

    suspend fun saveVisitLocally(visit: PatientVisit) {
        dao.insertVisit(visit)
    }

    suspend fun insertPatient(patient: Patient) {
        dao.insertPatient(patient)
    }

    suspend fun syncVisitsWithServer() {
        val unsyncedVisits = dao.getUnsyncedVisits()
        unsyncedVisits.forEach { visit ->
            try {
                apiService.syncVisit(visit) // Sync with server
                dao.markAsSynced(visit.id) // Mark as synced locally
            } catch (e: Exception) {
                Log.e("syncVisitsError", e.message.toString())
            }
        }
    }

    // Function to get patient details as a Flow
    fun getPatientDetails(healthId: String): Flow<UIResult<Patient>> = flow {
        try {
            val patient = dao.getPatientDetailsById(healthId)
            if (patient != null) {
                emit(UIResult.Success(patient)) // Emit success if patient is not null
            } else {
                emit(UIResult.Failure(Exception("Patient not found"))) // Emit failure if patient is null
            }
        } catch (e: Exception) {
           // emit(UIResult.Failure(e)) // Emit failure result
        }
    }

    suspend fun getVisitDetails(visitId: String): PatientVisit {
        return dao.getVisitById(visitId)
    }

    suspend fun markVisitAsCompleted(visitId: Int) {
        dao.markVisitAsCompleted(visitId)
    }

    fun enqueueSyncVisitsWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when connected to the internet
            .build()

        val syncVisitsWork = OneTimeWorkRequestBuilder<SyncVisitsWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncVisitsWork)
    }
}
