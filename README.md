# 🧳 Travel Itinerary Planner

A native Android application for planning and organizing trips end-to-end — create trips, add destinations, build day-wise schedules, jot down notes, and discover destination-based recommendations, all in one connected app.

Built as a Mobile Application Development project by a team of 4, with each member owning a core module.

## ✨ Features

- **Trip Management** — create, view, and manage multiple trips with names, dates, and cover emojis
- **Destination Handling** — add one or more destinations to any trip
- **Day-wise Schedule Planner** — break each trip into Day 1, Day 2... with timed activities, locations, and costs
- **Trip Notes** — attach sticky notes to any trip, with color-coding and timestamps
- **Smart Recommendations** — get destination-based suggestions for tourist spots, restaurants, and hotels, with the option to add any suggestion directly into your schedule

## 📱 Screens & Flow

Splash Screen
     ↓
Home (all trips)
     ↓
Create Trip → Trip Detail (hub)
                  ├── Add Destination
                  ├── Day Planner
                  ├── Notes
                  └── Recommendations

Every screen is linked through a shared `Trip` object, passed between screens via Android Intents using a `trip_id`.

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java |
| Platform | Android (minSdk 24, targetSdk 34) |
| UI | AppCompat, Material Components, ConstraintLayout, CardView |
| Lists | RecyclerView with custom Adapters |
| Local Storage | SharedPreferences + Gson (JSON serialization) |
| Networking | HttpURLConnection |
| External API | [Overpass API](https://overpass-api.de/) (OpenStreetMap data) |
| JSON Parsing | org.json |


## 🗺️ Recommendation Module

One of the core modules — suggests nearby places based on a trip's destination:

1. Loads **curated offline data** instantly for popular cities (no network needed)
2. Simultaneously fetches **live nearby places** (tourist spots, restaurants, hotels) from the Overpass API using the destination's coordinates
3. Falls back gracefully to offline data if the live fetch fails — no errors shown to the user
4. Lets users filter by category (All / Tourist / Food / Hotel)
5. One-tap **"Add to Trip"** pushes any recommendation directly into the Day Planner schedule


## 📂 Project Structure


app/src/main/java/com/example/travel/planner/
├── activities/       → Screens (SplashActivity, MainActivity, RecommendationsActivity, etc.)
├── adapters/         → RecyclerView adapters for each list-based screen
├── models/           → Plain data classes (Trip, Destination, DayPlan, Recommendation, etc.)
└── utils/            → Helper classes (TripStorage, RecommendationFallback)


## 🚀 Getting Started

1. Clone the repository:
   bash
   git clone https://github.com/Prathiksha14k/Travel-Planner.git
   
2. Open the project in **Android Studio**
3. Let Gradle sync complete
4. Run on an emulator or physical device (minSdk 24+)

> **Note:** The Recommendation module requires an active internet connection to fetch live data via the Overpass API. Without internet, it falls back to built-in offline suggestions.


## 👥 Team & Modules

| Module | Description |
|---|---|
| Notes & Splash | Launch screen + per-trip notes |
| Trip Creation | Create/edit trips, add destinations |
| Day Planner | Day-wise activity scheduling |
| Recommendations | Destination-based place suggestions |
