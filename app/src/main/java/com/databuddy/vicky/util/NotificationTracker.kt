package com.databuddy.vicky.util

import android.content.Context
import java.time.LocalDate

object NotificationTracker {
    
    private const val PREFS_NAME = "notification_tracker"
    private const val KEY_LAST_MONTH = "last_month"
    private const val KEY_MID_MONTH_SENT = "mid_month_sent"
    private const val KEY_90_PERCENT_SENT = "90_percent_sent"
    private const val KEY_LOW_DATA_SENT = "low_data_sent"
    private const val KEY_MONTH_END_SENT = "month_end_sent"
    private const val KEY_OVER_BUDGET_SENT = "over_budget_sent"
    
    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * Check if we're in a new month and reset flags if needed
     */
    private fun checkAndResetIfNewMonth(context: Context) {
        val prefs = getPrefs(context)
        val currentMonth = LocalDate.now().monthValue
        val lastMonth = prefs.getInt(KEY_LAST_MONTH, -1)
        
        if (currentMonth != lastMonth) {
            // New month - reset all flags
            prefs.edit().apply {
                putInt(KEY_LAST_MONTH, currentMonth)
                putBoolean(KEY_MID_MONTH_SENT, false)
                putBoolean(KEY_90_PERCENT_SENT, false)
                putBoolean(KEY_LOW_DATA_SENT, false)
                putBoolean(KEY_MONTH_END_SENT, false)
                putBoolean(KEY_OVER_BUDGET_SENT, false)
                apply()
            }
        }
    }
    
    /**
     * Check if a notification has been sent this month
     */
    fun hasBeenSent(context: Context, notificationType: String): Boolean {
        checkAndResetIfNewMonth(context)
        return getPrefs(context).getBoolean(notificationType, false)
    }
    
    /**
     * Mark a notification as sent for this month
     */
    fun markAsSent(context: Context, notificationType: String) {
        checkAndResetIfNewMonth(context)
        getPrefs(context).edit().putBoolean(notificationType, true).apply()
    }
    
    // Notification type constants
    const val TYPE_MID_MONTH = KEY_MID_MONTH_SENT
    const val TYPE_90_PERCENT = KEY_90_PERCENT_SENT
    const val TYPE_LOW_DATA = KEY_LOW_DATA_SENT
    const val TYPE_MONTH_END = KEY_MONTH_END_SENT
    const val TYPE_OVER_BUDGET = KEY_OVER_BUDGET_SENT
}
