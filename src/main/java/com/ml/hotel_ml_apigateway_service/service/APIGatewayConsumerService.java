package com.ml.hotel_ml_apigateway_service.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;



@Service
public class APIGatewayConsumerService {

    private String secretKey = "bUl4RGJBRm11VVlTdlZTeDRhM0pQdlBmODJCcHpxN0NtSXhEYkFGbXVVWVN2VlN4NGEzSlB2UGY4MkJwenE3Qw==";

    private final KafkaTemplate kafkaTemplate;

    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public APIGatewayConsumerService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//    @KafkaListener(topics = "jwt_topic", groupId = "hotel_ml_apigateway_service")
//    public void earnJwtToken(String message){
//        try {
//            byte[] decodedBytes = Base64.getDecoder().decode(message);
//            message = new String(decodedBytes);
//            UserTokenDto token = new UserTokenDto();
//            token.setToken(message);
//            Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(message).getPayload();
//            token.setEmail(claims.getSubject());
//            token.setRoles(claims.get("roles", List.class).toString());
//            token.setExpiryDate(claims.getExpiration());
//
//            userTokenRepository.save(Instance.mapUserTokenDtoToUserToken(token));
//
//        }catch (Exception e){
//            logger.info("Invalid Data");
//        }
//    }



    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
