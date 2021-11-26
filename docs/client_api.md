
# Android Application Environment Verification API

## What is request ID?

When the application environment is not secure it does not make sense to get results on the device because they can be compromised. The only thing that library is returning is request ID. It does not contain any information. But with using this you are able to use [server API](server_api.md) from more secured environment (e.g. your own server side).

## Android library public API

```kotlin
interface AevClient {
    fun getRequestId(listener: (String) -> (Unit))
    fun getRequestId(listener: (String) -> (Unit), errorListener: (String) -> (Unit))
}
```

## Get the request ID

A **free API keys** are required to connect to our Application Environment Verification API. Use the Public one in the library.

_To get your API keys, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `API keys` in the email subject, no need to compose a body)_

Kotlin
```kotlin

// Initialization
val aevClient = AevClientFactory.getInstance(
    applicationContext,
    "YOUR_PUBLIC_API_KEY"
)


// Get the RequestID
aevClient.getRequestId(
                listener = { requestId ->
                    // Handle the string with requestId
                },
                errorListener = { error ->
                    // Handle the error string
                })

```
Java
```java

AevClient aevClient = AevClientFactory.getInstance(applicationContext, "YOUR_PUBLIC_API_KEY");

aevClient.getRequestId(new Function1<String, Unit>() {
        @Override
        public Unit invoke(String s) {
              // Handle the string with requestId
        }
    }, new Function1<String, Unit>() {
        @Override
        public Unit invoke(String s) {
             // Handle the error string
        }
    });

```
