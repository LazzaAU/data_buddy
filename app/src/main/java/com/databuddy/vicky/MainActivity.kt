package com.databuddy.vicky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.databuddy.vicky.ui.theme.DataBuddyTheme
import com.databuddy.vicky.util.NotificationHelper
import com.databuddy.vicky.util.WorkManagerHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup notifications
        NotificationHelper.createNotificationChannel(this)
        WorkManagerHelper.scheduleDailyDataCheck(this)
        
        setContent {
            DataBuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataBuddyApp()
                }
            }
        }
    }
}

@Composable
fun DataBuddyApp() {
    val navController = rememberNavController()
    val viewModel: DataBuddyViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentConfig by viewModel.currentConfig.collectAsState()
    val hasPermission = viewModel.hasUsagePermission()
    
    NavHost(
        navController = navController,
        startDestination = if (hasPermission) "main" else "permission"
    ) {
        composable("permission") {
            PermissionRequestScreen(
                onPermissionGranted = {
                    if (viewModel.hasUsagePermission()) {
                        navController.navigate("main") {
                            popUpTo("permission") { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable("main") {
            DataBuddyScreen(
                viewModel = viewModel,
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                currentConfig = currentConfig,
                onSaveConfig = { config ->
                    viewModel.savePlanConfig(config)
                },
                onBack = { navController.popBackStack() },
                onNavigateToHelp = { navController.navigate("help") }
            )
        }
        
        composable("help") {
            HelpScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
