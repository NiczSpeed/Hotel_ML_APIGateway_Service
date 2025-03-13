# 🔄 Hotel_ML_APIGateway_Service - Central API communication hub

## 📖 Table of Contents
1. [📌 Overview](#-overview)
2. [🖥️ Technologies](#-technologies)
3. [📂 Structure of the Code](#-structure-of-the-code)
4. [📊 Diagrams](#-diagrams)

---
## 📌 Overview
Hotel_ML_APIGateway_Service is a backend microservice based on **Spring Boot**, whose job is to exchange data between **hotel_ml_front** and other microservices thanks to Apache Kafka. It is also responsible for endpoint security thanks to Spring Security, authenticating JWT tokens and saving them to the database as deprecated when the user logs out.

## ❗ Important information
> To launch an application using the described service, go to:
> ➡️ [Main README](https://github.com/NiczSpeed/HotelML?tab=readme-ov-file#%EF%B8%8F-how-to-run-the-entire-system)

📌 **Key features::**
- ✅ Sending messages to other microservices
- ✅ Securing endpoints with Spring Securit
- ✅ Authenticating users with JWT tokens  
- ✅ Handling errors
- ✅ Storing deprecated tokens in the database (deprecated tokens are generated when users log out)
- ✅ Encrypting stored and brokered data with AES 

---

## 🖥️ Technologies

| Component       | Technology |
|----------------|------------|
| **JęzykLanguage**  | Java 21 |
| **Framework**  | Spring Boot 3 |
| **Build Tool**  | Gradle (Kotlin) |
| **Database** | PostgreSQL |
| **Communication** | Apache Kafka |
| **Authorization** | Spring Security + JWT |
| **ORM** | Spring Data JPA (Hibernate) |
| **Orchestration** | Docker, Docker Compose |

---

## 📂 Structure of the Code
```plaintext
/backend-service
│── \src\main\java\com\ml\hotel_ml_apigateway_service\
│   ├── configuration/                                      # Microservice configuration layer
│   │   ├── CorsConfiguration.java                              # CORS configuration
│   │   ├── JwtFilter.java                                      # Configuration to support JWT tokens
│   │   ├── KafkaConsumerConfiguration.java                     # Configuring Apache Kafka Consumer
│   │   ├── KafkaProducerConfiguration.java                     # Apache Kafka Producer Configuration
│   │   ├── KafkaTopicsConfiguration.java                       # Configuring Apache Kafka themes
│   │   ├── ObjectMapperConfiguration.java                      # ObjectMapper configuration
│   │   ├── SecurityConfiguration.java                          # Spring Security master configuration for the application
│   ├── controller/                                         # REST API layer
│   │   ├── APIGatewayAdminController.java                      # Admin endpoints
│   │   ├── ApiGateWayHotelController.java                      # Hotel endpoints
│   │   ├── APIGatewayReservationController.java                # Reservation endpoints
│   │   ├── ApiGateWayRoomController.java                       # Rooms endpoints
│   │   ├── APIGatewayUserController.java                       # User endpoints
│   ├── dto/                                                # DTO layer
│   │   ├── DeprecatedTokenDto.java                             # Dto for DeprecetedToken Entity
│   ├── exceptions/                                         # Additional exceptions of the microservices
│   │   ├── ErrorWhileDecodeException.java                      # Exception signaling a decoding problem
│   │   ├── ErrorWhileEncodeException.java                      # Exception signaling an encoding problem
│   ├── mapper/                                             # Layer mapping of microservice entities and DTOs
│   │   ├── DeprecatedTokenMapper.java                          # DeprecetedToken Mapper
│   ├── model/                                              # Entity classes
│   │   ├── DeprecatedToken.java                                # Entity used for saving deprecated tokens
│   ├── repository/                                         # The layer of connection of entities to the database
│   │   ├── DeprecatedTokenRepository.java                      # DeprecatedToken repository
│   ├── service                                             # Business logic layer
│   │   ├── APIGatewayProducerService.java                      # Main class that handles sending and receiving information from other microservices
│   │   ├── DeprecatedTokenService.java                         # DeprecatedToken business logic
│   ├── utils/                                              # Additional functionalities 
│   │   ├── encryptors/                                         # Encryptor layer
│   │   |   ├── StringConverter.java                                # String converter
|   |   |── Encryptor.java                                      # Class inheriting EncryptorUtil to provide data encryption
|   |   |── EncryptorUtil.java                                      # A class containing encryption and decryption methods
|   |── HotelMlApiGatewayServiceApplication.java            # Spring Boot main class
│── src/main/resources/application.yml                      # Application configuration
│──.env                                                 # Environment variables for the Docker container
│── Dockerfile                                          # Docker image definition
│── compose.yml                                         # Launching applications and dependencies
```
## 📊 Diagrams

### 🗂️ Entity-Relationship Diagram (ERD)
This diagram represents the relationships between entities in the database.

🔗 [View the full ERD](docs/ERD/Hotel_ML_APIGateway_Service.svg)

---

### 🏛 Class Diagrams
These diagrams illustrate the main object-oriented structure of the application, including entities, their attributes, methods, and relationships.

---

#### 🛡️ Encryption classes
This diagram illustrates encryption classes in service

🔗 [View the encryption classes](docs/Entity/Hotel_ML_APIGateway_Service_Diagram_encryption.svg)

---

#### 🚨 Exception classes
This diagram illustrates exception classes in service

🔗 [View the exception classes](docs/Entity/Hotel_ML_APIGateway_Service_Diagram_Exceptions.svg)

---

#### ⚙️ Configuration classes
This diagram ilustrates configuration classes in service

🔗 [View the configuration classes](docs/Entity/Hotel_ML_APIGateway_Service_Diagram_Configuration.svg)

---

#### 💬 Message communication classes
This diagram illustrates message communication classes in service.

The diagram presents the concepts of Coordinator, Producer and Listener, defining roles in a Kafka-based and multithreaded architecture.

* Coordinator – retrieves data, opens a new thread, invokes the Producer, and waits up to 5 seconds for a Consumer response.
* Producer – sends data to the appropriate services via Apache Kafka brokers.
* Listener – listens for messages on a specific topic and forwards them for further processing.

🔗 [View the message communication classes](docs/Entity/Hotel_ML_APIGateway_Service_Diagram_Messages_Communication.svg)

---

#### 💼 DeprecatedToken business logic classes
This diagram ilustrates DeprecatedToken business logic classes in service

🔗 [View the DeprecatedToken business logic classes](docs/Entity/Hotel_ML_APIGateway_Service_Diagram_DeprecatedToken.svg)

---