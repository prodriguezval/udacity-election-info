# Political Preparedness

This project is the final test of the Android Kotlin Developer Nanodegree from Udacity.

### Permissions required

* [INTERNET](https://developer.android.com/reference/android/Manifest.permission#INTERNET)
* [FINE_LOCATION](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION)
* [COARSE LOCATION](https://developer.android.com/reference/android/Manifest.permission#ACCESS_COARSE_LOCATION)
  Required by FINE_LOCATION

### Final result

![](img/app.gif)

## Goal

The final goal of this project is demonstrate know-how in:

* Coroutine & Live Data
* MVVM Architecture
* UI development (Motion layout, glide)
* Data handling (Retrofit, Room Database)
* use of phone hardware (Location)

## Dependencies

```
// Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version_kotlin"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version_kotlin_coroutines"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version_kotlin_coroutines"
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$version_kotlin_serialization"

    // Constraint Layout
    implementation "androidx.constraintlayout:constraintlayout:$version_constraint_layout"

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-extensions:$version_lifecycle_extensions"

    // Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:$version_navigation"
    implementation "androidx.navigation:navigation-ui-ktx:$version_navigation"

    // Core with Ktx
    implementation "androidx.core:core-ktx:$version_core"

    // Retrofit
    implementation "com.squareup.retrofit2:retrofit: $version_retrofit"
    implementation "com.squareup.retrofit2:converter-moshi:$version_retrofit"
    implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$version_retrofit_coroutines_adapter"

    // Moshi
    implementation "com.squareup.moshi:moshi:$version_moshi"
    implementation "com.squareup.moshi:moshi-kotlin:$version_moshi"
    implementation "com.squareup.moshi:moshi-adapters:$version_moshi"

    // Glide
    implementation "com.github.bumptech.glide:glide:$version_glide"
    annotationProcessor "com.github.bumptech.glide:compiler:$version_glide"

    //Room
    implementation "androidx.room:room-ktx:$version_room"
    implementation "androidx.room:room-runtime:$version_room"
    kapt "androidx.room:room-compiler:$version_room"

    // Location
    implementation "com.google.android.gms:play-services-location:$version_play_services_location"
```

### Installation

1. get the API key for Google Civics API
   following [this](https://developers.google.com/civic-information/docs/using_api) instructions
2. To hide the secrets from the VCS I use the plugin secrets-gradle-plugin, to run the app you add
   line in the local.properties file as follows:

```
CIVICS_API_KEY=<YOUR_CIVICS_API_KEY>
```

## Project Instructions

1. Download the code using this command:
   ```
   git clone git@github.com:prodriguezval/udacity-election-info.git
   ```
2. Open the project in android studio, I used Android Studio Bumblebee | 2021.1.1 Patch 2
3. Run the project on your phone or an emulator with API 26 or higher

## Built With

To build this project I used the Google Civics API, which you can
find [here](https://developers.google.com/civic-information).

You will need an API Key in order to run the project without issues, please follow the installation
process
