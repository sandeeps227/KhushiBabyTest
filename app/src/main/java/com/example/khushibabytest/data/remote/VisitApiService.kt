package com.example.khushibabytest.data.remote

import com.example.khushibabytest.data.local.entities.PatientVisit
import retrofit2.http.Body
import retrofit2.http.POST

interface VisitApiService {
    @POST("syncVisit")
    suspend fun syncVisit(@Body visit: PatientVisit)
}