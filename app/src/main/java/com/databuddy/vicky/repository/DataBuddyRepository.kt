package com.databuddy.vicky.repository

import com.databuddy.vicky.data.*
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
     * Calculate actual remaining data by subtracting all usage since setup
     */
    suspend fun calculateActualRemainingData(config: PlanConfig): Double {
        val allUsage = dao.getAllMonthlyUsage().first()
        val totalUsed = allUsage.sumOf { it.dataUsedGB }
        return config.currentRemainingGB - totalUsed
    }
    
    /**
     * Calculate monthly data budget based on ACTUAL remaining data and remaining time
     * (actual remaining data after usage) ÷ remaining months = budget per month
     */
    suspend fun calculateMonthlyBudget(config: PlanConfig): Double {
        val endDate = LocalDate.parse(config.billingEndDate)
        val today = LocalDate.now()
        
        // Calculate months remaining (including current month)
        val monthsRemaining = ChronoUnit.MONTHS.between(
            YearMonth.from(today),
            YearMonth.from(endDate)
        ) + 1 // +1 to include current month
        
        // Get actual remaining data (initial - all usage)
        val actualRemaining = calculateActualRemainingData(config)
        
        return if (monthsRemaining > 0) {
            actualRemaining / monthsRemaining
        } else {
            actualRemaining
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
