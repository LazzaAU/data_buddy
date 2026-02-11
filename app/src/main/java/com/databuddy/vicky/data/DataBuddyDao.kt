package com.databuddy.vicky.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBuddyDao {
    
    // Plan Configuration
    @Query("SELECT * FROM plan_config WHERE id = 1")
    fun getPlanConfig(): Flow<PlanConfig?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanConfig(config: PlanConfig)
    
    @Query("DELETE FROM plan_config")
    suspend fun deletePlanConfig()
    
    // Monthly Usage
    @Query("SELECT * FROM monthly_usage ORDER BY year DESC, month DESC")
    fun getAllMonthlyUsage(): Flow<List<MonthlyUsage>>
    
    @Query("SELECT * FROM monthly_usage WHERE year = :year AND month = :month LIMIT 1")
    suspend fun getUsageForMonth(year: Int, month: Int): MonthlyUsage?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyUsage(usage: MonthlyUsage)
    
    @Query("SELECT * FROM monthly_usage ORDER BY year DESC, month DESC LIMIT 2")
    fun getLastTwoMonths(): Flow<List<MonthlyUsage>>
}
