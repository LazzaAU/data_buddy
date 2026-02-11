# Data Buddy - Android App Project

## Project Overview
Data Buddy is an Android application designed to help elderly, non-technical users (specifically Vicky) track their mobile data usage in a friendly, conversational manner. The app uses a character named "Larry" to communicate data usage statistics in plain, reassuring language.

## Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModels
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## Key Features
- **Settings/Configuration Screen**: User enters total plan size, billing cycle dates, and current remaining data
- **Smart Budget Calculator**: Divides remaining data by remaining months to calculate monthly budget
- **Real Data Tracking**: Uses Android NetworkStatsManager + Room Database to track actual monthly usage
- **Monthly Comparison**: Compares actual usage vs calculated budget to determine if user is on track
- **Larry Says Messaging**: Context-aware friendly messages (no tech jargon):
  - Under budget: "You've got heaps left this month, stream away!"
  - On track: "You're doing great, keep it up!"
  - Over budget: "Maybe ease up a bit this month to balance things out"
- **Simple, large-text UI** optimized for elderly users (24sp minimum)
- **Future: Generic version** with configurable personas (remove "Larry" for universal appeal)

## Development Guidelines
- **Keep UI extremely simple** with large fonts (24sp+) and high contrast
- **Use friendly, reassuring language** in all messages - NO technical jargon
- **The message is king** - any visualizations (charts, etc.) are secondary to clear, human-friendly text
- **Vicky-specific design philosophy**: 
  - She knows more than she lets on, but tech isn't her thing
  - She plays the "helpless woman" card to get help
  - The app should reduce "Larry fatigue" from constant reassurance calls
  - Messages should be definitive and confident so she trusts them
- **Test on Samsung S20 FE** (Vicky's phone)
- **Future considerations**: 
  - Make it configurable for universal use
  - Remove "Larry" persona for generic version
  - Keep core logic the same, just swap messaging

## Coding Standards
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Keep business logic in ViewModels
- Use StateFlow for reactive UI updates
