# AI History API

The AI history API lets authenticated users retrieve previously generated AI advice and persisted chat conversations. These endpoints are read-only and do not call the internal Python AI service.

Protected endpoints require:

```http
Authorization: Bearer <token>
Accept: application/json
```

All responses use the shared API envelopes and include `Cache-Control: no-store`.

## Advice History

```http
GET /api/ai/advice?startDate=2026-06-20&endDate=2026-06-26&page=0&size=20&sort=createdAt,desc
```

Query parameters:

| Parameter | Default | Description |
| --- | --- | --- |
| `startDate` | none | Optional inclusive `adviceDate` lower bound |
| `endDate` | none | Optional inclusive `adviceDate` upper bound |
| `page` | `0` | Zero-based page number |
| `size` | `20` | Page size, clamped to `1..100` |
| `sort` | `createdAt,desc` | Allowed fields: `createdAt`, `adviceDate` |

Response `200 OK`:

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [
      {
        "id": 1001,
        "adviceDate": "2026-06-26",
        "startDate": "2026-06-20",
        "endDate": "2026-06-26",
        "adviceText": "Try protecting sleep consistency and increasing water intake on busy days.",
        "modelName": "simplewell-rule-based-v1",
        "createdAt": "2026-06-26T13:00:00Z"
      }
    ],
    "page": {
      "number": 0,
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "sort": ["createdAt,desc"]
    }
  }
}
```

Empty history returns `200 OK` with an empty `content` array.

## Advice Detail

```http
GET /api/ai/advice/{id}
```

Response `200 OK` returns one `AiAdviceDto`. Missing or non-owned advice returns:

```json
{
  "success": false,
  "message": "AI advice not found",
  "errorCode": "RESOURCE_NOT_FOUND"
}
```

The existing `GET /api/ai/advice/latest` endpoint remains available and still returns `404 NO_AI_ADVICE_FOUND` when the user has no advice.

## Chat Conversations

```http
GET /api/ai/chat/conversations?page=0&size=20
```

Response `200 OK`:

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [
      {
        "conversationId": "3a932f06-d628-4a37-9c54-8d2869e99901",
        "startedAt": "2026-07-09T01:00:00Z",
        "lastMessageAt": "2026-07-09T01:02:00Z",
        "messageCount": 2,
        "lastRole": "ASSISTANT",
        "lastMessagePreview": "Keep your sleep schedule steady and review your activity trend again tomorrow."
      }
    ],
    "page": {
      "number": 0,
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "sort": ["lastMessageAt,desc"]
    }
  }
}
```

Conversations are ordered by latest activity first. Empty history returns `200 OK` with an empty `content` array.

## Chat Messages

```http
GET /api/ai/chat/conversations/{conversationId}/messages?page=0&size=50&sort=createdAt,asc
```

Query parameters:

| Parameter | Default | Description |
| --- | --- | --- |
| `page` | `0` | Zero-based page number |
| `size` | `50` | Page size, clamped to `1..100` |
| `sort` | `createdAt,asc` | Allowed field: `createdAt` |

Response `200 OK`:

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "content": [
      {
        "role": "USER",
        "content": "Can you help me understand my recent wellness pattern?",
        "modelName": null,
        "createdAt": "2026-07-09T01:00:00Z"
      },
      {
        "role": "ASSISTANT",
        "content": "Your recent pattern looks steady. Keep a consistent sleep window and continue moderate activity.",
        "modelName": "simplewell-rule-based-v1",
        "createdAt": "2026-07-09T01:02:00Z"
      }
    ],
    "page": {
      "number": 0,
      "size": 50,
      "totalElements": 2,
      "totalPages": 1,
      "sort": ["createdAt,asc"]
    }
  }
}
```

Invalid conversation ids return:

```json
{
  "success": false,
  "message": "conversationId must be a valid UUID",
  "errorCode": "VALIDATION_ERROR"
}
```

Missing or non-owned conversations return:

```json
{
  "success": false,
  "message": "AI chat conversation not found",
  "errorCode": "RESOURCE_NOT_FOUND"
}
```

Unsupported sort fields or directions return `400 VALIDATION_ERROR`.
