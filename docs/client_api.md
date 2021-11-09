
# Android Application Environment Verification API


### Get the request ID

Kotlin

```kotlin

// Initialization
val aevClient = AevClientFactory.getInstance(
    applicationContext,
    YOUR_API_TOKEN
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

A **free token** is required to connect to our Application Environment Verification API.

_To get your token, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `token` in the email subject, no need to compose a body)_
<br/>
_The free token is limited to 1M API calls per month while in beta._