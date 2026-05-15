# Rakta-Seva Connect
### Real-Time Emergency Blood Donor Platform

Rakta-Seva Connect is a life-saving mobile application designed to bridge the gap between blood donors and recipients in real-time. Built with modern Android technologies, it provides a seamless and efficient workflow for managing emergency blood requirements.

---

## Project Overview
The application enables users to post emergency blood requests with specific details such as blood group, units required, hospital location, and urgency levels. Nearby donors are notified (planned), and the dashboard provides a real-time view of active requests, ensuring that help reaches those in need as quickly as possible.

---

## Features
- **OTP Authentication**: Secure login using Firebase Phone Authentication.
- **Firebase Firestore Integration**: Real-time data synchronization for user profiles and blood requests.
- **Real-time Blood Requests**: Instant broadcasting of emergency requirements.
- **Dashboard**: A comprehensive view of all active emergency requests.
- **Compose UI**: Modern, responsive, and beautiful UI built with Jetpack Compose.
- **MVVM Architecture**: Clean separation of concerns for better maintainability.

---

## Technologies Used

### Frontend:
- **Kotlin**: The primary language for modern Android development.
- **Jetpack Compose**: Android's modern toolkit for building native UI.
- **Material 3**: The latest version of Google's open-source design system.

### Backend:
- **Firebase Authentication**: Handles secure, phone-based identity verification.
- **Firebase Firestore**: A flexible, scalable NoSQL cloud database for real-time data storage.

### Architecture:
- **MVVM (Model-View-ViewModel)**: Decouples business logic from UI.
- **Navigation Compose**: Handles in-app navigation with ease.
- **Repository Pattern**: Centralizes data access logic.

### Tools:
- **Android Studio**: The official IDE for Android development.
- **GitHub**: Version control and project collaboration.
- **Firebase Console**: Managing backend services and analytics.

---

## Architecture Overview
The project follows the **MVVM (Model-View-ViewModel)** architecture pattern. This ensures that the UI (View) is independent of the data handling logic (ViewModel), while the Repository layer manages data operations from Firebase. This approach makes the codebase scalable, testable, and robust.

---

## Screenshots

### Login Screen
![Login Screen](docs/images/login_screen.png)

### OTP Verification
![OTP Verification](docs/images/otp_verification_v2.png)

### Dashboard
![Dashboard](docs/images/dashboard_screen.png)

### Request Blood Screen
![Request Blood Screen](docs/images/request_blood_screen.png)

---

## Installation Steps
1. **Clone Repository**:
   ```bash
   git clone https://github.com/04Sanjanaa/Rakta-Seva-Connect.git
   ```
2. **Open in Android Studio**:
   Import the project as a Gradle project.
3. **Add google-services.json**:
   Download your `google-services.json` from the Firebase Console and place it in the `app/` directory.
4. **Sync Gradle**:
   Click on 'Sync Project with Gradle Files' and wait for completion.
5. **Run App**:
   Click the 'Run' button to launch the application on an emulator or physical device.

---

## Firebase Setup
- **Enable Phone Authentication**: In the Firebase Console, go to Authentication > Sign-in method and enable 'Phone'.
- **Add SHA-1 Fingerprint**: Generate your SHA-1 using `./gradlew signingReport` and add it to your Firebase Project Settings.
- **Configure Firestore**: Create a Firestore database and ensure the collections `users` and `blood_requests` are set up with appropriate security rules.

---

## Future Scope
- **Maps Integration**: Visualizing donor locations and hospital routes.
- **Push Notifications**: Instant alerts for donors when a request is posted in their vicinity.
- **AI Assistant**: Enhanced GenAI capabilities for donation eligibility checks.

---

## Author
**SANJANAA P**

---
*Developed for internship evaluation and professional portfolio showcase.*
