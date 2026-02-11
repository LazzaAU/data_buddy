# Data Buddy 📱

An Android app designed to help Vicky (and eventually everyone!) track mobile data usage in a friendly, non-technical way.

## About

Data Buddy was created to solve a simple problem: helping elderly family members understand their data usage without the stress and confusion. The app features "Larry" - a friendly voice that explains data consumption in plain English, without any technical jargon.

### Key Features
- 📊 Simple data usage tracking (300GB annual plan, November to November)
- 💬 Friendly "Larry says..." messages that reassure rather than alarm
- 🎬 Netflix streaming hours calculator (because that's what matters!)
- 🎨 Large text and high-contrast UI designed for elderly users
- ✅ No confusing numbers or technical terms

## Technical Details

### Built With
- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with ViewModels and StateFlow
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

### Project Structure
```
app/src/main/java/com/databuddy/vicky/
├── MainActivity.kt          # App entry point
├── DataBuddyScreen.kt       # Main UI with Compose
├── DataBuddyViewModel.kt    # Business logic and state
└── ui/theme/                # Theme, colors, typography
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 8 or higher
- Android SDK with API level 34

### Building the Project
1. Open the project in Android Studio
2. Sync Gradle files (should happen automatically)
3. Build > Make Project (or Ctrl+F9)
4. Run on an emulator or physical device (API 26+)

### Running on a Device
The app requires permission to read network usage stats. On first run:
1. The app will show a friendly permission request screen
2. Tap "Grant Permission" button
3. Find "Data Buddy" in the system settings list
4. Toggle the switch ON
5. Return to the app

### First Time Setup
1. Grant the usage stats permission (see above)
2. Tap "Set Up Now" on the welcome screen
3. Enter your plan details:
   - **Total Plan Size**: e.g., 300 GB
   - **Data Remaining Now**: Check your carrier app (e.g., 196 GB)
   - **Plan Start Date**: Format YYYY-MM-DD (e.g., 2025-11-01)
   - **Plan End Date**: Format YYYY-MM-DD (e.g., 2026-11-01)
4. Tap "Save Settings"
5. The app will automatically track your usage and show Larry's message!

## Current Status

This is **Version 0.5** - Core features implemented! ✨

**Completed Features**:
- ✅ Room Database for tracking monthly usage and configuration
- ✅ Settings screen for plan configuration (total GB, remaining data, billing cycle)
- ✅ Smart budget calculator (remaining data ÷ remaining months)
- ✅ NetworkStatsManager integration for real Android data tracking
- ✅ Permission handling with user-friendly guidance
- ✅ Smart "Larry says" messaging that compares actual vs budget:
  - Well under budget: "Stream away!"
  - On track: "Keep it up!"
  - Slightly over: "Maybe ease up a bit"
  - Over budget: "Watch less next month!"
- ✅ Navigation between main screen and settings
- ✅ Large-text Vicky-friendly UI (24sp+ fonts)
- ✅ Color-coded cards based on usage status

**Ready to Test**: Load on a real Android device (API 26+) and test!

**Next Steps**:
- ⏳ Test on Samsung S20 FE (Vicky's device)
- ⏳ Add app icon and launcher graphics
- ⏳ Fine-tune messaging based on real-world usage
- ⏳ Optional: Add data visualization (pie chart)
- ⏳ Create generic version (configurable persona instead of "Larry")

## Design Philosophy

**Vicky-First Design**:
- Font sizes: Minimum 24sp, headings 32sp+
- Colors: High contrast, friendly blues and greens
- Language: No "MB", "GB/month", "bandwidth" - just simple streaming hours
- Feedback: Always reassuring, never alarming
- Layout: Spacious, no clutter, scrollable

## Future Enhancements
- Voice feedback for Larry's messages
- Widget for home screen
- Notification reminders if getting close to limit
- Multi-user support with different plans
- Dark mode for evening use

## Contributing

This started as a personal project for family, but if you have elderly relatives with similar needs, feel free to adapt it! The code is intentionally simple and well-commented.

## License

Created with ❤️ for Vicky (and Larry's peace of mind!)

---

**Note**: Currently uses sample data. Integration with Android's NetworkStatsManager will be added in the next iteration.
