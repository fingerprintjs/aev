
# Android Application Environment Verification API

## What is request ID?

When the application environment is not secure it does not make sense to get results on the device. The only thing that library is returning is request ID. It does not contain any information. But with using this you are able to use [server API](server_api.md) from more secured environment (e.g. your own server side).

## Android library public API

```kotlin
interface AevClient {
    fun getRequestId(listener: (String) -> (Unit))
    fun getRequestId(listener: (String) -> (Unit), errorListener: (String) -> (Unit))
}
```

## Get the request ID

A **free token** is required to connect to our Application Environment Verification API.

_To get your token, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `token` in the email subject, no need to compose a body)_
<br/>
_The free token is limited to 1M API calls per month while in beta._

Kotlin

```kotlin

// Initialization
val aevClient = AevClientFactory.getInstance(
    applicationContext,
    "YOUR_API_TOKEN"
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

AevClient aevClient = AevClientFactory.getInstance(applicationContext, "YOUR_API_TOKEN");

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
