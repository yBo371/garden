<div align="center">

[简体中文](./README.md) · **English**

</div>

# Garden App

Garden App is an Android garden management demo built with **Kotlin**, **Jetpack Compose**, and the **MVVM architecture**. The app revolves around garden plots, market purchases, inventory management, flower catalog, and personal achievements. Currently, the data is mainly driven by in-memory state and static repository data.

## Features

- **Garden Page**: Displays 6 garden plots and allows users to select tools to water, remove pests, speed up, and collect crops.  
- **Planting Process**: Click an empty plot to select seeds from the inventory. Crops enter a growth countdown with state prompts for watering or harvesting.  
- **Market Page**: Browse seeds and gardening tools by category, purchase unlocked items with coins, and update the inventory after purchase.  
- **Profile Page**: Shows user information, flower catalog, inventory items, and achievement tasks.  
- **Shared State**: Coins, tool selection, plot state, inventory, and message notifications are maintained in `GameStore`.

## Technology Stack

- Kotlin  
- Android Gradle Plugin 8.13.0  
- Gradle Wrapper 8.13  
- Jetpack Compose + Material 3  
- AndroidX Lifecycle / ViewModel / Navigation Compose  
- Coil Compose for loading remote images  
- Java 21 / Kotlin JVM target 21

## Project Structure

```
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/gardenapp/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── GameStore.kt
│       │   │   └── GardenRepository.kt
│       │   ├── model/
│       │   │   └── Models.kt
│       │   ├── ui/
│       │   │   ├── GardenApp.kt
│       │   │   ├── components/
│       │   │   ├── navigation/
│       │   │   ├── screens/
│       │   │   └── theme/
│       │   └── viewmodel/
│       └── res/
├── design.html
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew.bat
```

- `design.html` is the visual reference prototype.  
- `app/build/`, `.gradle/`, `.idea/`, etc., are build or IDE artifacts and should not be maintained as business code.  
- Currently, there is no dedicated `app/src/test/` or `app/src/androidTest/` test directory submitted.

## Requirements

- Android Studio or compatible Android SDK  
- JDK 21  
- Windows users can run `gradlew.bat` directly from the repository  
- Network access is required to Maven mirrors and image CDN; the app needs `INTERNET` permission to load remote images  

Gradle and dependency repositories are configured with mirrors:  
- Gradle Wrapper uses Tencent Cloud Gradle mirror  
- Maven repositories prioritize Alibaba Cloud mirror, with `google()` and `mavenCentral()` as backup

## Build and Run

From the project root directory:

```
.\gradlew.bat assembleDebug
```

Install to a connected device or emulator:

```
.\gradlew.bat installDebug
```

Run local unit tests:

```
.\gradlew.bat testDebugUnitTest
```

Run Android instrumentation tests:

```
.\gradlew.bat connectedDebugAndroidTest
```

Clean build artifacts:

```
.\gradlew.bat clean
```

If using Android Studio, it is recommended to **Sync Project with Gradle Files** before building or running.

## Key Code Explanation

- `MainActivity.kt`: App entry point, sets up edge-to-edge and mounts the root Compose component.  
- `ui/GardenApp.kt`: Main app framework, includes bottom navigation and entry to three pages.  
- `ui/screens/GardenScreen.kt`: Garden page, handles plot display, toolbar, and seed selection dialog.  
- `ui/screens/MarketScreen.kt`: Market page, handles item categories, coins display, and purchases.  
- `ui/screens/ProfileScreen.kt`: Profile page, shows user info, catalog, inventory, and achievements.  
- `data/GameStore.kt`: Global game state, includes planting, watering, pest removal, speeding up, harvesting, purchase, and countdown refresh logic.  
- `data/GardenRepository.kt`: Static initial data, including plots, tools, market items, catalog, and user info.  
- `viewmodel/`: Converts `GameStore` and static data to UI-consumable states for each page.

## Current Limitations

- Game state is only stored in memory and resets to default on app restart  
- Market, catalog, profile, and image URLs are currently static  
- Tool items are added to inventory after purchase, but planting only consumes seeds  
- No automated tests have been submitted yet

## Development Suggestions

- Add new business states to `data/GameStore.kt` or a separate repository, and expose to UI via ViewModel  
- Keep Composables focused on UI; complex state flows should be in ViewModel or data layer  
- When adding tests, start with `GameStore` state transitions such as purchasing, planting, watering, speeding up, and harvesting  
- Do not commit `local.properties`, `.idea/`, `.gradle/`, or `app/build/` artifacts
