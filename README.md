
# Smart Pantry Manager

Smart Pantry Manager is a Java-based Android application designed to help users reduce food waste by tracking their home ingredients and suggesting recipes they can cook using only what they have.

## Core Logic: Strict-Matching Rule
A recipe is only suggested if **every single ingredient** it requires is present in the user's pantry in at least the required quantity. The app handles simple pluralization (e.g., "Tomato" vs "Tomatoes") and case differences to ensure robust matching.

## Features
- **Pantry Management:** Add, edit, and delete ingredients (name, quantity, unit, expiry date).
- **Recipe Suggestions:** View recipes you can cook right now based on your current pantry items.
- **Recipe Details:** View ingredients and preparation steps for any suggested recipe.
- **Persistence:** All data is saved locally using **SQLite (via Room persistence library)**.
- **Settings:** Toggle expiry alerts and set unit preferences.

## Technical Details
- **Language:** Java
- **Database:** SQLite (Room)
- **Architecture:** MVVM (Model-View-ViewModel) with Navigation Components.
- **UI:** RecyclerView for lists, Material Design components, and ConstraintLayout.

## Setup Instructions
1. Open the project in Android Studio.
2. Ensure you have the Android SDK for API 37 installed.
3. Build and run the app on an emulator or physical device.
4. On first run, the database will be pre-seeded with 20 sample recipes.

## GitHub Version Control
- Incremental development with clear commit messages.
- Full CRUD functionality implemented.
- Strict-matching logic tested with unit tests.
=======
# Smart-Pantry-Manager
A Java Android Application that Suggests Recipes Based Strictly on Leftover  Ingredients to Cut Food Waste 

