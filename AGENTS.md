# Repository Guidelines

## Project Structure & Module Organization
This repository contains a single Android app module: `app/`. Kotlin sources live under `app/src/main/java/com/example/gardenapp/` and are organized by responsibility:

- `data/`: repositories and shared state (`GameStore.kt`)
- `model/`: UI and domain models
- `viewmodel/`: MVVM state holders
- `ui/screens/`: page-level Compose screens
- `ui/components/`: reusable Compose widgets
- `ui/theme/`: colors, typography, theme setup

Resources are in `app/src/main/res/`. The HTML prototype used as the visual reference is `design.html`.

## Build, Test, and Development Commands
Use the Gradle wrapper from the repo root:

- `.\gradlew.bat assembleDebug`: build the debug APK
- `.\gradlew.bat installDebug`: install to a connected device/emulator
- `.\gradlew.bat testDebugUnitTest`: run local unit tests
- `.\gradlew.bat connectedDebugAndroidTest`: run instrumentation tests
- `.\gradlew.bat clean`: clear build outputs

If Android Studio is available, prefer `Sync Project with Gradle Files` before running.

## Coding Style & Naming Conventions
Write new code in Kotlin using MVVM and Jetpack Compose. Follow 4-space indentation and keep files ASCII unless existing content already requires Unicode. Use:

- `PascalCase` for classes, composables, and view models
- `camelCase` for functions, properties, and state fields
- clear package grouping by feature or layer

Keep composables stateless where practical; move mutable UI logic into `viewmodel/` or shared state in `data/`.

## Testing Guidelines
There are currently no dedicated test source sets checked in. Add unit tests under `app/src/test/` and UI tests under `app/src/androidTest/`. Name test files after the target class, for example `GardenViewModelTest.kt`. Prefer testing state transitions such as purchase, planting, watering, and harvest flows.

## Commit & Pull Request Guidelines
Current history uses a simple style (`first commit`). Keep future commits short, imperative, and specific, for example:

- `Add warehouse inventory syncing`
- `Fix garden tool selection state`

For pull requests, include:

- a short summary of behavior changes
- screenshots or screen recordings for UI changes
- affected files or flows
- any manual verification steps

## Configuration Notes
Gradle and dependencies are configured with mirror repositories. Do not commit machine-specific changes from `local.properties`, `.idea/`, or generated build outputs.
