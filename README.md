# FastBite 🍔

A modern Android food ordering application built with **Kotlin** and **Jetpack Compose**, featuring a multi-role platform for customers, restaurants, and delivery drivers.

## 📱 Features

### User Roles
- **Customers**: Browse restaurants, place orders, track deliveries
- **Restaurants**: Manage menu, receive and process orders
- **Drivers**: Accept delivery requests, update order status

### Core Features
- 🔐 Authentication & Role Selection
- 🎨 Modern Material Design 3 UI with Jetpack Compose
- 🗺️ Real-time order tracking
- 💬 In-app chat functionality
- 📱 Responsive navigation with bottom navigation bar
- 🌐 Backend powered by Supabase

## 🛠️ Tech Stack

### Frontend
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Navigation Compose
- **Architecture**: MVVM pattern

### Backend & Services
- **Backend-as-a-Service**: Supabase
  - PostgreSQL database (PostgREST)
  - Authentication
  - Realtime subscriptions
  - Storage

### Networking
- **HTTP Client**: Ktor
- **Serialization**: kotlinx.serialization

### Dependencies
- AndroidX Core KTX
- AndroidX Lifecycle (ViewModel, Runtime)
- AndroidX Activity Compose
- Material 3 Components
- Material Icons Extended

## 📋 Requirements

- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Compile SDK**: 36
- **Java Compatibility**: Java 11

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Gradle 8.x

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FastBite
   ```

2. **Configure Supabase**
   - Create a Supabase project at [supabase.com](https://supabase.com)
   - Update the Supabase configuration in `SupabaseClient.kt` with your:
     - Project URL
     - Anonymous API Key

3. **Sync Gradle**
   ```bash
   ./gradlew sync
   ```

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open the project in Android Studio and run on an emulator/device.

## 📁 Project Structure

```
app/
├── src/main/java/com/saas/fastbite/
│   ├── data/
│   │   ├── model/          # Data models
│   │   ├── remote/         # Supabase client configuration
│   │   └── repository/     # Data repositories
│   ├── navigation/         # Navigation graph
│   ├── screens/
│   │   ├── auth/           # Authentication screens
│   │   ├── customer/       # Customer-specific screens
│   │   ├── restaurant/     # Restaurant dashboard
│   │   ├── driver/         # Driver screens
│   │   ├── shared/         # Shared components (Chat, Scaffold, Nav)
│   │   └── onboarding/     # Onboarding flow
│   └── ui/theme/           # Theme, Colors, Typography
└── build.gradle.kts
```

## 🎨 Screens

- **Onboarding**: Introduction to the app
- **Authentication**: Login/Signup with role selection
- **Customer Dashboard**: Browse restaurants and place orders
- **Restaurant Dashboard**: Manage orders and menu
- **Driver Dashboard**: View and accept delivery requests
- **Chat Screen**: Real-time messaging between users
- **Bottom Navigation**: Easy navigation between main sections

## 🔧 Configuration

### Supabase Setup

Update `app/src/main/java/com/saas/fastbite/data/remote/SupabaseClient.kt`:

```kotlin
val supabase = SupabaseClient {
    supabaseUrl = "YOUR_SUPABASE_URL"
    supabaseKey = "YOUR_SUPABASE_ANON_KEY"
}
```

### Build Configuration

Key configurations in `build.gradle.kts`:
- Compose enabled with BOM
- ProGuard rules for release builds
- Multiple Supabase modules (Auth, PostgREST, Realtime, Storage)

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## 📦 Building for Release

```bash
./gradlew assembleRelease
```

The APK will be generated at `app/build/outputs/apk/release/`.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Supabase](https://supabase.com)
- [Ktor](https://ktor.io)
- [Material Design 3](https://m3.material.io)

## 📞 Support

For support, please open an issue in the repository or contact the development team.

---

**Built with ❤️ using Kotlin and Jetpack Compose**
