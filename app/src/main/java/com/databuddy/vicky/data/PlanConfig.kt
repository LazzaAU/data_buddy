package com.databuddy.vicky.data

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
    val totalDataGB: Int,           // e.g., 300 GB
    val currentRemainingGB: Int,    // e.g., 196 GB (what user has left NOW)
    val billingStartDate: String,   // e.g., "2025-11-01"
    val billingEndDate: String,     // e.g., "2026-11-01"
    val lastUpdated: String = LocalDate.now().toString()
)
