# Application Environment Verification API

Once you get the requestId from a [client](client_api.md), send it to your backend, and use the server API to get results of verification.
For security reasons it's the best way to check if the device has been compromised. 

| **Detection scenarios** | **What's triggering**                                                          | **Results**       |
|-------------------------|--------------------------------------------------------------------------------|-------------------|
| Root management apps    | Installed [Magisk](https://github.com/topjohnwu/Magisk)                                                               | `true` or `false` |
| Emulator                | Running in emulated environent: popular emulators such as Nox, Bluestacks etc. | `true` or `false` |

## Get the results

### Make sure you have the API token 

A **free token** is required to connect to our Application Environment Verification API.

_To get your token, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `token` in the email subject, no need to compose a body)_
<br/>
_The free token is limited to 1M API calls per month while in beta._

### Get the results of verification by the requestId

#### Request
```sh
GET https://app-protect.fpapi.io/results?id=REQUEST_ID
Auth-Token: YOUR_API_TOKEN
```

#### Response
```json
{
  "deviceId": "1xu9l9Ure84KB8CnEbAsdwdqhc",
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
