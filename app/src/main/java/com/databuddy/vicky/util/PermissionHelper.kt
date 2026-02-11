package com.databuddy.vicky.util

import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Helper class to manage permissions for data usage tracking
 */
object PermissionHelper {
    
    /**
     * Check if usage stats permission is granted
     */
    fun hasUsageStatsPermission(context: Context): Boolean {
        val dataUsageReader = DataUsageReader(context)
        return dataUsageReader.hasUsageStatsPermission()
    }
    
    /**
     * Open system settings to grant usage stats permission
     */
    fun requestUsageStatsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
