package com.databuddy.vicky

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

data class DataBuddyUiState(
    val lastMonthUsageGB: Int = 10,
    val currentMonthUsageGB: Int = 5,
    val totalDataGB: Int = 300,
    val remainingDataGB: Int = 285,
    val remainingStreamingHours: Int = 95,
    val larryMessage: String = "You've used 10 GB last month and only 5 GB this month. You have plenty of data to continue streaming!"
)

class DataBuddyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DataBuddyUiState())
    val uiState: StateFlow<DataBuddyUiState> = _uiState.asStateFlow()
    
    init {
        updateLarryMessage()
    }
    
    private fun updateLarryMessage() {
        val state = _uiState.value
        val percentageUsed = ((state.currentMonthUsageGB.toFloat() / state.totalDataGB) * 100).roundToInt()
        
        val message = when {
            percentageUsed < 10 -> 
                "You've used ${state.lastMonthUsageGB} GB last month and only ${state.currentMonthUsageGB} GB this month. You have plenty of data to continue streaming!"
            
            percentageUsed < 30 -> 
                "You're doing great! You've used ${state.currentMonthUsageGB} GB this month. Keep enjoying your shows, Vicky!"
            
            percentageUsed < 60 -> 
                "You've used ${state.currentMonthUsageGB} GB so far. You still have ${state.remainingDataGB} GB left - more than enough for your Netflix!"
            
            percentageUsed < 80 -> 
                "You've used ${state.currentMonthUsageGB} GB this month. You still have ${state.remainingDataGB} GB remaining. No need to worry!"
            
            else -> 
                "You've used ${state.currentMonthUsageGB} GB. You have ${state.remainingDataGB} GB left. Just keep an eye on it, but you should be fine!"
        }
        
        _uiState.value = state.copy(larryMessage = message)
    }
    
    // Method to calculate streaming hours (assuming 3GB per hour for HD Netflix)
    private fun calculateStreamingHours(dataGB: Int): Int {
        return (dataGB / 3.0).roundToInt()
    }
}
