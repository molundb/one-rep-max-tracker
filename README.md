# One Rep Max Tracker

**One Rep Max Tracker** is an Android application built entirely with Kotlin and Jetpack Compose using MVVM architecture. It is currently in development and not yet available in the Google Play Store.

## Features 
With **One Rep Max Tracker** users can track their heaviest lifts, also known as 1 rep maxes or 1RMs.

## Architecture
The app follows MVVM architecture inspired by [Now In Android](https://github.com/android/nowinandroid).

## Build
The app contains the usual `debug` and `release` build variants. It also uses [product flavors](https://developer.android.com/studio/build/build-variants#product-flavors) to control where to send tracking events.

To build and run the app you need to create a firebase project and [add the google-services.json file to the project](https://support.google.com/firebase/answer/7015592?hl=en#android&zippy=%2Cin-this-article).

## Testing
To run the tests execute the following gradle tasks: 
 - `testDevDebug` runs all local tests against the `devDebug` variant.
 - `connectedDevDebugAndroidTest` runs all instrumented tests. At the moment there are only instrumented tests.

## UI 
The app was designed using [Material 3 guidelines](https://m3.material.io/). The Screens and UI elements are built entirely using [Jetpack Compose](https://developer.android.com/compose).
