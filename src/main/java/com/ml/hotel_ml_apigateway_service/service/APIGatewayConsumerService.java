package com.ml.hotel_ml_apigateway_service.service;

import ch.qos.logback.core.subst.Token;
import com.ml.hotel_ml_apigateway_service.dto.UserTokenDto;
import com.ml.hotel_ml_apigateway_service.repository.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.micrometer.observation.GlobalObservationConvention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.util.Base64;

import static com.ml.hotel_ml_apigateway_service.mapper.UserTokenMapper.Instance;

@Service
public class APIGatewayConsumerService {

    private String secretKey = "bUl4RGJBRm11VVlTdlZTeDRhM0pQdlBmODJCcHpxN0NtSXhEYkFGbXVVWVN2VlN4NGEzSlB2UGY4MkJwenE3Qw==";

    private final KafkaTemplate kafkaTemplate;

    private final UserTokenRepository userTokenRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public APIGatewayConsumerService(KafkaTemplate kafkaTemplate, UserTokenRepository userTokenRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userTokenRepository = userTokenRepository;
    }

    @KafkaListener(topics = "jwt_topic", groupId = "hotel_ml_apigateway_service")
    public void earnJwtToken(String message){
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(message);
            message = new String(decodedBytes);
            UserTokenDto token = new UserTokenDto();
            token.setToken(message);
            Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(message).getPayload();
            token.setEmail(claims.getSubject());
            token.setRoles(claims.get("roles", List.class).toString());
            token.setExpiryDate(claims.getExpiration());

            logger.info("Token: " + token.getToken());
            logger.info("Email: " + token.getEmail());
            logger.info("Roles: " + token.getRoles());
            logger.info("ExpiryDate: " + token.getExpiryDate());

            userTokenRepository.save(Instance.mapUserTokenDtoToUserToken(token));

        }catch (Exception e){
            logger.info("Invalid Data");
        }
    }



    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
