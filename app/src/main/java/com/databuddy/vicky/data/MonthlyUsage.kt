package com.databuddy.vicky.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tracks actual data usage for each month
 * This is populated by reading Android's NetworkStatsManager
 */
@Entity(tableName = "monthly_usage")
data class MonthlyUsage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val year: Int,              // e.g., 2026
    val month: Int,             // 1-12
    val dataUsedGB: Double,     // Actual usage for that month
    val recordedDate: String    // When this was recorded
)
