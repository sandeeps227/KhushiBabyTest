package com.example.khushibabytest

import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.data.repository.PatientRepository
import com.example.khushibabytest.ui.event.UIResult
import com.example.khushibabytest.ui.viewmodels.PatientVisitViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class PatientVisitViewModelTest {

    // Use a test dispatcher for coroutines
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: PatientVisitViewModel
    private val repository: PatientRepository = mock()

    @Before
    fun setUp() {
        viewModel = PatientVisitViewModel(repository)
    }

    @Test
    fun `saveVisitLocally should save visit and enqueue sync work`() = runTest {
        val visit = PatientVisit(id = 1, patientId = "123", patientName = "sa", symptoms = "na",
            diagnosis = "xray", medicineName = "dolo", visitDate = "2024-01-01".toLong(), dosage = "daily",
            frequency = "3 times", duration = "1hr", synced = false)
        viewModel.saveVisitLocally(visit)

        verify(repository).saveVisitLocally(visit)
        verify(repository).enqueueSyncVisitsWork()
    }

    @Test
    fun `fetchPatientDetails should update patientDetails state flow with success`() = runTest {
        val patient = Patient(id = 123, name = "John Doe",age=23,gender="male", contactNumber = "986784933", location = "hyd", healthId = "12345")
        whenever(repository.getPatientDetails("123")).thenReturn(flow { emit(UIResult.Success(patient)) })

        viewModel.fetchPatientDetails("123")

        assertTrue(viewModel.patientDetails.value is UIResult.Success)
        assertEquals(patient, (viewModel.patientDetails.value as UIResult.Success).data)
    }

    @Test
    fun `fetchPatientDetails should update patientDetails state flow with failure`() = runTest {
        val exception = Exception("Patient not found")
        whenever(repository.getPatientDetails("123")).thenReturn(flow { emit(UIResult.Failure(exception)) })

        viewModel.fetchPatientDetails("123")

        assertTrue(viewModel.patientDetails.value is UIResult.Failure)
        assertEquals("Patient not found", (viewModel.patientDetails.value as UIResult.Failure).exception.message)
    }

    @Test
    fun `fetchVisitDetails should update visitDetails mutable state`() = runTest {
        val visit = PatientVisit(id = 1, patientId = "123", patientName = "sa", symptoms = "na",
            diagnosis = "xray", medicineName = "dolo", visitDate = "2024-01-01".toLong(), dosage = "daily",
            frequency = "3 times", duration = "1hr", synced = false)
        whenever(repository.getVisitDetails("1")).thenReturn(visit)

        viewModel.fetchVisitDetails("1")

        assertNotNull(viewModel.visitDetails.value)
        assertEquals(visit, viewModel.visitDetails.value)
    }

    @Test
    fun `markVisitAsCompleted should update repository`() = runTest {
        viewModel.markVisitAsCompleted(1)

        verify(repository).markVisitAsCompleted(1)
    }
}
