package com.databuddy.vicky.repository

import com.databuddy.vicky.data.*
import kotlinx.coroutines.flow.Flow
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
     * Calculate monthly data budget based on remaining data and remaining time
     * remaining data ÷ remaining months = budget per month
     */
    fun calculateMonthlyBudget(config: PlanConfig): Double {
        val endDate = LocalDate.parse(config.billingEndDate)
        val today = LocalDate.now()
        
        // Calculate months remaining (including current month)
        val monthsRemaining = ChronoUnit.MONTHS.between(
            YearMonth.from(today),
            YearMonth.from(endDate)
        ) + 1 // +1 to include current month
        
        return if (monthsRemaining > 0) {
            config.currentRemainingGB.toDouble() / monthsRemaining
        } else {
            config.currentRemainingGB.toDouble()
        }
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
