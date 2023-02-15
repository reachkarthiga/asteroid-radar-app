package com.udacity.asteroidradar.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.Repository
import com.udacity.asteroidradar.roomDataBase.Database

class RefreshDataWorker(private val context: Context, params : WorkerParameters) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {

        val database = Database.getInstance(context = context)
        val repository = Repository(database.dao, database.pictureDao)

        return try {
            repository.getAsteroidDetails()
            Result.success()
        } catch (e :Exception) {
            Result.retry()
        }

    }
}