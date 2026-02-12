package com.ftc.databuddy.data

import androidx.room.Entity

/**
 * Tracks actual data usage for each month
 * This is populated by reading Android's NetworkStatsManager
 * Uses composite primary key of year+month to prevent duplicates
 */
@Entity(
    tableName = "monthly_usage",
    primaryKeys = ["year", "month"]
)
data class MonthlyUsage(
    val year: Int,              // e.g., 2026
    val month: Int,             // 1-12
    val dataUsedGB: Double,     // Actual usage for that month
    val recordedDate: String    // When this was recorded
)
