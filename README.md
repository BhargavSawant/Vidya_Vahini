```markdown
# VidyaVahini 🚌

VidyaVahini is a real-time school bus tracking application built with Jetpack Compose. It leverages Firebase and OpenStreetMap to provide live location updates, route visualization, and estimated time of arrival (ETA) for a safer and more predictable school commute.

## ✨ Features

* **Live Map Tracking:** Real-time vehicle positioning using osmdroid.
* **Intelligent Path Snapping:** Automatically draws route lines on actual roads using OSRM data (via osmbonuspack).
* **Journey Analytics:** Live calculation of ETA and total remaining distance.
* **Smart Timeline:** A vertical UI showing:
  * **Passed Stops:** With actual arrival times.
  * **Active/Next Stop:** Highlighting the immediate destination.
  * **Upcoming Stops:** With "minutes away" estimates.
* **Firebase Integration:** Synchronized real-time data handling for bus coordinates and trip status.
* **Modern UI:** Built entirely with Jetpack Compose and Material 3 design principles.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **Framework:** Jetpack Compose (Material 3)
* **Database:** Firebase Realtime Database
* **Maps:** Osmdroid (OpenStreetMap) & OSM Bonus Pack
* **Navigation:** Compose Navigation
* **Architecture:** MVVM (Model-View-ViewModel)

## 🚀 Getting Started

### Prerequisites

* **Android Studio:** Ladybug (2024.2.1) or newer.
* **JDK:** 17 or higher.
* **Android Device/Emulator:** API Level 24 (Nougat) or higher.

### Step 1: Clone the Repository

```bash
git clone [https://github.com/your-username/VidyaVahini.git](https://github.com/your-username/VidyaVahini.git)
cd VidyaVahini
```

### Step 2: Firebase Setup (Crucial)

Since `google-services.json` is ignored for security, you must add your own:

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project named **VidyaVahini**.
3. Add an Android App using the package name found in your `app/build.gradle`.
4. Download the `google-services.json` file.
5. Move the file into the `app/` directory: `app/google-services.json`

### Step 3: Firebase Database Rules

Enable Realtime Database and set the rules to allow reading for testing:

```json
{
  "rules": {
    ".read": true,
    ".write": "auth != null" 
  }
}
```

### Step 4: Build and Run

1. Open the project in Android Studio.
2. Wait for the Gradle sync to finish.
3. Click **Run 'app'** to install on your device.

## 📦 Key Dependencies

This project uses the following major libraries:

* **Firebase BOM:** 32.8.0 (Database)
* **Osmdroid:** 6.1.18 (Map Rendering)
* **OSM Bonus Pack:** 6.9.0 (Routing/Polyline)
* **Compose Material 3:** 2024.09.00
* **Navigation Compose:** 2.7.7

## 📁 Project Structure
```text
app/
├── src/main/java/.../vidyavahini/
│   ├── ui/                # Compose UI Screens & Components
│   ├── viewmodel/         # LiveData & Firebase Logic
│   ├── model/             # Data classes (Route, Stop, Vehicle)
│   └── navigation/        # App Navigation Graph
├── google-services.json   # Required for Firebase (Local only)
└── build.gradle           # Dependencies and Config
```

## ⚠️ Important Notes

* **Permissions:** Ensure the app has Location and Internet permissions granted on the device.
* **OSRM Usage:** The app uses public OSRM servers for routing. For heavy production use, it is recommended to set up a private OSRM endpoint.
* **Security:** Never commit your `google-services.json` or `firebase-key.json` to public repositories (these are already added to `.gitignore`).

## 🤝 Contributing

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/NewFeature`)
3. Commit your Changes (`git commit -m 'Add some NewFeature'`)
4. Push to the Branch (`git push origin feature/NewFeature`)
5. Open a Pull Request
```
