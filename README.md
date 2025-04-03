<div align="center">
  <img src="logo.png" alt="Logo" width="150" height="150">
  <h3>Dutch Buddy - Learn Dutch Basics</h3>
</div>

A beginner-friendly Android app designed to help users learn Dutch vocabulary using English as the base language. With flashcards and quizzes, Dutch Buddy makes learning Dutch simple, interactive, and fun.

[![Java](https://img.shields.io/badge/Java-8%2B-blue)](https://www.java.com/) [![Android](https://img.shields.io/badge/Android-5.0%2B-green)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](LICENSE)

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [App Structure](#app-structure)
- [Usage](#usage)
  - [Flashcards](#flashcards)
  - [Quizzes](#quizzes)
- [Future Improvements](#future-improvements)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## Overview

**Dutch Buddy** is an Android app that helps users learn Dutch vocabulary through interactive flashcards and quizzes. Words and phrases are organized into categories like Greetings, Food, and Travel, making it easy to focus on specific topics. The app works offline, so users can learn anytime, anywhere.

---

## Features

- **Category-Based Learning**: Learn Dutch words and phrases organized into categories like Greetings, Food, and Travel.
- **Flashcards**: Swipe through flashcards to study Dutch vocabulary with English translations.
- **Quizzes**: Test your knowledge with multiple-choice quizzes and track your score.
- **Offline Support**: All vocabulary is stored locally, so no internet connection is required.

---

## Requirements

- Android Studio (latest version recommended)
- Java 8+
- Android 5.0+ (API Level 21 or higher)

---

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/dutch-buddy.git
   cd dutch-buddy
   ```

2. Open the project in **Android Studio**.

3. Build and run the app on an emulator or physical device.

---

## App Structure

```
dutch-buddy/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                # Java source code
│   │   │   ├── res/                 # XML layouts and resources
│   │   │   ├── AndroidManifest.xml  # App configuration
│   │   └── test/                    # Unit tests
├── database/                        # SQLite database for vocabulary
├── README.md                        # This file
└── LICENSE                          # License file
```

---

## Usage

### Flashcards

1. Select a category from the main screen (e.g., Greetings, Food, Travel).
2. Swipe through flashcards to study Dutch words and their English translations.
3. Tap the card to flip it and reveal the translation.

### Quizzes

1. Select a category and start the quiz.
2. Answer multiple-choice questions based on Dutch vocabulary.
3. View your score at the end of the quiz.

---

## Future Improvements

- [ ] Add audio pronunciation for Dutch words.
- [ ] Implement progress tracking and gamification features.
- [ ] Expand the vocabulary database with more categories.
- [ ] Add speech recognition for practicing pronunciation.
- [ ] Include animations for flashcard transitions.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- The Android development community for resources and tutorials.
- Dutch language learners for inspiring the creation of this app.
- Open-source contributors for tools and libraries used in the project.