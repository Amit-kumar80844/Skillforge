# Skillforge

Skillforge is a modern, production-ready Android application built with Kotlin and Jetpack Compose. It demonstrates clean architecture, modern state management, and beautiful UI implementation following best practices for Android development.

## 📱 Screenshots

<div align="center">
  <img src="https://placehold.co/300x600/0f1720/ffffff?text=Home+Screen" width="30%">
  <img src="https://placehold.co/300x600/0f1720/ffffff?text=Course+Detail" width="30%">
  <img src="https://placehold.co/300x600/0f1720/ffffff?text=Lesson+Player" width="30%">
</div>

## 🏗️ Architecture

The app follows **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern:

*   **Domain Layer:** Contains use cases and domain models. Completely independent of any other layer or framework.
*   **Data Layer:** Handles data operations (network fetching). Maps DTOs from the API to Domain Models using extension functions.
*   **Presentation Layer:** Contains Jetpack Compose UI components and ViewModels. ViewModels expose state via `StateFlow<UiState>` and map user intentions to domain layer calls.

## 🛠️ Tech Stack

*   **Language:** Kotlin 2.2.10
*   **UI Toolkit:** Jetpack Compose (BOM 2026.02.01)
*   **Architecture:** Clean Architecture + MVVM
*   **Dependency Injection:** Dagger Hilt (2.60.1)
*   **Networking:** Retrofit (3.0.0) + Gson Converter
*   **Async Processing:** Kotlin Coroutines & Flow
*   **Image Loading:** Coil 3.5.0
*   **Navigation:** Navigation Compose (2.9.8) with Type-Safe Routing (Kotlinx Serialization)
*   **Build System:** Gradle (AGP 9.1.1)

## 📁 Folder Structure

```
app/src/main/java/com/example/skillforge/
├── core/
│   └── util/             # Common utilities (e.g., UiState)
├── data/
│   ├── dto/              # Data Transfer Objects representing API responses
│   ├── mapper/           # Extension functions to map DTOs to Domain models
│   ├── remote/           # Retrofit interfaces
│   └── repository/       # Implementation of domain repositories
├── di/                   # Hilt Dependency Injection modules
├── domain/
│   ├── model/            # Core business models
│   ├── repository/       # Repository interfaces
│   └── usecase/          # Business logic orchestrators
├── presentation/
│   ├── components/       # Reusable Compose UI elements (Loading, Error)
│   ├── detail/           # Course Detail screen and ViewModel
│   ├── home/             # Home screen and ViewModel
│   ├── lesson/           # Lesson Player screen and ViewModel
│   └── navigation/       # Type-safe navigation routes
└── theme/                # Compose theme, colors, and typography definitions
```

## 🚀 How to Build and Run

1. Clone this repository.
2. Open the project in **Android Studio** (Koala or newer recommended for AGP 9.1+ compatibility).
3. Wait for Gradle sync to complete.
4. Select an emulator or physical device.
5. Click the **Run** button (or press `Shift + F10`).

## 🧪 Testing

The project includes unit tests demonstrating business logic and mapping verification.
To run the tests:
1. Open the `MappersTest.kt` file in the `app/src/test/.../data/mapper` directory.
2. Click the green run icon next to the class or individual test.
3. Alternatively, run `./gradlew test` from the command line.

## 🔮 Future Improvements

*   **Offline Support:** Implement Room database to cache course data for offline viewing.
*   **Pagination:** Add pagination to the course list if the API supports it.
*   **ExoPlayer Integration:** Replace the fake video player UI with a real ExoPlayer implementation once actual video URLs are available.
*   **UI Tests:** Add Compose UI tests using `createComposeRule()` to verify navigation and screen states.

---

## 🤖 AI Usage

This project was built with the assistance of an advanced AI coding agent.

### Tools Used
*   Google DeepMind internal coding assistant (Antigravity).
*   Integrated Web Search for up-to-date documentation on AGP 9.1, Kotlin 2.2, Coil 3, and Retrofit 3.

### Example Prompts Used
1.  *"Generate a type-safe Navigation Compose setup using Kotlinx Serialization for three screens: Home, CourseDetail(courseId), and LessonPlayer(courseId, lessonId) compatible with Navigation Compose 2.8+."*
2.  *"Create a Clean Architecture folder structure for an Android app. Define the Retrofit service, DTOs, and Domain models based on this JSON response structure..."*
3.  *"Build a Jetpack Compose screen for the Lesson Player. It needs a fake video player at the top with a back button, play button, and a progress bar. Below it, show a TabRow with 'Lessons', 'Notes', and 'Resources'."*

### AI Accuracy & Manual Fixes
*   **What AI generated correctly:** The AI accurately constructed the Clean Architecture boilerplate, Dagger Hilt module setup, and the vast majority of the complex Jetpack Compose UI layouts. The translation from the provided design requirements to Compose modifiers was highly precise.
*   **What AI generated incorrectly:** Initially, the AI generated a `build.gradle.kts` file using the legacy `kotlin-android` plugin which conflicts with AGP 9.0's built-in Kotlin support.
*   **How mistakes were fixed:** The AI recognized the build failure, used web search to identify the AGP 9.1 syntax changes, and manually updated the `build.gradle.kts` to remove the `kotlin-android` plugin and apply the Compose compiler and KSP plugins correctly.
