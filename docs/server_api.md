# Application Environment Verification API
### Get the results of verification by the requestId

#### Request
```sh
GET https://app-protect.fpapi.io/results?id=1xu9PyL9pclHYbHupthsiupaRci
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

A **free token** is required to connect to our Application Environment Verification API.

_To get your token, please ping us on [Discord](https://discord.com/invite/P6Ya76HkbF) or email us at android@fingerprintjs.com_
_(just type `token` in the email subject, no need to compose a body)_
<br/>
_The free token is limited to 1M API calls per month while in beta._