# Project Structure - Rakta-Seva Connect

This document outlines the organized folder and package hierarchy of the Rakta-Seva Connect project, following the MVVM Clean Architecture.

## Directory Overview

### Root Folders
- `app/`: The primary Android application module.
- `docs/`: Contains project documentation and media assets (screenshots).
- `gradle/`: Gradle build system configuration.

### App Module Structure (`com.raktaseva.app`)

#### 1. `data/`
Responsible for data retrieval and persistence.
- **`repository/`**: Contains the concrete implementations of domain interfaces.
    - `AuthRepositoryImpl.kt`: Manages Firebase Phone Authentication logic.
    - `DonorRepositoryImpl.kt`: Handles Firestore CRUD operations for users and blood requests.

#### 2. `domain/`
The core business logic layer.
- **`models/`**: POJO (Plain Old Java Object) classes for data.
    - `User.kt`: Represents donor profile data.
    - `BloodRequest.kt`: Represents emergency blood request details.
- **`repository/`**: Abstraction interfaces for data operations.
    - `AuthRepository.kt`: Interface for authentication methods.
    - `DonorRepository.kt`: Interface for data management.

#### 3. `presentation/`
The UI layer using Jetpack Compose.
- **`navigation/`**: Centralized navigation management.
    - `NavGraph.kt`: Defines routes and screen transitions.
- **`ui/`**: Visual components.
    - **`screens/`**: Individual Composable screens.
        - `auth/`: Authentication flow screens (Login, Profile Setup).
        - `dashboard/`: Main application hub.
        - `request/`: Emergency request creation flow.
        - `assistant/`: AI Assistant integration.
    - **`theme/`**: Design tokens, colors, and typography (Material 3).

#### 4. `di/`
Dependency Injection management.
- `AppModule.kt`: Dagger Hilt module providing singleton dependencies (Firebase, Repositories).

#### 5. `utils/`
Common helpers and constants.
- `Constants.kt`: Storage for collection names and constant values.
- `Resource.kt`: Sealed class for handling Success, Error, and Loading states.

---

## Firestore Collections
The app relies on two primary collections:
1. `users`: Stores unique donor profiles linked by UID.
2. `blood_requests`: Stores all emergency requests with metadata (status, urgency, timestamp).

## Navigation Flow
`LoginScreen` -> `OTP Verification` -> `ProfileSetup` (if new user) -> `Dashboard` -> `RequestBloodScreen`
