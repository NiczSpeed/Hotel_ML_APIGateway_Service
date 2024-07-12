package com.ml.hotel_ml_apigateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class HotelMlApiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelMlApiGatewayServiceApplication.class, args);
    }

}
