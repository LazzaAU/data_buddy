package com.ftc.databuddy.util

import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Process
import java.time.LocalDate
import java.time.ZoneId

/**
 * Helper class to read actual data usage from Android's NetworkStatsManager
 * Requires PACKAGE_USAGE_STATS permission
 */
class DataUsageReader(private val context: Context) {
    
    private val networkStatsManager: NetworkStatsManager by lazy {
        context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
    }
    
    /**
     * Check if the app has permission to read network stats
     */
    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    
    /**
     * Get total mobile data usage for a specific month
     * @param year Year (e.g., 2026)
     * @param month Month (1-12)
     * @return Data usage in GB
     */
    fun getMonthlyDataUsage(year: Int, month: Int): Double {
        if (!hasUsageStatsPermission()) {
            return 0.0
        }
        
        try {
            // Calculate start and end of month in milliseconds
            val startOfMonth = LocalDate.of(year, month, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            
            val endOfMonth = startOfMonth.let {
                LocalDate.of(year, month, 1)
                    .plusMonths(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
            
            // Query mobile data usage
            val bucket = networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,  // subscriberId - null means default
                startOfMonth,
                endOfMonth
            )
            
            val totalBytes = bucket.rxBytes + bucket.txBytes
            
            // Convert bytes to GB
            return totalBytes / (1024.0 * 1024.0 * 1024.0)
            
        } catch (e: Exception) {
            e.printStackTrace()
            return 0.0
        }
    }
    
    /**
     * Get current month's data usage
     */
    fun getCurrentMonthDataUsage(): Double {
        val now = LocalDate.now()
        return getMonthlyDataUsage(now.year, now.monthValue)
    }
    
    /**
     * Get data usage for the last X months including current month
     */
    fun getRecentMonthsUsage(numberOfMonths: Int): Map<Pair<Int, Int>, Double> {
        val result = mutableMapOf<Pair<Int, Int>, Double>()
        var currentDate = LocalDate.now()
        
        repeat(numberOfMonths) {
            val year = currentDate.year
            val month = currentDate.monthValue
            val usage = getMonthlyDataUsage(year, month)
            result[Pair(year, month)] = usage
            currentDate = currentDate.minusMonths(1)
        }
        
        return result
    }
}
