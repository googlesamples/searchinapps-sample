SearchInApps API sample apps
============

Introduction
------------

The SearchInApps API allows apps to get Google related search suggestions and trending searches.
These apps provide fully functional examples of integrating with the SearchInApps SDK.

Getting started
---------------
Clone this project to your workstation using a git client. You can use the
[instructions from GitHub](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
if you need guidance.

### Android ###
The Android projects use the Gradle build system. To build this project, use the
`gradlew build` command or use "Import Project" in Android Studio.

For more resources on learning Android development, visit the
[Developer Guides](https://developer.android.com/guide/) at
[developer.android.com](https://developer.android.com).

### iOS ###
The iOS projects are Xcode projects that use [cocoapods](https://cocoapods.org/)
for package dependency. 

For more resources on learning iOS development, visit Apple's
[documentation](https://developer.apple.com/documentation/) at
[developer.apple.com](https://developer.apple.com).

Prerequisites
-------------

The sample apps need a **client id** and **api key** to work. Please follow the [SearchInApps Android SDK developer guide](https://developers.google.com/search-in-apps/android) or [SearchInApps iOS SDK developer guide](https://developers.google.com/search-in-apps/ios) to get your client id and api key.

For Android, insert the **client id** and **api key** into the sample apps' AndroidManifest.xml file, for iOS insert it into the project's Info.plist.

For Android Java developers
-------------------

The Android View sample application is contained in the view/ directory. It is written fully in Java. The detailed information about the SearchInApps SDK's Java classes can be found [here](https://developers.google.com/search-in-apps/android/reference/com/google/android/libraries/searchinapps/package-summary).

For Android Jetpack Compose developers
------------------------------

The Jetpack Compose sample application is contained in the compose/ directory. It is written fully in Kotlin. The detailed information about the SearchInApps SDK's Kotlin classes can be found [here](https://developers.google.com/search-in-apps/android/reference/kotlin/com/google/android/libraries/searchinapps/package-summary).

For iOS SwiftUI developers
------------------------------
The SwiftUI sample application is contained in the swiftui/ directory. The detailed information about the SearchInApps SDK's Swift classes can be found [here](https://developers.google.com/search-in-apps/ios/reference/api/swift_reference/Classes/ContextualSearchRuntime).


Support
-------

Please report issues with this sample in this project's issues page:
https://github.com/googlesamples/searchinapps-sample/issues
