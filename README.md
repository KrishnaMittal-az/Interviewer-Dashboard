# Interview Scheduler

Spring Boot + MySQL sample service that demonstrates a simple interview scheduling system (Clean Architecture style).

## Purpose
- Manage interviewer weekly availability
- Generate time slots from weekly availability
- Allow candidates to browse and book open slots

## Prerequisites
- Java 17+
- Maven
- MySQL 8.0+

## Quick start

1. Create the database (MySQL):

```sql
CREATE DATABASE interview_scheduler;
```

2. Configure credentials

Edit `src/main/resources/application.yml` and set your DB username/password (and JDBC URL if not default).

3. Build and run

Recommended (Maven):

```bash
mvn -DskipTests clean package
mvn spring-boot:run
```

Run on a different port (example using environment override in PowerShell):

```powershell
#$env:SPRING_APPLICATION_JSON='{"server":{"port":8081}}'; mvn spring-boot:run
```

Or build and run the produced jar:

```bash
mvn -DskipTests package
java -jar target/interview-scheduler-0.0.1-SNAPSHOT.jar --server.port=8081
```


## Database migrations
- Flyway runs automatically on startup and applies `src/main/resources/db/migration`.

## Web UI & APIs
- Basic UI: `http://localhost:8080/` (slot browsing/booking)
- Swagger/OpenAPI: `http://localhost:8080/swagger-ui.html`

## Key REST endpoints (v1)
- `POST /api/v1/interviewers` — create interviewer
- `PUT /api/v1/interviewers/{id}/availability` — set weekly availability
- `PUT /api/v1/interviewers/{id}/capacity/{capacity}` — update weekly capacity
- `POST /api/v1/interviewers/{id}/slots/generate?days=14` — generate slots
- `GET /api/v1/slots?from={iso}&to={iso}&interviewerId={id}&limit={n}` — list open slots (cursor pagination)
- `POST /api/v1/bookings/slot/{slotId}` — book a slot (body: `{ "candidateId": <id> }`)
- `POST /api/v1/candidates` — register candidate

## Troubleshooting
- If the server fails to start because the port is in use, change the port using `--server.port=<port>` or the `SPRING_APPLICATION_JSON` env trick above.
- Flyway validates migrations; ensure the DB user has privileges to create/alter tables.
- The app prints a generated development security password on first startup; update `SecurityConfig` for production.

---

Manual cleanup commands (PowerShell):

```powershell
# backup files then remove
New-Item -ItemType Directory -Path .\\archive -Force
Move-Item -Path TODO.md,RUN.md,run.bat -Destination .\\archive\\
# replace README after review
Move-Item README_UPDATED.md README.md -Force
```
