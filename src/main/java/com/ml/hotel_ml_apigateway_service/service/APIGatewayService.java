package com.ml.hotel_ml_apigateway_service.service;

import lombok.extern.log4j.Log4j;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
public class APIGatewayService {

    private final KafkaTemplate kafkaTemplate;

    public APIGatewayService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate) {
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

    public String registerUserMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("register_topic",Base64.getEncoder().encodeToString(message.getBytes()));
        future.whenComplete((result, exception) -> {
            if (exception != null) System.out.println(exception.getMessage());
            else System.out.println("Message sent successfully");
        });
        return message;
    }
//    public JSONObject save(JSONObject user) {
//
//    }



}
