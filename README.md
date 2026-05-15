# RAKTA-SEVA CONNECT

**Real-time emergency blood donor Android application.**

Rakta-Seva Connect is a life-saving platform designed to bridge the gap between blood donors and recipients in real-time. Built with modern Android technologies, it provides a seamless experience for posting emergency blood requests and connecting with nearby donors.

## Features
- **OTP Authentication**: Secure login using Firebase Phone Authentication.
- **Blood Request Posting**: Quickly post emergency blood requirements with urgency levels (Normal, Urgent, Critical).
- **Dashboard**: Real-time view of active blood requests in the vicinity.
- **Firebase Firestore Integration**: Real-time synchronization of requests and user profiles.
- **AI Assistant**: Built-in GenAI assistant to help users with blood donation eligibility and general queries.

## Technologies Used
- **Kotlin**: Core programming language.
- **Jetpack Compose**: Modern toolkit for building native UI.
- **Firebase Authentication**: For secure phone-based login.
- **Firebase Firestore**: Real-time NoSQL database for requests and user data.
- **Hilt (Dagger)**: Dependency injection for clean and modular code.
- **Coroutines & Flow**: For asynchronous operations and reactive data streams.
- **MVVM Architecture**: Ensures separation of concerns and testability.

## Architecture
The project follows the **MVVM (Model-View-ViewModel)** architecture pattern, ensuring a clean separation between the UI and business logic.

```mermaid
graph TD
    subgraph Presentation
        UI[Compose Screens] --> VM[ViewModels]
    end
    subgraph Domain
        VM --> RI[Repository Interfaces]
        RI --> M[Models]
    end
    subgraph Data
        RI -.-> RE[Repository Impl]
        RE --> FB[Firebase Services]
    end
```

## Workflow Diagram

```mermaid
sequenceDiagram
    participant User
    participant UI as Compose Screen
    participant VM as ViewModel
    participant Repo as Repository
    participant FB as Firebase

    User->>UI: Request Blood
    UI->>VM: submitRequest()
    VM->>Repo: createBloodRequest()
    Repo->>FB: setDocument()
    FB-->>Repo: Success
    Repo-->>VM: Flow(Success)
    VM-->>UI: State Update
    UI-->>User: Show Success Snackbar
```

## Firebase Interaction Flow

```mermaid
graph LR
    App[Rakta-Seva App] --> Auth[Firebase Auth]
    App --> FS[Firestore Database]
    App --> FCM[Cloud Messaging]
    
    Auth -->|OTP| User[User Phone]
    FS -->|Data| Requests[Blood Requests]
    FS -->|Data| Profiles[User Profiles]
```

## Screenshots

### Login Screen
![Login Screen](docs/images/login_screen.png)

### OTP Verification
![OTP Verification](docs/images/otp_verification.png)

### Dashboard
![Dashboard](docs/images/dashboard_screen.png)

### Request Blood Screen
![Request Blood](docs/images/request_blood_screen.png)

## Installation Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/04Sanjanaa/Rakta-Seva-Connect.git
   ```
2. **Open in Android Studio**:
   Import the project as a Gradle project.
3. **Add google-services.json**:
   Download your `google-services.json` from Firebase Console and place it in the `app/` directory.
4. **Sync Gradle**:
   Wait for the project to download all dependencies and sync.
5. **Run app**:
   Connect a physical device or use an emulator and click 'Run'.

## Firebase Setup
1. **Enable Phone Authentication**:
   In Firebase Console -> Authentication -> Sign-in method -> Enable Phone.
2. **Add SHA-1 Fingerprint**:
   Add your debug and release SHA-1 fingerprints to the Firebase Project Settings.
3. **Firestore Setup**:
   Enable Cloud Firestore in Test Mode (or production with proper rules) and create the following collections:
   - `users`
   - `blood_requests`

## Future Enhancements
- **Maps Integration**: Real-time location tracking of donors.
- **Push Notifications**: Instant alerts for nearby emergency requests.
- **AI-Powered Matching**: Advanced algorithms to find the most suitable donors.

## Author
**SANJANAA P**

---
*Developed for professional project evaluation and portfolio showcase.*
