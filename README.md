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
The app requires permission to read network usage stats. On first run, you'll need to:
1. Go to Settings > Apps > Data Buddy > Permissions
2. Enable "Usage access" permission

## Current Status

This is **Version 1.0** - a foundation build with:
- ✅ Complete UI framework with Jetpack Compose
- ✅ "Larry says..." messaging system
- ✅ Sample data display (currently using mock data)
- ⏳ TODO: Integrate with Android's NetworkStatsManager for real data
- ⏳ TODO: Implement November-to-November billing cycle tracking
- ⏳ TODO: Add data persistence (SharedPreferences or Room DB)
- ⏳ TODO: Create app icon and launcher graphics

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
