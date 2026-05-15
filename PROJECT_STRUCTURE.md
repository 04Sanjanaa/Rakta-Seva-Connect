# Project Structure - Rakta-Seva Connect

This document provides an overview of the directory structure and the purpose of each package in the Rakta-Seva Connect project.

## Root Directory Structure
- `app/`: Main Android application module.
- `functions/`: Firebase Cloud Functions for backend logic (e.g., notifications).
- `docs/`: Documentation assets, including images and diagrams.
- `gradle/`: Gradle wrapper and configuration.

## App Module Structure (`com.raktaseva.app`)

### 1. `data/`
Contains implementation details of data fetching and persistence.
- `repository/`: Concrete implementations of repository interfaces defined in the domain layer.
  - `AuthRepositoryImpl.kt`: Firebase Auth implementation.
  - `DonorRepositoryImpl.kt`: Firebase Firestore implementation.

### 2. `domain/`
The core business logic layer, independent of any framework.
- `models/`: Data models used throughout the app.
  - `User.kt`: Donor profile model.
  - `BloodRequest.kt`: Emergency request model.
- `repository/`: Interfaces for data operations, ensuring abstraction.
  - `AuthRepository.kt`: Auth interface.
  - `DonorRepository.kt`: Donor data interface.

### 3. `presentation/`
The UI layer, responsible for displaying data and handling user interaction.
- `navigation/`: Navigation graph and route definitions.
  - `NavGraph.kt`: Main navigation host.
- `ui/`: UI components and styling.
  - `screens/`: Individual screens built with Jetpack Compose.
    - `auth/`: Login and Profile Setup screens.
    - `dashboard/`: Main dashboard for viewing requests.
    - `request/`: Screen for creating new blood requests.
    - `assistant/`: GenAI assistant chat screen.
  - `theme/`: Material 3 theme definitions.

### 4. `di/`
Dependency Injection modules.
- `AppModule.kt`: Provides singleton instances of Firebase, Repositories, etc.

### 5. `utils/`
Common utility classes and constants.
- `Constants.kt`: App-wide constants.
- `Resource.kt`: Wrapper for handling loading, success, and error states.

### 6. `MainActivity.kt`
The entry point of the application, hosting the `NavGraph`.

### 7. `RaktaSevaApplication.kt`
Custom Application class for Hilt initialization.
