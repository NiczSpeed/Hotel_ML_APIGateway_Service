package com.ml.hotel_ml_apigateway_service.service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Service
public class APIGatewayProducerService {

    private final KafkaTemplate kafkaTemplate;

    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    Logger logger = Logger.getLogger(getClass().getName());

    public APIGatewayProducerService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<String> registerUserMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        kafkaTemplate.send("register_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(10, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("User already Exist!")) {
                return new ResponseEntity<>("User already exist!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String loginUserMessage(String message) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("login_topic", Base64.getEncoder().encodeToString(message.getBytes()));
            future.whenComplete((result, exception) -> {
                if (exception != null) logger.severe(exception.getMessage());
                else logger.info("Message sent successfully");
            });

            return message;

        } catch (Exception e) {
            return null;
        }
    }


    @KafkaListener(topics = "error_request_topic_register", groupId = "hotel_ml_apigateway_service")
    public void registerError(String message) {
        String messageId = extractMessageId(message);
        CompletableFuture<String> responseFuture = responseFutures.get(messageId);
        if (responseFuture != null) {
            responseFuture.complete(message);
        }
    }

    @KafkaListener(topics = "success_request_topic_register", groupId = "hotel_ml_apigateway_service")
    public void registerSuccess(String message) {
        String messageId = extractMessageId(message);
        CompletableFuture<String> responseFuture = responseFutures.get(messageId);
        if (responseFuture != null) {
            responseFuture.complete(message);
        }
    }

    private String attachMessageId(String message, String messageId) {
        JSONObject json = new JSONObject(message);
        json.put("messageId", messageId);
        return json.toString();
    }

    private String extractMessageId(String message) {
        JSONObject json = new JSONObject(message);
        return json.optString("messageId");
    }

}
