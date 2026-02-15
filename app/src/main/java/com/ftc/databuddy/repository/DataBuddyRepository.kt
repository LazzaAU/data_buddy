package com.ftc.databuddy.repository

import com.ftc.databuddy.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

/**
 * Repository for managing data plan configuration and usage tracking
 */
class DataBuddyRepository(private val dao: DataBuddyDao) {
    
    // Plan Configuration
    val planConfig: Flow<PlanConfig?> = dao.getPlanConfig()
    
    suspend fun savePlanConfig(config: PlanConfig) {
        dao.insertPlanConfig(config)
    }
    
    // Monthly Usage
    val allMonthlyUsage: Flow<List<MonthlyUsage>> = dao.getAllMonthlyUsage()
    val lastTwoMonths: Flow<List<MonthlyUsage>> = dao.getLastTwoMonths()
    
    suspend fun saveMonthlyUsage(usage: MonthlyUsage) {
        dao.insertMonthlyUsage(usage)
    }
    
    suspend fun getUsageForMonth(year: Int, month: Int): MonthlyUsage? {
        return dao.getUsageForMonth(year, month)
    }
    
    /**
     * Check if month has changed since last config update, and roll over if needed.
     * Returns the updated config or null if no update needed.
     */
    suspend fun checkAndRolloverMonth(config: PlanConfig): PlanConfig? {
        val now = LocalDate.now()
        val currentYear = now.year
        val currentMonth = now.monthValue
        
        // Check if we're in a new month
        if (config.lastUpdatedYear == currentYear && config.lastUpdatedMonth == currentMonth) {
            // Same month, no rollover needed
            return null
        }
        
        // Month has changed! Get the previous month's TOTAL usage from database
        val previousMonthUsage = dao.getUsageForMonth(config.lastUpdatedYear, config.lastUpdatedMonth)
        val usageToSubtract = previousMonthUsage?.dataUsedGB ?: 0.0
        
        android.util.Log.d("DataBuddy", "Month rollover detected: ${config.lastUpdatedYear}-${config.lastUpdatedMonth} -> $currentYear-$currentMonth")
        android.util.Log.d("DataBuddy", "Subtracting previous month usage: ${usageToSubtract}GB from ${config.currentRemainingGB}GB")
        
        // Calculate new remaining: old remaining - previous month's total usage
        val newRemaining = config.currentRemainingGB - usageToSubtract
        
        // Create updated config with new month and new remaining balance
        val updatedConfig = config.copy(
            currentRemainingGB = newRemaining,
            lastUpdatedYear = currentYear,
            lastUpdatedMonth = currentMonth,
            lastUpdated = now.toString()
        )
        
        // Save the updated config
        dao.insertPlanConfig(updatedConfig)
        
        android.util.Log.d("DataBuddy", "New remaining balance for $currentYear-$currentMonth: ${newRemaining}GB")
        
        return updatedConfig
    }
    
    /**
     * Calculate actual remaining data by subtracting current month's usage only.
     * config.currentRemainingGB represents what the user had at the START of the current month.
     */
    suspend fun calculateActualRemainingData(config: PlanConfig): Double {
        val now = LocalDate.now()
        val currentYear = now.year
        val currentMonth = now.monthValue
        
        // Get current month's usage
        val currentMonthUsage = dao.getUsageForMonth(currentYear, currentMonth)
        val currentUsed = currentMonthUsage?.dataUsedGB ?: 0.0
        
        val remaining = config.currentRemainingGB - currentUsed
        
        android.util.Log.d("DataBuddy", "calculateActualRemaining: ${config.currentRemainingGB}GB (month start) - ${currentUsed}GB (used this month) = ${remaining}GB")
        
        return remaining
    }
    
    /**
     * Calculate monthly data budget based on ACTUAL remaining data and remaining time
     * (actual remaining data after usage) ÷ remaining months = budget per month
     */
    suspend fun calculateMonthlyBudget(config: PlanConfig): Double {
        val endDate = LocalDate.parse(config.billingEndDate)
        val today = LocalDate.now()
        
        // Calculate months remaining from today to end date
        val monthsRemaining = ChronoUnit.MONTHS.between(
            YearMonth.from(today),
            YearMonth.from(endDate)
        )
        
        // Get actual remaining data (initial - all usage)
        val actualRemaining = calculateActualRemainingData(config)
        
        // Use at least 1 month to avoid division by zero
        val divisor = if (monthsRemaining > 0) monthsRemaining else 1
        
        return actualRemaining / divisor
    }
    
    /**
     * Get current month's usage
     */
    suspend fun getCurrentMonthUsage(): MonthlyUsage? {
        val now = LocalDate.now()
        return dao.getUsageForMonth(now.year, now.monthValue)
    }
    
    /**
     * Get previous month's usage
     */
    suspend fun getPreviousMonthUsage(): MonthlyUsage? {
        val previousMonth = LocalDate.now().minusMonths(1)
        return dao.getUsageForMonth(previousMonth.year, previousMonth.monthValue)
    }
}
