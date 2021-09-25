@file:Suppress("unused")

package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.Constants.DELETE_ASTEROIDS_WORK_NAME
import com.udacity.asteroidradar.Constants.REFRESH_ASTEROIDS_WORK_NAME

import com.udacity.asteroidradar.work.AsteroidWork
import com.udacity.asteroidradar.work.DeleteAsteroidDataWork

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
// This code is attributed to nourishnew
///
//Asteroid-Radar-App-Udacity-project-2
class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<AsteroidWork>(
            // once a day
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            REFRESH_ASTEROIDS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // keep - will disregard the new request
            repeatingRefreshRequest
        )

        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<DeleteAsteroidDataWork>(
            // once a day
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DELETE_ASTEROIDS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // keep - will disregard the new request
            repeatingDeleteRequest
        )
    }
}