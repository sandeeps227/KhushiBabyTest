package com.example.khushibabytest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_visits")
data class PatientVisit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientName:String,
    val patientId: String,
    val visitDate: Long,
    val symptoms: String,
    val diagnosis: String,
    val medicineName: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val synced: Boolean = false, // Indicates if synced with the server
    val completed: Boolean = false // Indicates if the visit is marked as completed
)
