# Angels and Saints

A modern, responsive, and offline-first Android application designed to provide daily spiritual nourishment through liturgical content, saintly biographies, and multi-language prayers. Built entirely with **Jetpack Compose** and **Material 3**.

## ✨ Features

*   **📅 Daily Feast & Readings**: Dynamic, date-based content fetched automatically for the current day. Includes intuitive "Previous/Next" navigation to browse the liturgical calendar.
*   **🧩 Block-Based Content**: A flexible dynamic rendering engine that supports various content types including headings, body text, and full-width images.
*   **🌍 Multi-Language Prayers**: Seamlessly switch between English and Marathi for prayers using a polished tabbed interface.
*   **🚀 Smart Offline Mode**:
    *   **Indefinite Caching**: Content is stored locally for up to 1 year using OkHttp disk caching.
    *   **Background Prefetching**: Automatically crawls and downloads all app content on launch to ensure a 100% offline-ready experience.
*   **📱 Responsive & Professional UI**:
    *   Adaptive grid layouts that scale perfectly from small phones to large tablets.
    *   Sophisticated "Gold, Cream, and Burgundy" theme inspired by classic ecclesiastical aesthetics.
    *   Instant-loading local assets for primary categories.

## 🛠 Tech Stack

*   **UI**: Jetpack Compose (Material 3)
*   **Navigation**: Compose Navigation (ID-based routing)
*   **Networking**: Retrofit 2 & OkHttp 3
*   **JSON Parsing**: Moshi (Kotlin Reflection)
*   **Image Loading**: Coil (with custom local resource fallback)
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Architecture**: MVVM (Model-View-ViewModel)

## 📁 Data Structure

The app consumes a remote JSON-based backend. The file hierarchy on the server should be:

- `home.json`: Main category list.
- `categories/{id}.json`: List of items per category.
- `items/{id}.json`: Detailed block-based content for individual items.
- `feast-daily/YYYY/MM/YYYY-MM-DD.json`: Dynamic daily feast data.
- `readings/YYYY/MM/YYYY-MM-DD.json`: Daily liturgical readings.

## 🚀 Getting Started

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/angels-and-saints.git
    ```
2.  **Add Assets**: Place category thumbnail images in `app/src/main/res/drawable/` (e.g., `cat_angels.jpg`, `cat_saints.jpg`).
3.  **Configure Backend**: Update the `BASE_URL` in `com.rhinepereira.saints.data.remote.RetrofitClient` to point to your hosted JSON content.
4.  **Build**: Open the project in **Android Studio Ladybug** (or newer) and run on a device or emulator.

---
*Developed by Rhine Pereira*
