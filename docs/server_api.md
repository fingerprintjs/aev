# Application Environment Verification API

Once you get the requestId from a [client](client_api.md), send it to your backend, and use the server API to get results of verification.
For security reasons it's the best way to check if the device has been compromised. 

| **Detection scenarios** | **What's triggering**                                                          | **Results**       |
|-------------------------|--------------------------------------------------------------------------------|-------------------|
| Root management apps    | Installed [Magisk](https://github.com/topjohnwu/Magisk)                                                               | `true` or `false` |
| Emulator                | Running in emulated environent: popular emulators such as Nox, Bluestacks etc. | `true` or `false` |

## Get the results

### Make sure you have the API token 

A **free API keys** are required to connect to our Application Environment Verification API. Use the Private one on the server side.

_To get your API keys, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `API keys` in the email subject, no need to compose a body)_

<br/>

### Get the results of verification by the requestId

#### Request
```json
POST /verify
Content-Type: application/json

{
  "privateApiKey": "1xu9KDq6EWYzeSSLmB9TpMe5UXp",
  "requestId": "1xu9PyL9pclHYbHupthsiupaRci"
}
```

#### Response
```json
{
  "deviceId": "1xu9l9Ure84KB8CnEbABmteHhhc",
  "results": {
    "rootManagementAppsDetected": true,
    "emulatorDetected": true
  }
}
```
