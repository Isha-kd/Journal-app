# 📓 Journal Application (Spring Boot Backend)

A backend-only **Journal Application** built using **Spring Boot and Java**, providing secure RESTful APIs to manage personal journal entries. The current version of this application uses **MongoDB Atlas** for cloud based data storage and implements **Basic Authentication and Role-Based Authorization**, with plans to add **OAuth2 (Google login), caching, deployment, and more advanced features**.

---

## 🚀 Features

- Create journal entries  
- Retrieve all journal entries  
- Retrieve a journal entry by ID  
- Update existing journal entries  
- Delete journal entries  
- Basic Authentication  
- Role-Based Access Control (RBAC)
- Cloud database integration (MongoDB Atlas) 
- RESTful API design  
- Clean layered architecture  

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

---

## 📂 Project Structure

```text
src
├── main
   ├── java
   │   └── com
   │       └── example
   │           └── JournalApplication
   │               ├── config
   │               ├── controller
   │               ├── entity
   │               ├── repository
   │               ├── service
   │               └── JournalApplication.java
   └── resources
       └── application.properties
```
---

## 📌 Planned Enhancements

- Mail features
- OAuth2 authentication (Google Login)
- Caching using Redis
- JWT-based authentication
- User registration and profile management
- API documentation with Swagger
- Deployment on Heroku
- Logging and monitoring
