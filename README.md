# Event-Driven Order Notification System

An **event-driven backend system** built using **Spring Boot** and **Apache Kafka** to process order events and send email notifications in a reliable and fault-tolerant manner.

The project focuses on **real-world backend architecture**, clean separation of concerns, and future scalability.

---

## 🧠 Overview

- Order Service exposes REST APIs to create and update orders
- Order events are published to Kafka topics
- Notification Service consumes Kafka events asynchronously
- Email notifications are sent automatically
- System remains resilient even if external services fail

---

## 🎯 Motivation

The goal of this project is to simulate how real-world backend systems handle  
**asynchronous communication**, **event-driven workflows**, and **external service integration** while maintaining scalability and fault tolerance.

---

## 🏗 Architecture

1. Client (Postman / REST API)
2. Order Service (Spring Boot)
3. Apache Kafka (Event Broker)
4. Notification Service (Spring Boot)
5. Email Service (SMTP / Gmail)

This architecture ensures:
- **Loose coupling** between services  
- **Scalability** through event-driven communication  
- **Fault tolerance** by decoupling producers and consumers  

---

## 🔄 Event Flow

1. Client creates an order using REST API
2. Order Service publishes an `order-event` to Kafka
3. Notification Service consumes the event
4. Email content is generated (optionally using Gemini AI)
5. Email notification is sent to the user
6. Errors are handled gracefully without message loss

---

## 🛠 Tech Stack

- Java  
- Spring Boot  
- Apache Kafka  
- Spring Kafka  
- REST APIs  
- SMTP Email (Gmail)  
- Google Gemini API  
- Maven  
- Git  
- Postman  

---

## 🚀 Key Features

- Event-driven microservices architecture
- Kafka Producer–Consumer implementation
- Reliable email notification workflow
- Fault-tolerant error handling
- Clean architecture (Controller–Service–Repository)
- AI-assisted email generation with graceful fallback

---

## 🤖 AI Integration (Gemini API)

This project integrates **Google Gemini API** to enhance email notifications by:

- Generating dynamic and context-aware email content
- Improving personalization of notifications
- Reducing hard-coded email templates

The AI service is triggered during the **Kafka consumer processing stage**.  
If the AI service is unavailable, the system safely falls back to predefined email content.

---

## 🔮 Future Scope

- Docker and Docker Compose–based deployment
- Dead Letter Queue (DLQ) for failed Kafka events
- Asynchronous processing for external API calls
- Improved observability using logging, metrics, and monitoring tools

---

## ▶ How to Run (Local)

1. Start Apache Kafka and Zookeeper
2. Run the Order Service (Spring Boot)
3. Run the Notification Service (Spring Boot)
4. Use Postman to create orders and trigger Kafka events

### Environment Variables

The following environment variable must be configured before running the application:

- `GEMINI_API_KEY` – API key for Gemini AI service (used for email content generation)

> **Note:** API keys and sensitive credentials are managed using environment variables and are never committed to the repository.

---

## 📌 Learning Outcomes

- Practical understanding of event-driven architecture
- Hands-on experience with Apache Kafka
- Building fault-tolerant backend systems
- Integrating external AI services safely
- Designing scalable and extensible microservices

---
