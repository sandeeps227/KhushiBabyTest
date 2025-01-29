package com.example.khushibabytest.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.khushibabytest.data.local.PatientDao
import com.example.khushibabytest.data.local.PatientDatabase
import com.example.khushibabytest.data.remote.VisitApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): PatientDatabase {
        return Room
            .databaseBuilder(appContext, PatientDatabase::class.java, "patient_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePatientDao(database: PatientDatabase): PatientDao {
        return database.patientDao()
    }


    @Provides
    @Singleton
    fun provideVisitApiService(): VisitApiService =
        Retrofit.Builder()
            .baseUrl("https://foo.bar")
            .build().create(VisitApiService::class.java)


    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}
