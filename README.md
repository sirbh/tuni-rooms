# 📍 Tampere University Map Service

This is a microservice-based project that provides map and room-finding services across Tampere University campuses. It is built using Spring Boot and Docker, and features service discovery, API gateway, Redis caching, and a user-friendly frontend.

## 🚀 Project Overview

The system allows users to search and locate rooms across various university buildings with a fast and reliable backend. Each component is modular, containerized, and communicates through a service discovery mechanism.

---

## 🧱 Project Structure

| Folder / File              | Description                                                  |
|---------------------------|--------------------------------------------------------------|
| `discovery/`              | Eureka-based service discovery server.                       |
| `gateway/`                | API gateway with CORS and Redis support.                     |
| `handler/`                | Handles search and map requests.                             |
| `interface/find-room/`    | Frontend UI to search for rooms, upgraded to Spring Boot.    |
| `mapservice/`             | Map data service with CORS and Redis integration.            |
| `docker-compose.yml`     | Orchestrates all services using Docker containers.           |

---

## 🛠️ Technologies Used

- **Java + Spring Boot**
- **Spring Cloud Eureka (Discovery)**
- **Redis (for caching)**
- **Docker + Docker Compose**
- **CORS configuration**
- **Microservice Architecture**

---

## 📝 Recent Changes

- ✅ Added Redis caching and CORS support to gateway, handler, and mapservice.
- 🧭 Replaced HTTP server in `interface/find-room/` with Spring Boot app.
- 📦 Updated Docker Compose for easier deployment and orchestration.


