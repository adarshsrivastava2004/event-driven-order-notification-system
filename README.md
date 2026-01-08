# Event-Driven Order Notification System

An **event-driven backend system** built using **Spring Boot** and **Apache Kafka** to process order events and send email notifications in a reliable and fault-tolerant manner.

The project focuses on **real-world backend architecture**, clean separation of concerns, and future scalability.

---

## 🧠 Overview

- Order Service exposes REST APIs to create and update orders
- Order events are published to Kafka
- Notification Service consumes Kafka events
- Email notifications are sent automatically
- System continues to work even if external services fail

---

## 🏗 Architecture

1. Client (Postman / REST API)
2. Order Service (Spring Boot)
3. Apache Kafka (event broker)
4. Notification Service (Spring Boot)
5. Email Service (SMTP)

This design ensures **loose coupling**, **scalability**, and **fault tolerance**.

---

## 🔄 Event Flow

1. Client creates an order via REST API
2. Order Service publishes an `order-event` to Kafka
3. Notification Service consumes the event
4. Email notification is sent to the user
5. Errors are handled gracefully without message loss

---

## 🛠 Tech Stack

- Java
- Spring Boot
- Apache Kafka
- Spring Kafka
- REST APIs
- SMTP Email
- Maven
- Git
- Postman

---

## 🚀 Key Features

- Event-driven microservices architecture
- Kafka Producer–Consumer implementation
- Reliable email notification system
- Fault-tolerant error handling and retries
- Clean architecture (Controller–Service–Repository)

---

## 🔮 Future Scope

- AI-generated email content using Gemini API
- Docker and Docker Compose–based deployment
- Dead Letter Queue (DLQ) for failed events
- Async processing for external API calls
- Improved observability and monitoring

---

## ▶ How to Run (Local)

1. Start Apache Kafka 
2. Run Order Service
3. Run Notification Service
4. Use Postman to create orders

> Note: API keys and sensitive credentials are managed via environment variables and are not committed to the repository.

---

## 📌 Learning Outcomes

- Understanding of event-driven architecture
- Hands-on experience with Apache Kafka
- Building fault-tolerant backend systems
- Designing future-ready and extensible services
