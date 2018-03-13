# Add Firebase to your app

Firebase is a mobile-backend-as-a-service that provides several features for building powerful mobile apps. Firebase has three core services: a realtime database, user authentication and hosting.

## Getting Started

### Prerequisites

* A device running Android 4.0 (Ice Cream Sandwich) or newer, and Google Play services 11.8.0 or higher
* [Android Studio](https://developer.android.com/studio/index.html) - The latest version of Android Studio


### Installing

If you're using Android Studio version 2.2 or later, the Firebase Assistant is the simplest way to connect your app to Firebase. The Assistant can connect your existing project or create a new one for you with all the necessary gradle dependencies.

### Add the SDK

If you would like to integrate the Firebase libraries into one of your own projects, you need to perform a few basic tasks to prepare your Android Studio project. You may have already done this as part of adding Firebase to your app.

First, add rules to your root-level build.gradle file, to include the google-services plugin and the Google's Maven repository:

```
buildscript {
    // ...
    dependencies {
        // ...
        classpath 'com.google.gms:google-services:3.2.0' // google-services plugin
    }
}

allprojects {
    // ...
    repositories {
        // ...
        maven {
            url "https://maven.google.com" // Google's Maven repository
        }
    }
}

```
Then, in your module Gradle file (usually the app/build.gradle), add the apply plugin line at the bottom of the file to enable the Gradle plugin:

```
apply plugin: 'com.android.application'

android {
  // ...
}

dependencies {
  // ...
  compile 'com.google.firebase:firebase-core:11.8.0'
  
  // Getting a "Could not find" error? Make sure you have
  // added the Google maven respository to your root build.gradle
}

// ADD THIS AT THE BOTTOM
apply plugin: 'com.google.gms.google-services'
```



## Authors

* **Muhammad Atif** - *Initial work* - [Muhammad Atif](https://github.com/atifabbasi19)

## License

```
Copyright 2018 Muhammad Atif

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
