package com.databuddy.vicky

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.databuddy.vicky.data.*
import com.databuddy.vicky.repository.DataBuddyRepository
import com.databuddy.vicky.util.DataUsageReader
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

data class DataBuddyUiState(
    val isConfigured: Boolean = false,
    val monthlyBudgetGB: Double = 0.0,
    val lastMonthUsageGB: Double = 0.0,
    val currentMonthUsageGB: Double = 0.0,
    val totalDataGB: Int = 300,
    val remainingDataGB: Int = 0,
    val larryMessage: String = "Welcome! Let's set up your data plan first.",
    val usageStatus: UsageStatus = UsageStatus.UNKNOWN,
    val userName: String = "",
    val helperName: String = ""
)

enum class UsageStatus {
    WELL_UNDER,    // Using way less than budget
    ON_TRACK,      // Around budget  
    SLIGHTLY_OVER, // A bit over budget
    OVER_BUDGET,   // Significantly over
    UNKNOWN
}

class DataBuddyViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DataBuddyDatabase.getDatabase(application)
    private val repository = DataBuddyRepository(database.dataBuddyDao())
    private val dataUsageReader = DataUsageReader(application)
    
    private val _uiState = MutableStateFlow(DataBuddyUiState())
    val uiState: StateFlow<DataBuddyUiState> = _uiState.asStateFlow()
    
    init {
        observeDataChanges()
        // Sync real usage data when app starts
        syncCurrentMonthUsage()
    }
    
    /**
     * Read current month's actual usage from Android and save to database
     */
    private fun syncCurrentMonthUsage() {
        viewModelScope.launch {
            if (dataUsageReader.hasUsageStatsPermission()) {
                val currentUsage = dataUsageReader.getCurrentMonthDataUsage()
                if (currentUsage > 0) {
                    updateCurrentMonthUsage(currentUsage)
                }
            }
        }
    }
    
    fun hasUsagePermission(): Boolean {
        return dataUsageReader.hasUsageStatsPermission()
    }
    
    private fun observeDataChanges() {
        viewModelScope.launch {
            combine(
                repository.planConfig,
                repository.lastTwoMonths
            ) { config, recentUsage ->
                if (config != null) {
                    updateUiState(config, recentUsage)
                } else {
                    _uiState.value = DataBuddyUiState(isConfigured = false)
                }
            }.collect()
        }
    }
    
    private suspend fun updateUiState(config: PlanConfig, recentUsage: List<MonthlyUsage>) {
        Log.d("DataBuddy", "=== UPDATE UI STATE ===")
        Log.d("DataBuddy", "Config: totalGB=${config.totalDataGB}, currentRemaining=${config.currentRemainingGB}")
        Log.d("DataBuddy", "Recent usage count: ${recentUsage.size}")
        recentUsage.forEachIndexed { index, usage ->
            Log.d("DataBuddy", "  [$index] ${usage.year}-${usage.month}: ${usage.dataUsedGB}GB")
        }
        
        val monthlyBudget = repository.calculateMonthlyBudget(config)
        val currentMonth = recentUsage.firstOrNull()
        val lastMonth = recentUsage.getOrNull(1)
        
        val currentUsage = currentMonth?.dataUsedGB ?: 0.0
        val previousUsage = lastMonth?.dataUsedGB ?: 0.0
        
        // Calculate actual remaining data (initial - all usage)
        val actualRemaining = repository.calculateActualRemainingData(config)
        Log.d("DataBuddy", "Monthly budget: $monthlyBudget GB")
        Log.d("DataBuddy", "Actual remaining: $actualRemaining GB (should be ${config.currentRemainingGB} - total_usage)")
        
        val status = calculateUsageStatus(currentUsage, monthlyBudget)
        val message = generateMessage(
            status = status,
            currentUsage = currentUsage,
            lastMonthUsage = previousUsage,
            monthlyBudget = monthlyBudget,
            remainingData = actualRemaining.toInt(),
            userName = config.userName,
            helperName = config.helperName
        )
        
        _uiState.value = DataBuddyUiState(
            isConfigured = true,
            monthlyBudgetGB = monthlyBudget,
            lastMonthUsageGB = previousUsage,
            currentMonthUsageGB = currentUsage,
            totalDataGB = config.totalDataGB,
            remainingDataGB = actualRemaining.toInt(),
            larryMessage = message,
            usageStatus = status,
            userName = config.userName,
            helperName = config.helperName
        )
    }
    
    private fun calculateUsageStatus(currentUsage: Double, budget: Double): UsageStatus {
        val percentageOfBudget = if (budget > 0) (currentUsage / budget) * 100 else 0.0
        
        return when {
            percentageOfBudget < 50 -> UsageStatus.WELL_UNDER
            percentageOfBudget < 90 -> UsageStatus.ON_TRACK
            percentageOfBudget < 110 -> UsageStatus.SLIGHTLY_OVER
            else -> UsageStatus.OVER_BUDGET
        }
    }
    
    /**
     * Generate personalized or generic message based on helper name
     */
    private fun generateMessage(
        status: UsageStatus,
        currentUsage: Double,
        lastMonthUsage: Double,
        monthlyBudget: Double,
        remainingData: Int,
        userName: String,
        helperName: String
    ): String {
        val currentUsageInt = currentUsage.roundToInt()
        val lastMonthUsageInt = lastMonthUsage.roundToInt()
        val budgetInt = monthlyBudget.roundToInt()
        
        val userGreeting = if (userName.isNotBlank()) "$userName, " else ""
        val hasHelper = helperName.isNotBlank()
        
        return when (status) {
            UsageStatus.WELL_UNDER -> {
                if (hasHelper) {
                    if (lastMonthUsage > 0) {
                        "🎉 Hey $userGreeting$helperName here! You used ${lastMonthUsageInt}GB last month and only ${currentUsageInt}GB this month. You're crushing it - stream away!"
                    } else {
                        "🎉 $userGreeting$helperName's impressed! You've only used ${currentUsageInt}GB this month of your monthly ${budgetInt}GB budget with ${remainingData}GB left. Stream till you fall asleep!💤 😏"
                    }
                } else {
                    if (lastMonthUsage > 0) {
                        "✅ Great work! You used ${lastMonthUsageInt}GB last month and ${currentUsageInt}GB this month. You have plenty of data remaining."
                    } else {
                        "✅ Excellent! You've used ${currentUsageInt}GB this month with ${remainingData}GB remaining. You're well under budget."
                    }
                }
            }
            
            UsageStatus.ON_TRACK -> {
                if (hasHelper) {
                    "👍 ${userGreeting}you're doing great! $helperName says you've used ${currentUsageInt}GB this month, right on track with your ${budgetInt}GB budget. Keep it up!"
                } else {
                    "📊 On track! You've used ${currentUsageInt}GB this month, matching your ${budgetInt}GB monthly budget well."
                }
            }
            
            UsageStatus.SLIGHTLY_OVER -> {
                if (hasHelper) {
                    "⚠️ Hey $userGreeting$helperName thinks you've used ${currentUsageInt}GB this month (budget is ${budgetInt}GB). Maybe ease up a little to stay balanced!"
                } else {
                    "⚠️ Usage alert: You've used ${currentUsageInt}GB this month against a ${budgetInt}GB budget. Consider reducing usage slightly."
                }
            }
            
            UsageStatus.OVER_BUDGET -> {
                if (hasHelper) {
                    "🚨 Heads up ${userGreeting}! $helperName noticed you've used ${currentUsageInt}GB when your budget is ${budgetInt}GB. Might want to watch it a bit next month!"
                } else {
                    "🚨 Over budget: You've used ${currentUsageInt}GB this month against a ${budgetInt}GB budget. Try to reduce usage next month."
                }
            }
            
            UsageStatus.UNKNOWN -> {
                if (hasHelper && userName.isNotBlank()) {
                    "👋 Hi $userName! $helperName here. Let's set up your data plan so I can help you track your usage!"
                } else {
                    "Welcome! Set up your data plan details to start tracking your usage."
                }
            }
        }
    }
    
    fun savePlanConfig(config: PlanConfig) {
        viewModelScope.launch {
            repository.savePlanConfig(config)
        }
    }
    
    // Expose the current config as a Flow
    val currentConfig: StateFlow<PlanConfig?> = repository.planConfig.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    fun updateCurrentMonthUsage(dataUsedGB: Double) {
        viewModelScope.launch {
            val now = LocalDate.now()
            val usage = MonthlyUsage(
                year = now.year,
                month = now.monthValue,
                dataUsedGB = dataUsedGB,
                recordedDate = now.toString()
            )
            repository.saveMonthlyUsage(usage)
        }
    }
}
