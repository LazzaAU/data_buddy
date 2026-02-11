package com.databuddy.vicky.util

import android.content.Context
import androidx.work.*
import com.databuddy.vicky.worker.DataCheckWorker
import java.util.concurrent.TimeUnit

object WorkManagerHelper {
    
    private const val DAILY_CHECK_WORK = "daily_data_check"
    
    fun scheduleDailyDataCheck(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DataCheckWorker>(
            1, TimeUnit.DAYS,
            30, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS) // First check after 1 hour
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_CHECK_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }
    
    fun cancelDailyDataCheck(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_CHECK_WORK)
    }
}
