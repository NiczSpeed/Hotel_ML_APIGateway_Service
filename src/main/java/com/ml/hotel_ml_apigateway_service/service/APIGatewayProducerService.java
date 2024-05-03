package com.ml.hotel_ml_apigateway_service.service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
public class APIGatewayProducerService {

    private final KafkaTemplate kafkaTemplate;

    public APIGatewayProducerService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//   public void registerUserMessage(String message) {
//        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("register_topic", message);
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                System.out.println(exception.getMessage());
//            } else System.out.println("Message sent successfully");
//        });
//
//    }

    public JSONObject registerUserMessage(String message) {
        JSONObject response = new JSONObject(message);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("register_topic",Base64.getEncoder().encodeToString(message.getBytes()));
        future.whenComplete((result, exception) -> {
            if (exception != null) System.out.println(exception.getMessage());
            else System.out.println("Message sent successfully");
        });
        return response;
    }

    public String loginUserMessage(String message) {

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("login_topic",Base64.getEncoder().encodeToString(message.getBytes()));
        future.whenComplete((result, exception) -> {
            if (exception != null) System.out.println(exception.getMessage());
            else System.out.println("Message sent successfully");
        });
        return message;
    }





}
