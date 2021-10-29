<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="resources/logo.svg" alt="FingerprintJS" width="300px" />
  </a>
</p>
																		      
																		      
# Android Application Protection API

<small><i>currently in beta - API may change</i></small>

A library for verification security of the application environment.

The library sends a set of signals to the server.

The server verifies safety of the application environment.

Fully written in Kotlin.


## Table of Contents
1. [Quick start](#quick-start)
2. [Usage](#usage)
4. [Demo App](#demo-app)


## Quick start

### Add repository

Add these lines to your `build.gradle`.


```gradle
allprojects {
  repositories {
  ...
  maven { url 'https://jitpack.io' }
}}
```

### Add dependencies

Download the latest release and store it to the `libs` folder of your module. 
 
Add these lines to `build.gradle` of a module.

This library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/).

If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

```gradle
dependencies {
  implementation "com.github.fingerprintjs:fingerprint-android:1.2"
  implementation(name:'Mobile-Application-Protection-1.0.0-release', ext:'aar')
  // Add this line only if you use this library with Java
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}

```


## Usage

### Get the request ID

Kotlin

```kotlin

// Initialization
val protector = ApplicationProtectorFactory.getInstance(
    applicationContext,
    YOUR_API_TOKEN
)


// Get the RequestID
applicationProtector.getRequestId(
                listener = { requestId ->
                    // Handle the string with requestId
                },
                errorListener = { error ->
                    // Handle the error string
                })

```

### Get the results of verification by the requestId

#### Request
```http request
GET /results?id=1xu9PyL9pclHYbHupthsiupaRci
Auth-Token: YOUR_API_TOKEN
```

#### Response
```json
{
  "deviceId": "1xu9l9Ure84KB8CnEbABmteHhhc",
  "results": {
    "rootManagementAppsDetected": {
      "v": true
    },
    "emulatorDetected": {
      "v": true
    }
  }
}
```

## Demo app

## Android API support

Android application protection library  supports API versions from 21 (Android 5.0) and higher.


## License

This library is MIT licensed.
Copyright FingerprintJS, Inc. 2020-2021.
