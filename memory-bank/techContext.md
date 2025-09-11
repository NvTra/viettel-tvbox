# σ₃: Technical Context

_v1.0 | Created: Thursday, September 11, 2025 | Updated: Thursday, September 11, 2025_
_Π: 🏗️DEVELOPMENT | Ω: 🔍R_

## 🛠️ Technology Stack

- 🖥️ **Frontend**: Kotlin + Jetpack Compose
- 📱 **Platform**: Android TV (API 23-36)
- 🏗️ **Architecture**: MVVM với Compose
- 🌐 **Networking**: Retrofit + OkHttp 5.1.0
- 🎬 **Media**: ExoPlayer (Media3)
- 🎨 **UI Framework**: TV Material Design 3

## 📦 Key Dependencies

```kotlin
// Core Android
- androidx.core:core-ktx: 1.17.0
- androidx.appcompat: 1.6.1
- androidx.compose.bom: 2024.09.00

// TV Specific
- androidx.tv:tv-foundation: 1.0.0-alpha07
- androidx.tv:tv-material: 1.0.0-alpha07
- androidx.leanback: 1.2.0-alpha02

// Media & Streaming
- androidx.media3:media3-exoplayer: 1.8.0
- androidx.media3:media3-ui: 1.8.0

// Networking
- com.squareup.okhttp3:okhttp: 5.1.0
- com.squareup.retrofit2:retrofit: 3.0.0
- com.squareup.retrofit2:converter-gson: 2.9.0

// UI & Images
- io.coil-kt:coil-compose: 2.7.0
- com.github.bumptech.glide:glide: 4.12.0
- androidx.navigation:navigation-compose: 2.7.7

// WebView & Gaming
- com.google.accompanist:accompanist-webview: 0.36.0
- com.google.android.gms:play-services-auth: 21.4.0
```

## 🏗️ Build Configuration

- **Gradle**: 8.12.1
- **Kotlin**: 2.2.0
- **AGP**: 8.12.1
- **Compile SDK**: 36
- **Java Version**: 11

## 🔧 Environment Setup

```kotlin
// Build config fields từ .env
- API_BASE_URL: Base URL cho API calls
- IMAGE_URL: URL cho image resources
- VIDEO_URL: URL cho video streaming
- BLACKNUT_URL: Blacknut gaming platform URL
- BLACKNUT_IMAGE_URL: Blacknut image resources
```

## 📁 Project Structure

```
app/
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/viettel/tvbox/
│   │   ├── MainActivity.kt          # Entry point
│   │   ├── MyApp.kt                 # Application class
│   │   ├── navigation/              # Navigation logic
│   │   ├── screens/                 # UI screens
│   │   │   ├── auths/              # Authentication
│   │   │   ├── home/               # Home & games
│   │   │   ├── category/           # Categories
│   │   │   ├── search/             # Search functionality
│   │   │   ├── promotion/          # Promotions
│   │   │   ├── my_list/            # User lists
│   │   │   └── layouts/            # Layout components
│   │   ├── services/               # API services
│   │   ├── theme/                  # UI theming
│   │   └── widgets/                # Reusable components
│   └── res/                        # Resources
```

## 🔐 Security & Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<uses-feature android:name="android.hardware.touchscreen" android:required="false" />
<uses-feature android:name="android.software.leanback" android:required="false" />
```

## 🌐 API Integration

- **Retrofit Configuration**: Centralized trong RetrofitInstance
- **OkHttp**: Version 5.1.0 với conflict resolution
- **Gson Converter**: JSON serialization/deserialization
- **Environment Variables**: Dynamic API URLs từ .env file

## 📱 TV Optimization

- **Leanback Support**: Android TV launcher integration
- **Focus Management**: TV remote navigation
- **Large Screen UI**: Optimized layouts cho TV
- **Performance**: Media playback optimization
