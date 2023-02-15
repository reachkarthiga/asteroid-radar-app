package com.udacity.asteroidradar.workManager

import android.app.Application
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class App :Application() {

    val scope = CoroutineScope(Dispatchers.Default)

    companion object  {
        const val WORK_NAME = "asteroid"
        const val INSTALLATION_LOADING = "firstTimeLoadingData"
    }

    override fun onCreate() {
        super.onCreate()
        callWorkManager()
    }

    private fun callWorkManager() {
        scope.launch {

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val request = PeriodicWorkRequestBuilder<RefreshDataWorker>( 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, request)

            val initialRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>().build()

            WorkManager.getInstance().enqueueUniqueWork(INSTALLATION_LOADING,
                ExistingWorkPolicy.KEEP, initialRequest)

        }


    }

}