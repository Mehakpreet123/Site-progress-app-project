# Site Progress App

## Overview
The **Site Progress App** is an Android application designed to track construction site progress. Built using **Android Studio** and **Firebase**, it allows users to update daily progress, view analytics, and manage project schedules efficiently.

## Features
- **Daily Progress Updates**: Users can upload progress reports with images and descriptions.
- **Firebase Integration**: Uses Firebase for real-time data storage and authentication.
- **Analytics Dashboard**: Provides insights on project progress with charts and reports.
- **Project Scheduling**: Helps manage construction timelines and deadlines.
- **User Authentication**: Secure login using Firebase Authentication.
- **Offline Mode**: Allows data entry even without an internet connection, syncing later when online.

## Tech Stack
- **Frontend**: Android (Kotlin/Java, XML UI)
- **Backend**: Firebase Firestore (NoSQL Database), Firebase Storage
- **Authentication**: Firebase Auth (Email/Google Sign-In)
- **UI Components**: Material Design, RecyclerView, Charts

## Installation & Setup
1. **Clone the Repository**:
   ```sh
   git clone https://github.com/yourusername/site-progress-app.git
   cd site-progress-app
   ```
2. **Open in Android Studio**:
   - Open **Android Studio** and select "Open an Existing Project".
   - Navigate to the project folder and open it.
3. **Connect Firebase**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project and add an Android app.
   - Download `google-services.json` and place it in `app/` directory.
   - Enable Firestore Database and Authentication in Firebase.
4. **Build & Run the App**:
   - Sync Gradle and click **Run** to launch the app.

## Firebase Configuration
Ensure the following Firebase services are enabled:
- **Firebase Firestore** (for storing progress updates)
- **Firebase Authentication** (for user login/sign-up)
- **Firebase Storage** (for uploading images)

## Folder Structure
```
app/
├── src/main/java/com/example/siteprogress
│   ├── activities/        # All Activities
│   ├── adapters/         # RecyclerView Adapters
│   ├── models/           # Data Models
│   ├── utils/            # Utility Classes
│   ├── firebase/         # Firebase Helpers
│   ├── MainActivity.kt   # Main Entry Point
│
├── src/main/res/layout/   # XML Layout Files
├── src/main/res/drawable/ # Icons & Images
├── google-services.json   # Firebase Config
```

## Future Enhancements
- **Push Notifications** for progress updates
- **Multi-user Role Management** (Admin, Site Engineer, Worker)
- **AI-based Predictions** for project completion time

## Contributors
- **Mehakpreet kaur** (https://github.com/Mehakpreet123)



