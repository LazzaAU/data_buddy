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
- Track data usage across a 300GB annual plan (November to November billing cycle)
- Display monthly data consumption in user-friendly terms
- Convert data usage to streaming hours (Netflix-focused)
- "Larry says..." conversational feedback system
- Simple, large-text UI optimized for elderly users

## Development Guidelines
- Keep UI extremely simple with large fonts and high contrast
- Use friendly, reassuring language in all messages
- Avoid technical jargon
- Test on real devices for accessibility
- Consider voice feedback for future iterations

## Coding Standards
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Keep business logic in ViewModels
- Use StateFlow for reactive UI updates
