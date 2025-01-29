package com.example.khushibabytest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.khushibabytest.data.local.entities.PatientVisit
import com.example.khushibabytest.data.local.entities.Patient

@Database(entities = [Patient::class, PatientVisit::class], version = 4)
abstract class PatientDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
}
