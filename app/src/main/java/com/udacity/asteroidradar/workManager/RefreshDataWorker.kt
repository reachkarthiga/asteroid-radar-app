package com.udacity.asteroidradar.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.Repository
import com.udacity.asteroidradar.roomDataBase.Database

class RefreshDataWorker(private val context: Context, params : WorkerParameters) : CoroutineWorker(context, params){

    companion object  {
        const val WORK_NAME = "asteroid"
    }

    override suspend fun doWork(): Result {

        val database = Database.getInstance(context = context)
        val repository = Repository(database.dao)

        return try {
            repository.getAsteroidDetails()
            Result.success()
        } catch (e :Exception) {
            Result.retry()
        }

    }
}