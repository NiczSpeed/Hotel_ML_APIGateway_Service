# 🔄 Hotel_ML_APIGateway_Service - Central API communication hub

## 📌 Overview
Hotel_ML_APIGateway_Service is a backend microservice based on **Spring Boot**, whose job is to exchange data between **hotel_ml_front** and other microservices thanks to Apache Kafka. It is also responsible for endpoint security thanks to Spring Security, authenticating JWT tokens and saving them to the database as deprecated when the user logs out.

## ❗ Important information
> To launch an application using the described service, go to:
> ➡️ [Main README](https://github.com/NiczSpeed/HotelML?tab=readme-ov-file#%EF%B8%8F-how-to-run-the-entire-system)

📌 **Key features::**
- ✅ Sending messages to other microservices
- ✅ Endpoint security (Spring Security)
- ✅ JWT token authentication
- ✅ Error handling
- ✅ Save deprecated tokens to the database (a deprecated token is when a user logs out)

---

## 🔧 Technologies
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
│   │   ├── DeprecatedTokenDto.java                             # Dto DeprecetedToken
│   ├── exceptions/                                         # Additional exceptions of the microservices
│   │   ├── ErrorWhileDecodeException.java                      # Exception signaling a decoding problem
│   │   ├── ErrorWhileEncodeException.java                      # Exception signaling an encoding problem
│   ├── mapper/                                             # Layer mapping of microservice entities and DTOs
│   │   ├── DeprecatedTokenMapper.java                          # DeprecetedToken Mapper
│   ├── model/                                              # Entity classes
│   │   ├── DeprecatedToken.java                                # DeprecatedToken model
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