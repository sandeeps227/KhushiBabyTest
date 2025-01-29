package com.example.khushibabytest

import androidx.work.WorkManager
import com.example.khushibabytest.data.local.PatientDao
import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.data.remote.VisitApiService
import com.example.khushibabytest.data.repository.PatientRepository
import com.example.khushibabytest.ui.event.UIResult
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PatientRepositoryTest {

    private lateinit var patientDao: PatientDao
    private lateinit var apiService: VisitApiService
    private lateinit var workManager: WorkManager
    private lateinit var patientRepository: PatientRepository

    @Before
    fun setUp() {
        patientDao = mock()
        apiService = mock()
        workManager = mock()
        patientRepository = PatientRepository(patientDao, apiService, workManager)
    }

    @Test
    fun `saveVisitLocally should call insertVisit on dao`() = runBlocking {
        val visit = PatientVisit(id = 1, patientName = "John Doe", patientId = "123", visitDate = System.currentTimeMillis(), symptoms = "Cough", diagnosis = "Flu", medicineName = "Paracetamol", dosage = "500mg", frequency = "Twice a day", duration = "5 days")

        patientRepository.saveVisitLocally(visit)

        verify(patientDao).insertVisit(visit)
    }

    @Test
    fun `insertPatient should call insertPatient on dao`() = runBlocking {
        val patient = Patient(id = 123, name = "John Doe",age=23,gender="male", contactNumber = "986784933", location = "hyd", healthId = "12345")

        patientRepository.insertPatient(patient)

        verify(patientDao).insertPatient(patient)
    }

    @Test
    fun `syncVisitsWithServer should sync unsynced visits and mark them as synced`() = runBlocking {
        val visit = PatientVisit(id = 1, patientName = "John Doe", patientId = "123", visitDate = System.currentTimeMillis(), symptoms = "Cough", diagnosis = "Flu", medicineName = "Paracetamol", dosage = "500mg", frequency = "Twice a day", duration = "5 days")
        whenever(patientDao.getUnsyncedVisits()).thenReturn(listOf(visit))

        patientRepository.syncVisitsWithServer()

        verify(apiService).syncVisit(visit)
        verify(patientDao).markAsSynced(visit.id)
    }

    @Test
    fun `getPatientDetails should return success when patient exists`() = runBlocking {
        val patient = Patient(id = 123, name = "John Doe",age=23,gender="male", contactNumber = "986784933", location = "hyd", healthId = "12345")
        whenever(patientDao.getPatientDetailsById("123")).thenReturn(patient)

        val result = patientRepository.getPatientDetails("123")

        assert(result is UIResult.Success<*>)
        assert((result as UIResult.Success<*>).data == patient)
    }


    @Test
    fun `getVisitDetails should return visit from dao`() = runBlocking {
        val visit = PatientVisit(id = 1, patientName = "John Doe", patientId = "123", visitDate = System.currentTimeMillis(), symptoms = "Cough", diagnosis = "Flu", medicineName = "Paracetamol", dosage = "500mg", frequency = "Twice a day", duration = "5 days")
        whenever(patientDao.getVisitById("1")).thenReturn(visit)

        val result = patientRepository.getVisitDetails("1")

        assert(result == visit)
    }

    @Test
    fun `markVisitAsCompleted should call markVisitAsCompleted on dao`() = runBlocking {
        val visitId = 1

        patientRepository.markVisitAsCompleted(visitId)

        verify(patientDao).markVisitAsCompleted(visitId)
    }

}