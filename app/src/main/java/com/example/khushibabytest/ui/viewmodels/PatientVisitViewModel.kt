package com.example.khushibabytest.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.data.repository.PatientRepository
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.ui.event.UIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientVisitViewModel @Inject constructor(private val repository: PatientRepository) : ViewModel() {
    private val _visitDetails = mutableStateOf<PatientVisit?>(null)
    val visitDetails: MutableState<PatientVisit?> get() = _visitDetails

    private val _patientDetails = MutableStateFlow<UIResult<Patient>>(UIResult.Failure(Exception("exception")))
    val patientDetails: StateFlow<UIResult<Patient?>> get() = _patientDetails

    fun saveVisitLocally(visit: PatientVisit) {
        viewModelScope.launch {
            repository.saveVisitLocally(visit)
            repository.enqueueSyncVisitsWork()
        }
    }

    fun fetchPatientDetails(healthId: String) {
        viewModelScope.launch {
            repository.getPatientDetails(healthId).collect { result ->
                _patientDetails.value = result // Update the state flow with the new result
            }
        }
    }

    fun fetchVisitDetails(visitId: String) {
        viewModelScope.launch {
            _visitDetails.value = repository.getVisitDetails(visitId)
        }
    }

    fun markVisitAsCompleted(visitId: Int) {
        viewModelScope.launch {
            repository.markVisitAsCompleted(visitId)
        }
    }
}