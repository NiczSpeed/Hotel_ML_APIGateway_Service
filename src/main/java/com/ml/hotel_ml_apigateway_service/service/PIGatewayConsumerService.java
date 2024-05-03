package com.ml.hotel_ml_apigateway_service.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class PIGatewayConsumerService {

    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public PIGatewayConsumerService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "jwt_topic", groupId = "hotel_ml_apigateway_service")
    public void earnJwtToken(String message){
        byte[] decodedBytes = Base64.getDecoder().decode(message);
        message = new String(decodedBytes);
        System.out.println(message);
    }

}
