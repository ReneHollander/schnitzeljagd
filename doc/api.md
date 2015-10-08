# Events
### `get_station`
Event Name: `get_station`
Response Type: Acknowledge
##### Request:
No data needed
##### Response:
```json
{
  "uuid": "c68b9afb-0626-43bb-ad07-344d5006a1c6",
  "title": "Example Task",
  "description": "This is the example task",
  "navigation": {
    "type": "compass|map|text|html"
  },
  "answer": {
    "type": "qr|question|area"
  }
}
```

### `submit`
Submits an answer to the server. If the answer is correct, the UUID of the next station will be sent. If there was an error or the answer is not correct an special error message will be sent.
Event Name: `submit`
Response Type: Acknowledge
##### Request:
```json
{
  "type": "qr|question|area"
}
```
##### Response:
Correct Answer:
```json
{
  "uuid": "c68b9afb-0626-43bb-ad07-344d5006a1c6"
}
```

Error:
```json
{
  "error": {
    "code": 404,
    "message": "I am an error message!"
  }
}
```

# Data Format
### `get_station` Response
#### General
```json
{
  "uuid": "c68b9afb-0626-43bb-ad07-344d5006a1c6",
  "title": "Example Task",
  "description": "This is the example task",
  "navigation": {
    "type": "compass|map|text|html"
  },
  "answer": {
    "type": "qr|question|area"
  }
}
```
#### Navigation
##### `compass`
Shows an arrow into the direction of the location.
```json
{
  "type": "compass",
  "target": {
    "lat": "48.305461",
    "lang": "16.326053"
  },
  "showdistance": "true|false"
}
```

##### `map`
Puts an marker onto a map to navigate to.
```json
{
  "type": "map",
  "target": {
    "lat": "48.305461",
    "lang": "16.326053"
  }
}
```

##### `text`
Displays the given text onto the screen.
```json
{
  "type": "text",
  "content": "Here goes the description!"
}
```

##### `html`
Displays the given HTML on a WebView.
```json
{
  "type": "html",
  "html": "<html><body>Here goes the description!</body></html>"
}
```

#### Answer
##### `qr`
Scan a QR Code to get to the next station.
```json
{
  "type": "qr",
  "hint": "Hint to display if the QR is not found"
}
```

##### `question`
Scan a QR Code to get to the next station.
```json
{
  "type": "question",
  "text": "Where is the Rathausplatz?",
  "answers": [
    {
      "uuid": "7f308c10-a8ff-4554-a29c-8079682a86d7",
      "text": "In Klosterneuburg"
    },
    {
      "uuid": "e6054ddb-2e0d-41dc-9234-778a7995da57",
      "text": "Nowhere"
    }
  ]
}
```

##### `area`
Stay in a specific area for a given amount of time.
```json
{
  "type": "area",
  "text": "Walk around on the Rathausplatz",
  "timetostay": 300,
  "showonmap": "true",
  "area": [
    {
      "lat": "48.304732",
      "lang": "16.325373"
    },
    {
      "lat": "48.305308",
      "lang": "16.326177"
    },
    {
      "lat": "48.305766",
      "lang": "16.326010"
    },
    {
      "lat": "48.305310",
      "lang": "16.324793"
    }
  ]
}
```

### `submit` Request
##### `qr`
```json
{
  "type": "qr",
  "uuid":
}
```

##### `question`
UUID of the selected question.
```json
{
  "type": "question",
  "uuid": "7f308c10-a8ff-4554-a29c-8079682a86d7"
}
```

##### `area`
Area validation is done client side!
```json
{
  "type": "area"
}
```

# QR Code content
The QR Code contains the content in the following format:
```
0a877a7a-0014-4ff6-8783-f3dc136a168f
```
It's just a plain UUID :D
