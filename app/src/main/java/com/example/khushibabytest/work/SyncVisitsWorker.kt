package com.example.khushibabytest.work

import android.content.Context
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.khushibabytest.data.repository.PatientRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SyncVisitsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PatientRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        return@coroutineScope try {
            repository.syncVisitsWithServer()
            Result.success()
        } catch (e: Exception) {
            // Handle the exception (e.g., log it)
            Result.retry() // Retry the work if it fails
        }
    }
}