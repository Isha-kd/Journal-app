# 📓 Journal Application (Spring Boot Backend)

A backend-only **Journal Application** built using **Spring Boot and Java**, providing secure RESTful APIs to manage personal journal entries. The current version of this application uses **MongoDB Atlas** for cloud based data storage and implements **Basic Authentication and Role-Based Authorization**, with plans to add **OAuth2 (Google login), caching, deployment, and more advanced features**.

---

## 🚀 Features

- CRUD operations for journal entries
- Basic Authentication for users
- Role-Based Access Control (RBAC)
- Cloud database integration (MongoDB Atlas) 
- RESTful API design  
- Logging and monitoring 
- Clean layered architecture  
- Profile-based configuration management
- Mail service for notifications and weekly reminders

---

## 🔐 Security

- **Basic Authentication**
- **Role-Based Authorization**
  - ADMIN and USER roles
  - Endpoint-level access control using Spring Security

---

## 🛠️ Tech Stack

- Java  
- Spring Boot  
- Spring Web  
- Spring Security  
- Spring Data JPA  
- MongoDB Atlas  
- Maven  
- SLF4J / Logback
- Spring Mail

---

## 📂 Project Structure

```text
src
├── main
   ├── java
   │   └── com
   │       └── spring
   │           └── JournalApplication
   │               ├── config
   │               ├── controller
   │               ├── entity
   │               ├── repository
   │               ├── scheduler
   │               ├── service
   │               └── JournalApplication.java
   └── resources
       ├── application.properties
       └── logback.xml
```
---

## 📌 Planned Enhancements

- OAuth2 authentication (Google Login)
- Caching using Redis
- JWT-based authentication
- User registration
- API documentation with Swagger
- Deployment on Heroku
