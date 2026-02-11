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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            DataBuddyScreen(
                viewModel = viewModel,
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                currentConfig = null, // Will be loaded from DB in a real implementation
                onSaveConfig = { config ->
                    viewModel.savePlanConfig(config)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
