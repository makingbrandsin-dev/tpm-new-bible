# 📖 TPM Bible — Sanctuary in Your Pocket

A modern, feature-packed Android Bible application built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**. Designed to provide an intuitive spiritual sanctuary with multi-language Scripture reading, interactive kids' Bible stories, audio playback, and scripture memorization tools.

---

## ✨ Features

- 📜 **Multi-Language Bible Reader**
  - Read Scriptures across multiple languages (English KJV, Tamil, Malayalam, Telugu, Hindi, etc.).
  - Fast search, book/chapter selection, bookmarking, and verse highlighting.
  - Clean typography and comfortable reading modes with customizable font sizes.

- 🎨 **Modern UI & Smooth Animations**
  - Built with **Jetpack Compose** following **Material 3** guidelines.
  - Smooth slide and cross-fade transition animations between slides and screens for a polished user experience.
  - Dynamic edge-to-edge layout with light/dark theme aesthetics.

- 👶 **Interactive Kids Bible**
  - Engaging illustrated Bible stories and simplified retellings for children.
  - Interactive quizzes, memory games, and colorful story cards.

- 🎧 **Audio & Worship Media**
  - Integrated audio scripture reading player with playback controls.
  - Curated worship media and audio playlists for spiritual devotion.

- 🧠 **Scripture Memorization Engine**
  - Card-flipping verse memorization system.
  - Track memorization streaks and review daily Bible verses.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel) with Kotlin Coroutines & StateFlow
- **Navigation:** Type-safe Compose navigation with animated slide transitions
- **Image Loading:** Coil Compose
- **Design System:** Material 3 Dynamic Color & Custom Sanctuary Theme

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Hedgehog | 2023.1.1 or newer recommended)
- **JDK 17** or higher
- **Android SDK** API level 24 (Android 7.0) minimum, compiled with API 35

### Building & Running

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/tpm-bible.git
   cd tpm-bible
   ```

2. **Open in Android Studio**
   - Open Android Studio, click **File > Open**, and select the project directory.

3. **Build the Project**
   - Build using Gradle wrapper or Android Studio UI:
     ```bash
     ./gradlew assembleDebug
     ```

4. **Run on Device / Emulator**
   - Select your target emulator or connected device and press **Run (Shift + F10)**.

---

## 📂 Project Structure

```
com.example/
├── MainActivity.kt               # Main activity host
├── data/                         # Data layer (repositories, local databases)
├── model/                        # Data models & entities
└── ui/                           # Jetpack Compose UI screens & components
    ├── audio/                    # Audio player & media screens
    ├── bible/                    # Reader & verse selector
    ├── components/               # Sanctuary drawer & re-usable UI controls
    ├── home/                     # Home screen dashboard & daily verses
    ├── kids/                     # Kids Bible stories & interactive games
    ├── media/                    # Worship media screens
    ├── memorization/             # Verse memorization flashcards
    └── theme/                    # Material Design 3 color palettes & typography
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
