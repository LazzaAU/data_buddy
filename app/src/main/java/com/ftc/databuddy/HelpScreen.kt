package com.ftc.databuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Title
        Text(
            text = "📖 How Data Buddy Works",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // What is Data Buddy
        HelpSection(
            title = "What is Data Buddy?",
            content = "Data Buddy helps you stay on track with your mobile data plan. It's designed for anyone who wants simple, friendly reminders about their data usage - no tech knowledge needed!\n\n" +
                    "The app calculates how much data you can use each month based on what you have left in your plan, then tells you if you're on track, ahead, or need to slow down a bit."
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Settings Explained
        HelpSection(
            title = "Setting Up",
            content = "Here's what each setting means:"
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingExplanation(
            label = "User Name (Optional)",
            explanation = "The person who will use this app. Makes messages more personal!"
        )
        
        SettingExplanation(
            label = "Helper Name (Optional)",
            explanation = "The name of the person who set this up or helps with the tech stuff. EG: The user is your grandmother and the helper person is the grandson Dave, you would enter Dave in this field, that way personalised messages would show similair to 'hey Sue, dave says your data plan looks great this month, good job'."
        )
        
        SettingExplanation(
            label = "Total Data Plan (GB)",
            explanation = "How many gigabytes (GB) are in the mobile plan for the whole year. For example, if it's a 300GB yearly plan, enter 300."
        )
        
        SettingExplanation(
            label = "Plan Start Date",
            explanation = "When the yearly plan started. This is usually when the plan was purchased or renewed."
        )
        
        SettingExplanation(
            label = "Plan End Date",
            explanation = "When the yearly plan finishes. Usually one year after the start date."
        )
        
        SettingExplanation(
            label = "Current Remaining Data (GB)",
            explanation = "How many GB are left RIGHT NOW in the plan. Check the phone provider's app or website to verify accuracy periodically."
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // How it works
        HelpSection(
            title = "How Data Buddy Calculates",
            content = "1️⃣ Looks at how much data is left in the plan\n\n" +
                    "2️⃣ Counts how many months are left until the plan ends\n\n" +
                    "3️⃣ Divides the remaining data by remaining months to get a monthly budget\n\n" +
                    "4️⃣ Tracks actual usage each month and compares it to the budget\n\n" +
                    "5️⃣ Tells you if you're doing great, on track, or should ease up a bit"
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Color meanings
        HelpSection(
            title = "What the Colors Mean",
            content = "🟢 Green = You're using way less than your budget - stream away!\n\n" +
                    "🔵 Blue = You're right on track - keep doing what you're doing!\n\n" +
                    "🟠 Orange = You're a bit over budget this month - maybe ease up a little\n\n" +
                    "🔴 Red = You're using quite a bit more than budget - time to be careful"
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Example
        HelpSection(
            title = "Example Setup",
            content = "User Name: Vicky\n" +
                    "Helper Name: Larry\n" +
                    "Total Plan: 300 GB\n" +
                    "Start Date: 1-Nov-2025\n" +
                    "End Date: 31-Oct-2026\n" +
                    "Current Remaining: 196 GB\n\n" +
                    "Data Buddy will divide 196 GB by the months remaining and create a monthly budget. Then it tracks how much data Vicky actually uses and gives friendly feedback!"
        )
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun HelpSection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = content,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
fun SettingExplanation(
    label: String,
    explanation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = label,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = explanation,
                fontSize = 17.sp,
                lineHeight = 24.sp,
                color = Color(0xFF424242)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}
