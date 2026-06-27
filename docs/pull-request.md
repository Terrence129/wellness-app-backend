# Pull Request: Implement SimpleWell Backend MVP

## Summary

Implements the SimpleWell Spring Boot backend described in `docs/deep-research-report.md`.

## Module branches

- `feature/bootstrap-mysql` - config placeholders, API envelopes, exception handling
- `feature/schema-flyway` - Flyway migrations, MySQL tables, seed data, JPA entities/repositories
- `feature/auth-register-login` - register/login, bcrypt password hashing, JWT generation
- `feature/jwt-security-me` - JWT security, CORS, `/api/users/me`
- `feature/wellness-log-crud` - wellness log create/list/get/update/delete with ownership checks
- `feature/summary-pagination` - weekly summary and page DTO contract
- `feature/ai-advice` - FastAPI client, generated advice persistence, latest advice lookup
- `feature/postman-hardening` - Postman collection/environment and PR documentation

## API coverage

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`
- `POST /api/wellness-logs`
- `GET /api/wellness-logs`
- `GET /api/wellness-logs/date/{logDate}`
- `PUT /api/wellness-logs/{id}`
- `DELETE /api/wellness-logs/{id}`
- `GET /api/wellness-summary/weekly`
- `POST /api/ai/advice`
- `GET /api/ai/advice/latest`

## Database

Uses the existing configured database name `wellness-app` and creates the project tables with Flyway:

- `users`
- `wellness_logs`
- `ai_advice`

Seed data matches the research report. The seeded password for `dadao@example.com` and `alice@example.com` is `password`.

## Postman

- Collection: `postman/SimpleWell Backend.postman_collection.json`
- Environment: `postman/SimpleWell Backend.postman_environment.json`

Run `Auth/Login` first to save `{{token}}`, then run protected requests.

## Verification

- Passed: `mvn -DskipTests compile`
- Passed: `mvn test`

## Notes

- The AI endpoint expects the FastAPI service at `app.ai.base-url`.
- If the AI service is not running, `POST /api/ai/advice` returns `503` with `AI_SERVICE_UNAVAILABLE`.
- User-owned resources never accept `userId` from the client; ownership comes from the JWT principal.
