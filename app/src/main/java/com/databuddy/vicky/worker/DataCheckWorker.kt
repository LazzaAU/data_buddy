package com.databuddy.vicky.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.databuddy.vicky.data.DataBuddyDatabase
import com.databuddy.vicky.repository.DataBuddyRepository
import com.databuddy.vicky.util.DataUsageReader
import com.databuddy.vicky.util.NotificationHelper
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import kotlin.math.roundToInt

class DataCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val database = DataBuddyDatabase.getDatabase(context)
    private val repository = DataBuddyRepository(database.dataBuddyDao())
    private val dataUsageReader = DataUsageReader(context)
    
    override suspend fun doWork(): Result {
        return try {
            // Get config
            val config = repository.planConfig.first() ?: return Result.success()
            
            // Get current month usage
            val currentUsage = repository.getCurrentMonthUsage()?.dataUsedGB ?: 0.0
            
            // Calculate budget
            val monthlyBudget = repository.calculateMonthlyBudget(config)
            val actualRemaining = repository.calculateActualRemainingData(config)
            
            val today = LocalDate.now()
            val dayOfMonth = today.dayOfMonth
            val daysInMonth = today.lengthOfMonth()
            
            // Check 1: Mid-month pace check (on 14th or 15th)
            if (dayOfMonth in 14..15) {
                val expectedUsageAtMidMonth = monthlyBudget * (dayOfMonth.toDouble() / daysInMonth)
                if (currentUsage > expectedUsageAtMidMonth * 1.3) { // 30% over pace
                    NotificationHelper.sendNotification(
                        applicationContext,
                        "📊 Larry says: Watch your pace!",
                        "You've used ${currentUsage.roundToInt()}GB so far this month. At this rate, you might go over your ${monthlyBudget.roundToInt()}GB budget!",
                        NotificationHelper.NOTIFICATION_MID_MONTH_OVERUSE
                    )
                }
            }
            
            // Check 2: 90% of monthly budget used
            val percentUsed = (currentUsage / monthlyBudget) * 100
            if (percentUsed >= 90 && percentUsed < 100) {
                NotificationHelper.sendNotification(
                    applicationContext,
                    "⚠️ Larry says: Nearly at your limit!",
                    "You've used ${currentUsage.roundToInt()}GB of your ${monthlyBudget.roundToInt()}GB monthly budget. Maybe ease up a bit!",
                    NotificationHelper.NOTIFICATION_90_PERCENT_USED
                )
            }
            
            // Check 3: Running low on TOTAL remaining data (< 20% left)
            val percentOfTotalRemaining = (actualRemaining / config.totalDataGB) * 100
            if (percentOfTotalRemaining < 20 && percentOfTotalRemaining > 10) {
                NotificationHelper.sendNotification(
                    applicationContext,
                    "🚨 Larry says: Getting low overall!",
                    "You only have ${actualRemaining.roundToInt()}GB left until November. Time to be more careful with streaming!",
                    NotificationHelper.NOTIFICATION_TOTAL_DATA_LOW
                )
            }
            
            // Check 4: End of month good job (last day of month, under budget)
            if (dayOfMonth == daysInMonth && currentUsage < monthlyBudget) {
                val savedData = monthlyBudget - currentUsage
                NotificationHelper.sendNotification(
                    applicationContext,
                    "🎉 Larry says: Great job this month!",
                    "You saved ${savedData.roundToInt()}GB by staying under budget! Keep it up!",
                    NotificationHelper.NOTIFICATION_MONTH_END_GOOD
                )
            }
            
            // Check 5: Over 100% of monthly budget
            if (percentUsed >= 100) {
                val overAmount = currentUsage - monthlyBudget
                NotificationHelper.sendNotification(
                    applicationContext,
                    "🔴 Larry says: You're over budget!",
                    "You've used ${overAmount.roundToInt()}GB more than your ${monthlyBudget.roundToInt()}GB budget. Try to use less next month to balance out!",
                    NotificationHelper.NOTIFICATION_90_PERCENT_USED
                )
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
