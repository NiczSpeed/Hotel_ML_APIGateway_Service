package com.ml.hotel_ml_apigateway_service.service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class APIGatewayProducerService {

    private final KafkaTemplate kafkaTemplate;

    Logger logger = Logger.getLogger(getClass().getName());

    public APIGatewayProducerService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public JSONObject registerUserMessage(String message) {
        JSONObject response = new JSONObject(message);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("register_topic",Base64.getEncoder().encodeToString(message.getBytes()));
        future.whenComplete((result, exception) -> {
            if (exception != null) logger.severe(exception.getMessage());
            else logger.info("Message sent successfully");
        });
        return response;
    }

    public String loginUserMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("login_topic",Base64.getEncoder().encodeToString(message.getBytes()));
        future.whenComplete((result, exception) -> {
            if (exception != null) logger.severe(exception.getMessage());
            else logger.info("Message sent successfully");
        });
        return message;
    }





}
