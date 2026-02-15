package com.ftc.databuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Configuration for the user's data plan
 * Stores total plan size, billing cycle, and current state
 */
@Entity(tableName = "plan_config")
data class PlanConfig(
    @PrimaryKey
    val id: Int = 1, // Single row config
    val totalDataGB: Double,        // e.g., 300.0 GB or 14.0 GB
    val currentRemainingGB: Double, // Remaining data at the START of lastUpdatedMonth (auto-calculated to include current month usage)
    val billingStartDate: String,   // e.g., "2025-11-01"
    val billingEndDate: String,     // e.g., "2026-11-01"
    val userName: String = "",      // e.g., "Vicky" for personalization
    val helperName: String = "",    // e.g., "Larry" for friendly messages (blank = generic)
    val lastUpdated: String = LocalDate.now().toString(),
    val lastUpdatedYear: Int = LocalDate.now().year,   // Year when currentRemainingGB was set
    val lastUpdatedMonth: Int = LocalDate.now().monthValue  // Month when currentRemainingGB was set
)
