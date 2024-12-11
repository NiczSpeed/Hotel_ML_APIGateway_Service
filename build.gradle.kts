plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.ml"
version = "0.0.1"


val jsonVersion = "20240303"
val jwtVersion = "0.12.5"
val mapstructVersion = "1.5.5.Final"
val springSecurityVersion = "6.3.1"
val springStarterWebVersion = "3.3.2"


java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}




repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-core:${springSecurityVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springStarterWebVersion}")
    implementation("org.json:json:${jsonVersion}")
    implementation ("com.fasterxml.jackson.core:jackson-core")
    implementation ("com.fasterxml.jackson.core:jackson-databind")
    implementation ("com.fasterxml.jackson.core:jackson-annotations")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.jsonwebtoken:jjwt-api:${jwtVersion}")
    implementation("io.jsonwebtoken:jjwt-impl:${jwtVersion}")
    implementation("io.jsonwebtoken:jjwt-jackson:${jwtVersion}")
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
