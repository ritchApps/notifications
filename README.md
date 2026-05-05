# Notification System

## The Problem

Users subscribe to message categories (Sports, Finance, Movies) and specify which channels (SMS, Email, Push Notification) they want to receive notifications through. When a message is published to a category, the system automatically routes it to every subscribed user through each of their configured channels.

## Architecture Decisions

### Strategy Pattern

`NotificationChannel` is an interface implemented by `SmsChannel`, `EmailChannel`, and `PushNotificationChannel`. Spring injects a `List<NotificationChannel>` into `NotificationDispatchService`, which maps it to `Map<ChannelType, NotificationChannel>` for O(1) lookup at dispatch time. Adding a new channel requires only a new `@Service` class that implements the interface — zero changes to existing code.

### Observer Pattern

`MessageService` persists the message and then publishes a `MessageReceivedEvent` via `ApplicationEventPublisher`. The listener uses `@TransactionalEventListener(phase = AFTER_COMMIT)`, which guarantees the message is fully committed to the database before dispatch begins. This decouples persistence from notification delivery.

### Fault Tolerance

Each channel delivery attempt is wrapped in an independent `try/catch`. A failure on one channel does not affect other channels or other users. Every attempt — successful or not — is recorded in `notification_logs` with a status of `DELIVERED` or `FAILED` and an error message when applicable.

### Architecture Layers

```
Controllers → Services → Repositories
```

- DTOs are used for all API inputs and outputs; entities are never exposed directly
- MapStruct handles entity-to-DTO mapping
- Bean Validation (`@Valid`) is applied on all request DTOs
- `@RestControllerAdvice` centralizes exception handling
- Constructor injection is used throughout — no field injection

### Why Spring Events over Kafka

The system operates within a single JVM with no distributed processing requirement. Spring Application Events provide the same decoupling at zero infrastructure cost. The migration path is straightforward: replace `ApplicationEventPublisher` with `KafkaTemplate` and the listener with `@KafkaListener`.

### Database

PostgreSQL with Flyway-managed migrations. The schema enforces referential integrity via foreign keys, uses composite indexes on high-traffic join columns, and includes a `CHECK` constraint on `notification_logs.status` to prevent invalid values at the database level.

## Tech Stack

| Technology | Version |
|---|---|
| Java | 17 |
| Spring Boot | 4.x |
| Spring Data JPA | - |
| PostgreSQL | 16 |
| Flyway | - |
| MapStruct | - |
| JUnit 5 + Mockito | - |
| Maven | 3.8+ |

## Prerequisites

- Docker and Docker Compose
- Java 17+
- Maven 3.8+

## Running the Project

**Step 1** — Start the database:
```bash
docker-compose up -d
```

**Step 2** — Start the application:
```bash
./mvnw spring-boot:run
```

**Step 3** — Open in your browser:
```
http://localhost:8080
```

## Running the Tests

```bash
./mvnw test
```

> 24 tests across services and notification channels.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/messages` | Send a message |
| GET | `/api/messages/categories` | List available categories |
| GET | `/api/logs` | Get all logs, newest first |
| GET | `/api/logs/filter` | Filter logs by status, category, or channel |

## Environment Variables

| Variable | Default |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/notifications_db` |
| `SPRING_DATASOURCE_USERNAME` | `notifications_user` |
| `SPRING_DATASOURCE_PASSWORD` | `notifications_pass` |

## Pre-populated Users

| Name | Subscribed Categories | Channels |
|---|---|---|
| Alice Johnson | Sports, Finance | SMS, Email |
| Bob Martinez | Sports | Email |
| Carol Williams | Finance, Movies | SMS, Push |
| David Chen | Sports, Movies | Email, Push |
| Eva Rodriguez | Finance, Movies | SMS |
| Frank Kim | Sports, Finance, Movies | SMS, Email, Push |
| Grace Lee | Sports | SMS, Email |
| Henry Park | Finance | Email, Push |
| Iris Wang | Movies | SMS, Push |
| Jack Brown | Sports, Finance, Movies | SMS, Email, Push |

## Future Improvements

- **Kafka** for distributed deployments and dead-letter queue (DLQ) support
- **`@Retryable`** with exponential backoff for transient channel failures
- **Transactional Outbox Pattern** for guaranteed event delivery on JVM crash
- **Pagination** for the log history endpoint
