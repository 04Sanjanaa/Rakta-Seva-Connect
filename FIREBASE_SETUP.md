# Firebase Setup Guide - Rakta-Seva Connect

This document provides a step-by-step guide to configuring the Firebase backend for the Rakta-Seva Connect application.

## 1. Firebase Project Initialization
- Create a new project in the [Firebase Console](https://console.firebase.google.com/).
- Add an Android app to the project using the package name: `com.raktaseva.app`.
- Download the `google-services.json` file and place it in the `app/` directory of your project.

## 2. Authentication Configuration
### Phone Authentication
- In the Firebase Console, navigate to **Authentication** > **Sign-in method**.
- Enable the **Phone** provider.
- (Optional) Add test phone numbers for development.

### SHA-1 Fingerprint
- To enable Phone Auth, you must provide your app's SHA-1 fingerprint.
- Run the following command in the Android Studio terminal:
  ```bash
  ./gradlew signingReport
  ```
- Copy the SHA-1 from the output and add it to your Project Settings in the Firebase Console.

## 3. Cloud Firestore Setup
- Navigate to **Firestore Database** and click **Create database**.
- Start in **Test Mode** for initial development or configure proper security rules for production.
- **Collections**:
    - `users`: Created automatically when a user saves their profile.
    - `blood_requests`: Created when a user broadcasts an emergency request.

## 4. Troubleshooting
- **reCAPTCHA Verification**: Phone authentication may trigger reCAPTCHA. Ensure your Google Play Services are up to date on the device/emulator.
- **Missing google-services.json**: If the app crashes on startup, ensure the JSON file is correctly placed and the Gradle plugin is applied.
