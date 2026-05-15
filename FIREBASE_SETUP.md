# Firebase Setup Guide - Rakta-Seva Connect

This guide outlines the steps required to configure Firebase for the Rakta-Seva Connect application.

## 1. Create a Firebase Project
- Go to the [Firebase Console](https://console.firebase.google.com/).
- Click **Add project** and follow the setup wizard.

## 2. Register Android App
- Click the **Android icon** to add an app.
- Package Name: `com.raktaseva.app`
- App Nickname: `Rakta-Seva Connect`
- **SHA-1 Fingerprint**: (Crucial for Phone Auth)
  - Run `./gradlew signingReport` in the terminal to get your debug SHA-1.
- Download `google-services.json` and place it in the `app/` folder.

## 3. Enable Authentication
- Navigate to **Authentication** -> **Get Started**.
- Go to the **Sign-in method** tab.
- Click **Add new provider** and select **Phone**.
- Enable it and save.

## 4. Set Up Firestore Database
- Navigate to **Firestore Database** -> **Create database**.
- Select **Start in test mode** for initial development.
- Choose a location near your users.
- Create the following collections:
  - `users`: Stores user profiles.
  - `blood_requests`: Stores emergency requests.

## 5. Firebase Cloud Messaging (Optional)
- FCM is used for push notifications.
- Enable the **Cloud Messaging API** in the Google Cloud Console.

## 6. (Optional) Deploy Cloud Functions
- If you use the `functions` folder, install Firebase CLI:
  ```bash
  npm install -g firebase-tools
  ```
- Login and initialize:
  ```bash
  firebase login
  firebase init functions
  ```
- Deploy:
  ```bash
  firebase deploy --only functions
  ```

## 7. Troubleshooting
- **reCAPTCHA error**: Ensure the SHA-1 is correct and the Android Device Check API is enabled in Google Cloud Console.
- **Permission Denied**: Check your Firestore security rules.
