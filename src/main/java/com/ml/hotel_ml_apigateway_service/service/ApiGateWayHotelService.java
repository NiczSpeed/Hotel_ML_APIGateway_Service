package com.ml.hotel_ml_apigateway_service.service;

import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Service
public class ApiGateWayHotelService {

    private final KafkaTemplate kafkaTemplate;

    private String secretKey = "bUl4RGJBRm11VVlTdlZTeDRhM0pQdlBmODJCcHpxN0NtSXhEYkFGbXVVWVN2VlN4NGEzSlB2UGY4MkJwenE3Qw==";

    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    Logger logger = Logger.getLogger(getClass().getName());

    private final APIGatewayProducerService apiGatewayProducerService;


    public ApiGateWayHotelService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate, DeprecatedTokenRepository deprecatedTokenRepository, APIGatewayProducerService apiGatewayProducerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.apiGatewayProducerService = apiGatewayProducerService;
    }

//    public ResponseEntity<String> createHotelMessage(String message) {
//        CompletableFuture<String> responseFuture = new CompletableFuture<>();
//        String messageId = UUID.randomUUID().toString();
//        responseFutures.put(messageId, responseFuture);
//        String messageWithId = apiGatewayProducerService.attachMessageId(message, messageId);
//        kafkaTemplate.send("create_hotel_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
//        try {
//            String response = responseFuture.get(5, TimeUnit.SECONDS);
//            responseFutures.remove(messageId);
//            logger.info(response);
//            if (response.contains("Hotel with this options can not be created!")) {
//                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//            }
//            return new ResponseEntity<>(message, HttpStatus.CREATED);
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            return new ResponseEntity<>("Timeout or Error while creating new hotel!", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}
