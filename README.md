#  📊 Portfolio Holdings App

Android app that allows a relationship manager or HNI client to view portfolio holdings and drill down into individual positions. The focus is on building a clean, end-to-end solution with proper architecture, state management, and offline data handling rather than feature complexity.

---

## 🛠️ Tech Stack

Language: Kotlin  
UI: Jetpack Compose (Material Design 3)  
Architecture: MVVM  
DI: Hilt (Dependency Injection)  
Database: Room (offline storage)  
Asynchrony: Coroutines & Flow  
Serialization: Gson  
Navigation: Navigation Compose  
Testing: JUnit 4, kotlinx-coroutines-test

---

## ✨ Features

* 📊 Holdings list with invested value, current value, and P&L (₹ + %)
* 📈 Portfolio summary (total invested, total value, overall P&L)
* 🔍 Holding detail screen with stats and performance bar
* 📜 Transactions list with Buy/Sell filtering
* 📦 Offline-first using JSON data
* 🧪 Unit tests for domain, data, and ViewModel layers

---

# 🏗️ Architecture & Data Flow

This project follows Clean Architecture principles to ensure a testable, maintainable, and scalable codebase.

* **MVVM:** Clear separation between UI and business logic  
* **Offline-First:** Room acts as the single source of truth  
* **Reactive UI:** Built using StateFlow and lifecycle-aware collection
* 
**Data Flow:**
JSON → DTO → Entity → Domain Model → UI State

---

## 📦 Data Source — Why JSON Fixtures?

Bundled JSON files in `assets/` are used as the data source to keep the app simple, reliable, and fully offline.

* No network dependency — works consistently without API or internet access  
* Deterministic data — same results across all runs and devices  
* Faster development — no backend setup or API integration required  
* Easily replaceable — the data layer is abstracted, so switching to a real API later requires minimal changes  

---

## 🚀 How to Run
 
**Prerequisites:** Android Studio Ladybug or later, JDK 17+
 
```bash
# 1. Clone the repo
git clone https://github.com/karthika-sivakumar/PortfolioApp.git
 
# 2. Open in Android Studio
# File → Open → select the project folder
 
# 3. Let Gradle sync complete
 
# 4. Run on emulator or device
# Click ▶ Run
```

> ✅ No API key · ✅ No server setup · ✅ Data loads from bundled JSON assets
 
---

## 🧪 Tests
 
Three focused unit test suites covering each architectural layer.

**Run tests:**
 
```bash
./gradlew test
```
 
Or in Android Studio: right-click any test class → **Run**
 
---

## 🔭 What I'd Add With More Time
 
* **Pagination** for transaction history
* **Loading skeletons** instead of spinners
* **Compose UI tests** for screen-level flows
* **Timestamp-based cache invalidation** (stale vs fresh Room data)
* **Portfolio analytics** — allocation %, sector split
* **Retrofit integration** — drop-in replacement for `JsonDataSourceImpl`
* **Accessibility improvements** — content descriptions, contrast ratios
---
 
## ⏱️ Time Allocation

| Task | Time |
|---|---|
| Project setup + Architecture | 2 hrs |
| Data layer (JSON, DTO, DAO, Repository, Mappers) | 2 hrs |
| Holdings screen UI | 2 hrs |
| Detail screen + transactions tab | 2 hrs |
| Navigation + state wiring | 1 hr |
| Unit tests (domain, data, ViewModel) | 1 hrs |
| README + AI_LOG | 1 hr |
| **Total** | **~11 hrs** |
 
---

## 📂 Project Context

This project was developed as a solution for an Android Developer Internship assignment.

The primary objective was to demonstrate the ability to build a "small but deep" end-to-end solution. Rather than focusing on a broad feature set, the emphasis was placed on:

- Engineering Quality: Implementing a robust Clean Architecture.
- Data Integrity: Ensuring financial calculations are correct and tested.
- Resilience: Handling offline states and graceful failures using a local-first approach.
