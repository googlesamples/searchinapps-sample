Android SearchInApps API sample apps
============

Introduction
------------

Android SearchInApps API allows apps to get Google related search suggestions and trending searches.
These apps provide fully functional examples of integrating with SearchInApps SDK.

Getting started
---------------
Clone this project to your workstation using a git client. You can use the
[instructions from GitHub](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
if you need guidance.

This project uses the Gradle build system. To build this project, use the
`gradlew build` command or use "Import Project" in Android Studio.

For more resources on learning Android development, visit the
[Developer Guides](https://developer.android.com/guide/) at
[developer.android.com](https://developer.android.com).

Prerequisites
-------------

To let these sample apps work properly, please follow the [SearchInApps Android SDK developer guide](https://developers.google.com/search-in-apps/android) to get your client id and api key, and put them into the sample apps' AndroidManifest.xml file.

For Java developers
-------------------

The Android View sample application is contained in the view/ directory. It is written fully in Java. The detailed information about the SearchInApps SDK's Java classes can be found [here](https://developers.google.com/search-in-apps/android/reference/com/google/android/libraries/searchinapps/package-summary).

For Jetpack Compose developers
------------------------------

The Jetpack Compose sample application is contained in the compose/ directory. It is written fully in Kotlin. The detailed information about the SearchInApps SDK's Kotlin classes can be found [here](https://developers.google.com/search-in-apps/android/reference/kotlin/com/google/android/libraries/searchinapps/package-summary).

Support
-------

Please report issues with this sample in this project's issues page:
https://github.com/googlesamples/searchinapps-sample/issues
