# 📬 Notification System

A scalable and extensible notification system that delivers messages to users based on their category subscriptions and preferred communication channels (SMS, Email, Push Notification).

## 🚀 Overview

This system allows sending categorized messages to users. Each user is subscribed to specific categories and notification channels, ensuring they only receive relevant messages through their preferred mediums.

The system is designed with extensibility, separation of concerns, and fault tolerance in mind.

## 🧩 Features

- **Send messages by category:** Sports, Finance, Movies.
- **Notify users based on:**
    - Subscribed categories.
    - Preferred channels: SMS, Email, Push.
- **Multi-channel delivery:** Implemented using the Strategy Pattern.
- **Decoupled processing:** Using an event-driven architecture with Spring Application Events.
- **Notification logging:** Full audit trail for debugging and monitoring.
- **Operational UI:** A dedicated interface for sending messages and monitoring logs.
- **Fault tolerance:** Independent failure handling per channel.
- **Testing coverage:** Unit tests for services and notification strategies.

## 🏗️ Architecture

The system follows a layered architecture to ensure separation of concerns:
`Controller → Service → Repository → Database`

### Key Design Decisions
- **Strategy Pattern:** Encapsulates notification logic for each channel, allowing new channels to be added with zero changes to existing dispatch code.
- **Event-Driven Design:** Decouples the message persistence logic from the notification delivery process.
- **Transactional Events (AFTER_COMMIT):** Guarantees that notifications are only triggered if the message was successfully saved to the database.
- **Dependency Injection:** Uses constructor injection throughout to promote testability and loose coupling.

## 🔄 End-to-End Flow

1.  **Submission:** A message is submitted via the UI.
2.  **Persistence:** The message is validated and saved to PostgreSQL.
3.  **Event Trigger:** A `MessageReceivedEvent` is published.
4.  **Dispatch:**
    - The listener picks up the event after the transaction commits.
    - `NotificationDispatchService` identifies subscribed users for the category.
    - For each user, it iterates through their preferred channels.
    - Each channel implementation executes its delivery logic independently.
5.  **Logging:** Results (DELIVERED or FAILED) are persisted in `notification_logs`.

## 🧠 Design Patterns Used

- **Strategy Pattern:** Dynamically selects the appropriate `NotificationChannel` implementation.
- **Observer/Event Pattern:** Decouples the core business logic from the notification side-effects.
- **Repository Pattern:** Abstracts the data access layer using Spring Data JPA.

## 🗄️ Database Design

Relational schema managed via Flyway migrations:
- **Foreign Keys & Constraints:** Enforces data integrity at the database level.
- **Indexing:** Optimized for high-traffic operations like filtering logs by date or category.
- **Audit Logs:** Captures user data, message body, channel type, status, and error messages for full observability.

## 🖥️ User Interface

The UI consists of two main sections:
1.  **Submission Form:** Simple validation to ensure messages are not empty and categories are selected.
2.  **Log History:** A real-time view of notification attempts, sortable from newest to oldest, with filtering capabilities by status, category, and channel.

## 🧪 Testing Strategy

Unit tests cover multiple scenarios to ensure reliability:
- **Success Paths:** Correct delivery to multiple users and channels.
- **Failure Paths:** Verification that a failure in one channel does not stop the delivery process for others.
- **Logic Validation:** Ensuring users only receive messages for their subscribed categories.

## ⚖️ Trade-offs & Technical Decisions

This implementation balances simplicity for an assessment with the structural integrity required for a production system.

- **Synchronous Dispatch:** Events are decoupled logically, but currently run on the same execution thread. In a high-load environment, I would add `@Async` to the listener.
- **Enum Fragility:** Current persistence uses Enum ordinal mapping for speed of development. I recognize that `EnumType.STRING` or `AttributeConverters` are safer for evolving systems.
- **Blocking Loop:** The dispatch logic iterates through users and channels in a single pass. For a massive subscriber base, this would be replaced by batching and worker-pool processing.
- **Infrastructure Overhead:** I chose Spring internal events over a message broker like Kafka to keep the project's infrastructure footprint minimal while maintaining a clear migration path.

## 🚀 Future Improvements

- **Message Broker:** Integrate Kafka or RabbitMQ for horizontal scalability and persistent queues.
- **Transactional Outbox Pattern:** Ensure "at-least-once" delivery even in the event of a JVM crash.
- **Retry Mechanisms:** Implement exponential backoff for transient channel failures.
- **Pagination:** Add server-side pagination for the Log History UI to handle millions of records.

## 🧪 Failure Simulation

The system includes a failure simulation mechanism within the channels (random 20% failure rate) to demonstrate fault tolerance and how errors are captured in the logs.

## 🛠️ Tech Stack

- **Backend:** Java 17 / Spring Boot 4.0.6
- **Database:** PostgreSQL 16 (with Flyway)
- **Mapping:** MapStruct
- **Testing:** JUnit 5 / Mockito
- **Environment:** Docker Compose

## ▶️ Running the Project

1. **Start the database:**
   ```bash
   docker-compose up -d
   
2. Run the application:

Bash
./mvnw spring-boot:run

3. Access the UI: http://localhost:8080


## 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/messages` | Send a message |
| GET | `/api/messages/categories` | List available categories |
| GET | `/api/logs` | Get all logs, newest first |
| GET | `/api/logs/filter` | Filter by status, category, channel |
| GET | `/api/simulation` | Get fault simulation status |
| PUT | `/api/simulation` | Enable or disable fault simulation |



## ⚙️ Environment Variables

| Variable | Default |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/notifications_db` |
| `SPRING_DATASOURCE_USERNAME` | `notifications_user` |
| `SPRING_DATASOURCE_PASSWORD` | `notifications_pass` |



## 👥 Pre-populated Users

| Name | Categories | Channels |
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
