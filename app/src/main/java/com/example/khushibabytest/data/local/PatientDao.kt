package com.example.khushibabytest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.data.local.entities.Patient

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>

    @Query("SELECT * FROM patients WHERE healthId = :healthId")
    suspend fun getPatientDetailsById(healthId: String): Patient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: PatientVisit)

    @Query("SELECT * FROM patient_visits WHERE synced = 0")
    suspend fun getUnsyncedVisits(): List<PatientVisit>

    @Query("UPDATE patient_visits SET synced = 1 WHERE id = :visitId")
    suspend fun markAsSynced(visitId: Int)

    @Query("UPDATE patient_visits SET completed = 1 WHERE id = :visitId")
    suspend fun markVisitAsCompleted(visitId: Int)

    @Query("SELECT * FROM patient_visits WHERE patientId = :healthId")
    suspend fun getVisitById(healthId: String): PatientVisit
}