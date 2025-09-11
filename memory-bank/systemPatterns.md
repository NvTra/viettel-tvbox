# σ₂: System Patterns

_v1.0 | Created: Thursday, September 11, 2025 | Updated: Thursday, September 11, 2025_
_Π: 🏗️DEVELOPMENT | Ω: 🔍R_

## 🏛️ Architecture Overview

**MVVM Pattern với Jetpack Compose** - Ứng dụng áp dụng kiến trúc MVVM hiện đại kết hợp với declarative UI của Compose, tối ưu cho Android TV.

## 🎭 Core Patterns

### 🚀 Application Lifecycle

```kotlin
MyApp (Application)
├── RetrofitInstance.init()     # API initialization
└── MainActivity
    ├── SplashScreen (3s)       # Branding & loading
    └── MainContent
        ├── LoginScreen         # Authentication gate
        └── AppNavGraph         # Main navigation
```

### 🔐 Authentication Flow

```kotlin
Authentication Pattern:
├── UserPreferences.isLogin()   # Check login state
├── LoginScreen                 # Credential input
├── API Authentication          # Server validation
├── Session Storage            # Persistent login
└── Logout Event Flow          # Clean logout
```

### 🧭 Navigation Architecture

```kotlin
Navigation Hierarchy:
AppNavGraph
├── "main" (startDestination)
│   ├── Sidebar Navigation
│   └── Content Area
├── Game Screens
│   ├── GameHomeScreen
│   ├── AllGameScreen
│   └── GameDetail
├── Category System
│   ├── CategoryScreen
│   └── CategoryDetailScreen
├── User Features
│   ├── SearchScreen
│   ├── MyListScreen
│   └── PromotionScreen
└── WebView Integration
    └── PlayBlacknutWebViewScreen
```

## 🏗️ Component Architecture

### 📱 Screen Components

```kotlin
Screen Pattern:
├── @Composable Screen Function
├── State Management (remember/collectAsState)
├── LaunchedEffect for side effects
├── UI Composition với TV Material3
└── Navigation callbacks
```

### 🎨 UI Component Hierarchy

```kotlin
VietteltvTheme
└── Surface (TV Material3)
    ├── SplashScreen (conditional)
    └── MainContent
        ├── LoginScreen | AppNavGraph
        └── ToastMessage.Show()
```

### 🔧 Service Layer

```kotlin
Service Architecture:
RetrofitInstance
├── OkHttp Client Configuration
├── Gson Converter Setup
├── Environment-based URLs
└── API Service Interfaces
```

## 🎯 Design Patterns Applied

### 🔄 State Management

- **Compose State**: `remember`, `mutableStateOf`
- **Flow-based**: `collectAsState()` cho reactive updates
- **Lifecycle Aware**: `LaunchedEffect` cho side effects
- **Persistent Storage**: UserPreferences với SharedPreferences

### 🏷️ Dependency Injection

- **Manual DI**: Singleton pattern cho services
- **Context Injection**: Application context cho services
- **Factory Pattern**: RetrofitInstance creation

### 🎭 UI Patterns

- **Declarative UI**: Jetpack Compose functions
- **State Hoisting**: Parent manages child state
- **Composition**: Reusable UI components
- **TV Navigation**: Focus-based navigation

## 🔄 Data Flow Patterns

### 📡 API Communication

```kotlin
Data Flow:
UI Layer → ViewModel/Repository → RetrofitService → API
         ← (Response)            ← (JSON)        ←
```

### 💾 State Persistence

```kotlin
User State:
UserPreferences ↔ SharedPreferences ↔ Device Storage
              ↓
            Flow Events ← UI Components
```

### 🎮 Gaming Integration

```kotlin
Gaming Flow:
Game Selection → WebView Component → Blacknut Platform
              ↓
            Authentication → Game Streaming
```

## 🛡️ Error Handling Patterns

### ⚠️ Exception Management

- **Network Errors**: Retrofit exception handling
- **UI Errors**: Toast messaging system
- **Authentication**: Login state validation

### 🔄 Recovery Patterns

- **Auto-retry**: Network request retry logic
- **Fallback UI**: Error state handling
- **Session Recovery**: Re-authentication flow

## 📊 Performance Patterns

### 🚀 Optimization Strategies

- **Lazy Loading**: Content loaded on demand
- **Image Caching**: Coil/Glide optimization
- **Compose Performance**: State optimization
- **Memory Management**: ExoPlayer lifecycle

### 📱 TV-Specific Optimizations

- **Focus Management**: Remote control navigation
- **Large Screen Layouts**: TV-optimized UI
- **Media Playback**: Hardware acceleration
- **Background Processing**: Service lifecycle
