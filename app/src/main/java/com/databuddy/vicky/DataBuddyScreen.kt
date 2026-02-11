package com.databuddy.vicky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun DataBuddyScreen(
    viewModel: DataBuddyViewModel,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    if (!uiState.isConfigured) {
        // Show welcome screen with setup button
        WelcomeScreen(onNavigateToSettings = onNavigateToSettings)
    } else {
        // Show main dashboard
        MainDashboard(uiState = uiState, onNavigateToSettings = onNavigateToSettings)
    }
}

@Composable
fun WelcomeScreen(onNavigateToSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "👋",
            fontSize = 60.sp
        )
        
        Text(
            text = "Welcome to Data Buddy!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            textAlign = TextAlign.Center
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Larry says:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Let's get started by setting up your data plan details!",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
            }
        }
        
        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Set Up Now",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainDashboard(
    uiState: DataBuddyUiState,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Settings icon at top right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF1976D2)
                )
            }
        }
        
        // App title centered
        Text(
            text = "📱 Data Buddy",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        
        // Larry's Message Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (uiState.usageStatus) {
                    UsageStatus.WELL_UNDER -> Color(0xFF4CAF50)
                    UsageStatus.ON_TRACK -> Color(0xFF2196F3)
                    UsageStatus.SLIGHTLY_OVER -> Color(0xFFFF9800)
                    UsageStatus.OVER_BUDGET -> Color(0xFFF44336)
                    else -> Color(0xFF2196F3)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👋 Larry says:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = uiState.larryMessage,
                    fontSize = 19.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }
        
        // Budget vs Usage Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DataCard(
                title = "Monthly Budget",
                value = "${String.format("%.1f", uiState.monthlyBudgetGB)} GB",
                subtitle = "per month",
                modifier = Modifier.weight(1f),
                color = Color(0xFF2196F3)
            )
            
            DataCard(
                title = "This Month",
                value = "${String.format("%.1f", uiState.currentMonthUsageGB)} GB",
                subtitle = "used so far",
                modifier = Modifier.weight(1f),
                color = when (uiState.usageStatus) {
                    UsageStatus.WELL_UNDER -> Color(0xFF4CAF50)
                    UsageStatus.ON_TRACK -> Color(0xFF2196F3)
                    UsageStatus.SLIGHTLY_OVER, UsageStatus.OVER_BUDGET -> Color(0xFFFF9800)
                    else -> Color(0xFF4CAF50)
                }
            )
        }
        
        // Last Month Usage
        if (uiState.lastMonthUsageGB > 0) {
            DataCard(
                title = "Last Month",
                value = "${String.format("%.1f", uiState.lastMonthUsageGB)} GB",
                subtitle = "for comparison",
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF9E9E9E)
            )
        }
        
        // Remaining Data Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.remainingDataGB > 100) Color(0xFF4CAF50)
                else if (uiState.remainingDataGB > 50) Color(0xFFFF9800)
                else Color(0xFFF44336)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Remaining",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${String.format("%.1f", uiState.remainingDataGB.toDouble())} GB",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "of ${uiState.totalDataGB} GB plan",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

        }
        Text(
            text = "GB stands for Giga Byte",
            fontSize = 10.sp,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DataCard(
    title: String,
    value: String,
    subtitle: String = "",
    modifier: Modifier = Modifier,
    color: Color = Color.Blue
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
