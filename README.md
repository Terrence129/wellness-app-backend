# Wellness App Backend

Spring Boot backend for the SimpleWell wellness MVP. The API provides JWT-secured authentication, user profile access, wellness log CRUD, weekly summaries, and AI advice generation through an internal FastAPI service.

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL 8
- Flyway
- Maven Wrapper
- Postman collection for API testing

## Features

- Register and login with email/password
- JWT access tokens with issuer, audience, expiry, and `jti`
- Authenticated `/api/users/me`
- Create, list, retrieve, update, and delete wellness logs
- Service-layer ownership checks for user-owned records
- Weekly wellness summary with averages and totals
- AI advice generation via internal FastAPI endpoint
- AI chatbot conversations with persisted message history
- Stable JSON success/error envelopes
- Flyway-managed tables and seed data

## Database

The app uses the existing configured database name:

```text
wellness-app
```

Flyway creates:

- `users`
- `wellness_logs`
- `ai_advice`
- `ai_chat_messages`

Seed users:

```text
dadao@example.com / password
alice@example.com / password
```

## Configuration

Copy `.env.example` values into your local environment or override properties directly.

Important defaults:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/wellness-app?useSSL=false&serverTimezone=Asia/Singapore&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=123456
app.ai.base-url=http://127.0.0.1:8000
```

For production, set a long random `JWT_SECRET` and avoid committing secrets.

## Run Locally

Make sure MySQL is running and the `wellness-app` database exists.

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

If the wrapper has trouble finding PowerShell on Windows, run Maven from the downloaded wrapper distribution under `~/.m2/wrapper/dists`, or add PowerShell to `PATH`.

## Run With Docker

Start the backend and MySQL together:

```bash
docker compose up --build
```

Build the backend image:

```bash
docker build -t wellness-app-backend .
```

Run it against a MySQL database reachable from the container. For MySQL running on the host machine:

```powershell
docker run --rm -p 8080:8080 `
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/wellness-app?useSSL=false&serverTimezone=Asia/Singapore&allowPublicKeyRetrieval=true" `
  -e DB_USERNAME=root `
  -e DB_PASSWORD=123456 `
  -e JWT_SECRET=replace-with-a-long-random-secret-at-least-32-characters `
  -e AI_SERVICE_BASE_URL=http://host.docker.internal:8000 `
  wellness-app-backend
```

The Docker image uses the `docker` Spring profile by default, which expects MySQL at `mysql:3306` when used from a Docker network.

## API Overview

Auth:

- `POST /api/auth/register`
- `POST /api/auth/login`

User:

- `GET /api/users/me`

Wellness logs:

- `POST /api/wellness-logs`
- `GET /api/wellness-logs`
- `GET /api/wellness-logs/date/{logDate}`
- `PUT /api/wellness-logs/{id}`
- `DELETE /api/wellness-logs/{id}`

Summary:

- `GET /api/wellness-summary/weekly`

AI:

- `POST /api/ai/advice`
- `GET /api/ai/advice/latest`
- `POST /api/ai/chat`

Protected endpoints require:

```http
Authorization: Bearer <token>
```

## Response Shape

Success:

```json
{
  "success": true,
  "message": "Success",
  "data": {}
}
```

Error:

```json
{
  "success": false,
  "message": "Validation failed",
  "errorCode": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "email",
      "message": "must be a well-formed email address"
    }
  ]
}
```

## AI Service

Clients call the Spring Boot API. Spring Boot loads the authenticated user, reads persisted logs or chat history, builds the FastAPI request body, and then calls the internal AI service.

`POST /api/ai/advice` builds `{ "userId": ..., "logs": [...] }` from the selected date range and calls:

```text
POST /ai/wellness-advice
```

`POST /api/ai/chat` builds `{ "userId": ..., "message": ..., "history": [...] }` from the current message and stored conversation history, then calls:

```text
POST /ai/chat
```

Chat requests accept an optional `conversationId`. When omitted, the backend creates one and persists both the user message and assistant reply in `ai_chat_messages`.

Configure its base URL with:

```properties
app.ai.base-url=http://127.0.0.1:8000
```

If the AI service is unavailable, the backend returns `503 AI_SERVICE_UNAVAILABLE`.

## Postman

Import both files:

- `postman/SimpleWell Backend.postman_collection.json`
- `postman/SimpleWell Backend.postman_environment.json`

Run `Auth/Login` first. The login script saves `{{token}}` for protected requests.

## Verification

Compile:

```bash
./mvnw -DskipTests compile
```

Run tests:

```bash
./mvnw test
```

The current merged `master` branch was verified with:

```text
mvn test
```

## Branch And PR Records

Local PR-style records are stored in:

```text
docs/pull-requests/
```

The module branches were merged into `master` in order:

1. `feature/bootstrap-mysql`
2. `feature/schema-flyway`
3. `feature/auth-register-login`
4. `feature/jwt-security-me`
5. `feature/wellness-log-crud`
6. `feature/summary-pagination`
7. `feature/ai-advice`
8. `feature/postman-hardening`
