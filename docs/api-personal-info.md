# Personal Info API

The personal info API stores the authenticated user's current health profile. It uses metric units only and calculates BMI from the stored height and weight in every response.

Protected endpoints require:

```http
Authorization: Bearer <token>
Accept: application/json
```

Write requests also require:

```http
Content-Type: application/json
```

All responses use the shared API envelopes and include `Cache-Control: no-store`.

## BMI Calculation

BMI is informational only and is not persisted.

```text
bmi = weightKg / (heightCm / 100)^2
```

The API rounds BMI to 1 decimal place using `HALF_UP`.

Example:

```text
68.4 / (172.5 / 100)^2 = 23.0
```

## Fields

| Field | Type | Required | Validation |
| --- | --- | --- | --- |
| `heightCm` | decimal | Yes | `50.0` to `250.0`, max 1 decimal place |
| `weightKg` | decimal | Yes | `2.0` to `500.0`, max 1 decimal place |
| `gender` | string enum | Yes | `MALE`, `FEMALE`, `NON_BINARY`, `PREFER_NOT_TO_SAY` |
| `dateOfBirth` | ISO date | Yes | Must be in the past |
| `activityLevel` | string enum | Yes | `SEDENTARY`, `LIGHTLY_ACTIVE`, `MODERATELY_ACTIVE`, `VERY_ACTIVE` |

Response-only fields:

| Field | Type | Description |
| --- | --- | --- |
| `id` | number | Personal info record id |
| `bmi` | decimal | Calculated BMI rounded to 1 decimal place |
| `createdAt` | ISO instant | Creation timestamp |
| `updatedAt` | ISO instant | Last update timestamp |

## Get Current Personal Info

```http
GET /api/users/me/personal-info
```

Response `200 OK`:

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "heightCm": 172.5,
    "weightKg": 68.4,
    "gender": "MALE",
    "dateOfBirth": "1995-04-12",
    "activityLevel": "MODERATELY_ACTIVE",
    "bmi": 23.0,
    "createdAt": "2026-07-09T02:00:00Z",
    "updatedAt": "2026-07-09T02:00:00Z"
  }
}
```

Response `404 Not Found` when the current user has not entered personal info:

```json
{
  "success": false,
  "message": "Personal info not found",
  "errorCode": "RESOURCE_NOT_FOUND"
}
```

## Create Or Update Current Personal Info

```http
PUT /api/users/me/personal-info
```

Request:

```json
{
  "heightCm": 172.5,
  "weightKg": 68.4,
  "gender": "MALE",
  "dateOfBirth": "1995-04-12",
  "activityLevel": "MODERATELY_ACTIVE"
}
```

Response `201 Created` when creating the first profile, or `200 OK` when updating an existing profile:

```json
{
  "success": true,
  "message": "Personal info created successfully",
  "data": {
    "id": 1,
    "heightCm": 172.5,
    "weightKg": 68.4,
    "gender": "MALE",
    "dateOfBirth": "1995-04-12",
    "activityLevel": "MODERATELY_ACTIVE",
    "bmi": 23.0,
    "createdAt": "2026-07-09T02:00:00Z",
    "updatedAt": "2026-07-09T02:00:00Z"
  }
}
```

## Error Examples

Missing or invalid token returns `401 Unauthorized`:

```json
{
  "success": false,
  "message": "Authentication is required to access this resource",
  "errorCode": "UNAUTHORIZED"
}
```

Validation failure returns `400 Bad Request`:

```json
{
  "success": false,
  "message": "Validation failed",
  "errorCode": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "heightCm",
      "message": "must be greater than or equal to 50.0"
    }
  ]
}
```

Malformed JSON or an unsupported enum value returns `400 Bad Request`:

```json
{
  "success": false,
  "message": "Malformed request body",
  "errorCode": "MALFORMED_JSON"
}
```

Non-JSON write requests return `415 Unsupported Media Type`:

```json
{
  "success": false,
  "message": "Content-Type must be application/json",
  "errorCode": "UNSUPPORTED_MEDIA_TYPE"
}
```
