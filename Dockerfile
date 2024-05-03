FROM eclipse-temurin:21-jdk-alpine
WORKDIR /Hotel_ML_APIGateway_Service
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar Hotel_ML_APIGateway_Service-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","Hotel_ML_APIGateway_Service-0.0.1-SNAPSHOT.jar"]