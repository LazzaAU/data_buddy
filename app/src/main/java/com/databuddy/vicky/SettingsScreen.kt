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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.databuddy.vicky.data.PlanConfig
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "⚙️ Settings",
            fontSize = 28.sp,
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Total Data
        OutlinedTextField(
            value = totalDataGB,
            onValueChange = { totalDataGB = it },
            label = { Text("Total Data (GB)", fontSize = 16.sp) },
            placeholder = { Text("e.g., 300", fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Remaining Data
        OutlinedTextField(
            value = remainingDataGB,
            onValueChange = { remainingDataGB = it },
            label = { Text("Current Remaining (GB)", fontSize = 16.sp) },
            placeholder = { Text("e.g., 196", fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Start Date Picker Button
        OutlinedButton(
            onClick = { showStartDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Plan Start Date",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = startDate.format(dateFormatter),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        // End Date Picker Button
        OutlinedButton(
            onClick = { showEndDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Plan End Date",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = endDate.format(dateFormatter),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Save Button
        Button(
            onClick = {
                val config = PlanConfig(
                    totalDataGB = totalDataGB.toIntOrNull() ?: 300,
                    currentRemainingGB = remainingDataGB.toIntOrNull() ?: 196,
                    billingStartDate = startDate.toString(),
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel")
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }
}
