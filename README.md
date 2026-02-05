# 🛰️ Asteroid Alerting & Notification System

An **event-driven backend system** that monitors Near-Earth Objects (NEOs) using NASA’s public API and sends automated email alerts for potentially hazardous asteroids.

The system is designed using **microservices principles**, **Apache Kafka for event streaming**, and **Docker for infrastructure orchestration**, ensuring scalability, decoupling, and ease of local development.

---

## 📌 Project Purpose

The goal of this project is to:
- Consume real-time asteroid data from NASA
- Process and distribute asteroid alerts asynchronously
- Notify subscribed users via email
- Demonstrate backend system design without a frontend dependency

This project focuses on **backend automation**, **event-driven communication**, and **reliable message processing**.

---

## 🧩 System Components

### 🛰️ Asteroid Alerting Service
- Fetches Near-Earth Object data from NASA NEO API
- Parses asteroid diameter, miss distance, and close-approach date
- Publishes asteroid alert events to Kafka
- Acts as the **event producer**

### 📩 Notification Service
- Consumes asteroid alert events from Kafka
- Persists notification data in MySQL
- Sends scheduled email alerts to users
- Acts as the **event consumer**

---

## 🏗️ System Architecture


                                ┌──────────────────────┐
            │     NASA NEO API     │
            │ (Near Earth Objects) │
            └──────────┬──────────┘
                       │
                       ▼
        ┌────────────────────────────────┐
        │  Asteroid Alerting Service      │
        │  (Spring Boot)                  │
        │                                │
        │  - Fetches asteroid data       │
        │  - Calculates close approaches │
        │  - Builds collision events     │
        │  - Publishes Kafka messages    │
        └──────────┬─────────────────────┘
                   │
                   ▼
        ┌────────────────────────────────┐
        │            Kafka               │
        │  (Docker Container)            │
        │                                │
        │  Topic: asteroid-alert         │
        └──────────┬─────────────────────┘
                   │
                   ▼
        ┌────────────────────────────────┐
        │  Notification Service           │
        │  (Spring Boot)                  │
        │                                │
        │  - Consumes Kafka events        │
        │  - Persists data to MySQL       │
        │  - Triggers scheduled emails    │
        └──────────┬─────────────────────┘
                   │
                   ▼
        ┌────────────────────────────────┐
        │           MySQL                │
        │      (Docker Container)        │
        │                                │
        │  - Stores notifications        │
        │  - Tracks email status         │
        └──────────┬─────────────────────┘
                   │
                   ▼
        ┌────────────────────────────────┐
        │         Email Service           │
        │        (MailTrap / SMTP)        │
        │                                │
        │  - Sends asteroid alerts       │
        │  - One email to all users      │
        └────────────────────────────────┘




---

## 🐳 Docker Infrastructure Components

All infrastructure services run via Docker Compose.

```
┌──────────────────────────────────────────────┐
│ Docker Compose                               │
│                                              │
│  ┌─────────────┐   ┌──────────────┐          │
│  │  Zookeeper  │◄──►│   Kafka     │          │
│  └─────────────┘   └──────────────┘          │
│          │                 │                 │
│          ▼                 ▼                 │
│   ┌─────────────┐   ┌──────────────┐         │
│   │ Kafka UI    │   │ Schema Reg.  │         │
│   └─────────────┘   └──────────────┘         │
│                                              │
│   ┌────────────────────────────────────┐     │
│   │        MySQL 8 Database            │     │
│   └────────────────────────────────────┘     │
└──────────────────────────────────────────────┘
```


<img width="1459" height="633" alt="image" src="https://github.com/user-attachments/assets/a8fb469f-880e-4134-93d2-b08234e4be44" />





---



## ⚙️ Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Kafka
- Spring Data JPA
- Spring Mail
- Lombok

### Infrastructure
- Apache Kafka
- Zookeeper
- MySQL
- Docker & Docker Compose

### External APIs
- NASA Near Earth Object (NEO) API

---

## 🔄 Application Flow

1. Asteroid Alerting Service calls NASA NEO API
2. Asteroid data is parsed and mapped into domain objects
3. Asteroid alert events are published to Kafka
4. Notification Service consumes events from Kafka
5. Events are persisted as notifications in MySQL
6. A scheduled job sends email alerts to subscribed users
7. Notifications are marked as sent after successful delivery

---

## 🔄 End-to-End Flow

1. Fetch Asteroid Data from NASA
   - The system calls the NASA Near-Earth Objects (NEO) API.
   - It retrieves asteroid details along with close-approach information.

2. Process Asteroid Data
   - The Asteroid Alerting Service processes the response.
   - It filters relevant asteroids.
   - It calculates the average asteroid diameter.
   - It extracts miss distance and close-approach date.

3. Create Asteroid Event
   - The processed data is converted into an AsteroidCollisionEvent.

4. Publish Event to Kafka
   - The Asteroid Alerting Service publishes the event to Kafka.
   - The event is sent to the asteroid-alert topic.

5. Store Event in Kafka
   - Kafka (running in Docker) stores the event.
   - The event remains in Kafka until it is consumed.

6. Consume Event in Notification Service
   - The Notification Service listens to the asteroid-alert topic.
   - It consumes the asteroid event from Kafka.

7. Save Notification to Database
   - The Notification Service saves the asteroid alert in MySQL.
   - MySQL runs as a Docker container.

8. Run Scheduled Email Job
   - A scheduled job runs at fixed intervals.
   - It checks for notifications where emailSent = false.

9. Identify Eligible Users
   - The system retrieves users who have notifications enabled.

10. Send Email Alerts
    - The Email Service sends asteroid alert emails.
    - Emails are sent using SMTP (MailTrap).

11. Update Notification Status
    - After successful delivery, the notification is marked as emailSent = true.
    - This prevents duplicate emails.

12. Repeat the Process
    - The system waits for the next asteroid event.

The flow repeats automatically.

---

## 🐳 Docker & Infrastructure Setup

Docker is used to **simplify local setup** and **avoid manual installation** of infrastructure components.

### Services Managed by Docker Compose
- MySQL (database)
- Zookeeper (Kafka coordination)
- Kafka Broker
- Kafka UI (monitoring)
- Kafka Schema Registry

All services run within a shared Docker network for seamless communication.

---

## ▶️ Running the Project

### 1️⃣ Prerequisites
- Java 17 or higher
- Docker & Docker Compose
- Maven

---

### 2️⃣ Start Infrastructure Using Docker

```bash
docker compose up -d

```

This will start the following services:
- MySQL – Notification persistence
- Zookeeper – Kafka coordination
- Kafka Broker – Event streaming
- Kafka UI – Topic monitoring (http://localhost:8084)
- Schema Registry – Kafka schema management

Check running containers:
docker ps


---

### 3️⃣ Run Backend Services

#### 🚀 Asteroid Alerting Service

This service:
- Fetches asteroid data from NASA NEO API
- Processes close-approach details
- Publishes events to Kafka

Service runs on:
http://localhost:8080

Trigger asteroid alert processing:
POST [/api/v1/asteroid-alerting/alert](http://localhost:8080/api/v1/asteroid-alerting/alert)



#### 📬 Notification Service

This service:
- Consumes Kafka asteroid events
- Stores notifications in MySQL
- Sends scheduled email alerts

Service runs on:
http://localhost:8081

---

### 4️⃣ Verify the System

- Kafka topics visible at:
👉 http://localhost:8084

- MySQL running on:
👉 localhost:3307

- Emails sent via configured SMTP server

- Notifications persisted in database 

---


### 5️⃣ Stop the Project

- To stop backend services:
  Ctrl + C

- To stop and remove Docker containers:
  docker compose down

- To remove volumes (⚠️ deletes DB data):
  docker compose down -v

---

## 🗄️ Database Configuration & Table Management

### Database Used
- MySQL 8 (Docker container)
- Spring Data JPA with Hibernate

#### Do I Need to Create Tables Manually?
No. Manual table creation is **not required**.

Tables are automatically created and managed by Hibernate when the Notification Service starts, based on the JPA entity classes.

This is controlled by the following configuration:
```properties
spring.jpa.hibernate.ddl-auto=update
```

---


## 📊 Kafka Monitoring

http://localhost:8084

<img width="1918" height="889" alt="image" src="https://github.com/user-attachments/assets/7698832c-e336-408c-8bb6-abf769ed0fbb" />

## 📧 Email Notification Logic

- Only users with notificationEnabled = true receive emails
- Emails are sent asynchronously
- Failed email attempts do not mark notifications as sent
- Email delivery is scheduled at fixed intervals

        ┌──────────────────────────────────┐
        │  Kafka Topic: asteroid-alert     │
        └───────────────┬──────────────────┘
                        │
                        ▼
        ┌──────────────────────────────────┐
        │  Notification Service             │
        │                                  │
        │  - Kafka Consumer                │
        │  - Persists notification data   │
        │  - Prepares email content       │
        └───────────────┬──────────────────┘
                        │
                        ▼
        ┌──────────────────────────────────┐
        │  Email Scheduler                 │
        │                                  │
        │  - Runs at fixed intervals       │
        │  - Fetches pending notifications │
        └───────────────┬──────────────────┘
                        │
                        ▼
        ┌──────────────────────────────────┐
        │  SMTP Mail Server                │
        │                                  │
        │  - Sends email alerts to users   │
        └──────────────────────────────────┘


---


## 🚀 Future Enhancements

- Frontend Interface
  - A web dashboard to view asteroid alerts
  - Display asteroid size, distance, and close-approach date

- Smarter Alert Logic
  - Send alerts only for potentially dangerous asteroids
  - Allow configuring minimum distance or size for alerts
 
- System Monitoring
  - Add better logs to track failures
  - Monitor Kafka message flow and database operations



---


## 👩‍💻 Author

Jasmine 


