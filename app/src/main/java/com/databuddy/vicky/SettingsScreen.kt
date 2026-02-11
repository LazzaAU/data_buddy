package com.databuddy.vicky

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.databuddy.vicky.data.PlanConfig
import java.time.Instant
import java.time.LocalDate
iOptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentConfig: PlanConfig?,
    onSaveConfig: (PlanConfig) -> Unit,
    onBack: () -> Unit
) {
    // Aussie date formatter: 11-Nov-2026
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH) }
    
    var totalDataGB by remember { mutableStateOf(currentConfig?.totalDataGB?.toString() ?: "300") }
    var remainingDataGB by remember { mutableStateOf(currentConfig?.currentRemainingGB?.toString() ?: "196") }
    
    // Parse existing dates or use defaults
    val defaultStartDate = try {
        currentConfig?.billingStartDate?.let { LocalDate.parse(it) } ?: LocalDate.of(2025, 11, 1)
    } catch (e: Exception) {
        LocalDate.of(2025, 11, 1)
    }
    
    val defaultEndDate = try {
        currentConfig?.billingEndDate?.let { LocalDate.parse(it) } ?: LocalDate.of(2026, 11, 1)
    } catch (e: Exception) {
        LocalDate.of(2026, 11, 1)
    }
    
    var startDate by remember { mutableStateOf(defaultStartDate) }
    var endDate by remember { mutableStateOf(defaultEndDate) }
    
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    var totalDataGB by remember { mutableStateOf(currentConfig?.totalDataGB?.toString() ?: "300") }
    var remainingDataGB by remember { mutableStateOf(currentConfig?.currentRemainingGB?.toString() ?: "196") }
    var startDate by remember { mutableStateOf(currentConfig?.billingStartDate ?: "2025-11-01") }
    var endDate by remember { mutableStateOf(currentConfig?.billingEndDate ?: "2026-11-01") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "⚙️ Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Instructions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Text(
                text = "📋 Enter your data plan details",
                fontS Picker Button
        OutlinedButton(
            onClick = { showStartDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Plan Start Date",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = startDate.format(dateFormatter),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        // End Date Picker Button
        OutlinedButton(
            onClick = { showEndDatePicker = true.toString(),
                    billingEndDate = endDate.toString(),
                    lastUpdated = LocalDate.now().toString()
                )
                onSaveConfig(config)
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "💾 Save Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Cancel Button
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
    
    // Start Date Picker Dialog
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        startDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showStartDatePicker = false
                }) {
                    Text("OK", fontSize = 18.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel", fontSize = 18.sp)
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }
    
    // End Date Picker Dialog
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        endDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showEndDatePicker = false
                }) {
                    Text("OK", fontSize = 18.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel", fontSize = 18.sp)
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
            onValueChange = { startDate = it },
            label = { Text("Plan Start Date", fontSize = 16.sp) },
            placeholder = { Text("YYYY-MM-DD", fontSize = 14.sp) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth()
        )
        
        // End Date
        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Plan End Date", fontSize = 16.sp) },
            placeholder = { Text("YYYY-MM-DD", fontSize = 14.sp) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Save Button
        Button(
            onClick = {
                val config = PlanConfig(
                    totalDataGB = totalDataGB.toIntOrNull() ?: 300,
                    currentRemainingGB = remainingDataGB.toIntOrNull() ?: 196,
                    billingStartDate = startDate,
                    billingEndDate = endDate,
                    lastUpdated = LocalDate.now().toString()
                )
                onSaveConfig(config)
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "💾 Save Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Cancel Button
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
