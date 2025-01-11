# Productivity App

This is a simple productivity app designed to help users track their tasks and manage time effectively. The app features **Home**, **Timer**, and **Profile** sections for better productivity tracking.

## Features

### Home
- Displays a list of tracked tasks.
- Each task includes:
  - Task name.
  - Elapsed time (HH:MM:SS format).
- Built with a `RecyclerView` for dynamic list handling.

### Timer
- Allows users to:
  - Start, stop, and reset a timer.
  - Input a task name while timing.
  - Save the elapsed time and task name to the **Home** screen upon reset.

### Profile
- Placeholder for user profile features (future updates).

---

## Technical Details

### Technologies Used
- **Kotlin**: The programming language used.
- **Android Jetpack Components**:
  - **Navigation Component**: For managing fragment navigation.
  - **ViewModel & LiveData**: For state management and UI updates.
  - **RecyclerView**: For displaying a list of tasks dynamically.

### Architecture
- **Single-Activity Architecture**:
  - `MainActivity` serves as the host for fragments.
  - Fragments:
    - `HomeFragment`
    - `TimerFragment`
    - `ProfileFragment`
- **MVVM Pattern**:
  - Shared data is managed using a `SharedViewModel`.

---

## Screenshots
Add screenshots of the app here:
- Home Screen
  <img width="324" alt="Ekran Resmi 2024-11-30 ÖS 6 34 45" src="https://github.com/user-attachments/assets/caa65639-34bf-4563-9c74-3fc62c914a86">

- Timer Screen
  <img width="324" alt="Ekran Resmi 2024-11-30 ÖS 6 35 14" src="https://github.com/user-attachments/assets/cfcc4f26-3312-4fca-bbf0-121f240aac6d">
  
- Profile Screen
  <img width="324" alt="Ekran Resmi 2024-11-30 ÖS 6 35 29" src="https://github.com/user-attachments/assets/6cbd2614-15bb-47cc-91dc-d9591d850f94">

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo.git
