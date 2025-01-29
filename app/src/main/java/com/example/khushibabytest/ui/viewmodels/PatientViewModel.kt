package com.example.khushibabytest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khushibabytest.data.local.entities.Patient
import com.example.khushibabytest.data.repository.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(private val repository: PatientRepository) : ViewModel() {

    fun insertPatient(patient: Patient) {
        viewModelScope.launch {
            repository.insertPatient(patient)
        }
    }
}