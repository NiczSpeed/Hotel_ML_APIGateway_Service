package com.ml.hotel_ml_apigateway_service.service;


import com.ml.hotel_ml_apigateway_service.dto.DeprecatedTokenDto;
import com.ml.hotel_ml_apigateway_service.mapper.DeprecatedTokenMapper;
import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;


@Service
public class APIGatewayProducerService {

    private final KafkaTemplate kafkaTemplate;

    private String secretKey = "bUl4RGJBRm11VVlTdlZTeDRhM0pQdlBmODJCcHpxN0NtSXhEYkFGbXVVWVN2VlN4NGEzSlB2UGY4MkJwenE3Qw==";

    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    Logger logger = Logger.getLogger(getClass().getName());

    private final DeprecatedTokenRepository deprecatedTokenRepository;


    public APIGatewayProducerService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate, DeprecatedTokenRepository deprecatedTokenRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.deprecatedTokenRepository = deprecatedTokenRepository;
    }

    public ResponseEntity<String> registerUserMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        JSONObject json = new JSONObject(message);
        String email = json.getString("email");
        if(!email.contains("@")){
            return new ResponseEntity<>(validateFieldsFromJson(json, "Something is wrong with email address!", messageId), HttpStatus.BAD_REQUEST);
        }
        kafkaTemplate.send("register_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("User already Exist!")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> loginUserMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        kafkaTemplate.send("login_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("Invalid username or password!")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                String header = request.getHeader("Authorization");
                String token = header.substring(7);
                DeprecatedTokenDto tokenDto = new DeprecatedTokenDto();
                tokenDto.setToken(token);
                deprecatedTokenRepository.saveAndFlush(DeprecatedTokenMapper.Instance.mapDeprecatedTokenToDeprecatedTokenDto(tokenDto));
                return new ResponseEntity<>("Successful logout!", HttpStatus.OK);
            }
            return new ResponseEntity<>("You are not logged in!", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Authorization header not found!", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<String> getUserDetails() {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String message = "{email:" + getAuthenticatedUser() + "}";
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        kafkaTemplate.send("user_details_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            response = new StringBuilder(removeMessageIdFromMessage(response)).reverse().delete(0, 2).reverse().toString().replaceAll("\\\\", "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createHotelMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        kafkaTemplate.send("create_hotel_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new hotel!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createRoomMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        kafkaTemplate.send("create_room_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createReservationMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JSONObject json = new JSONObject(message);
        json.put("clientEmail", authentication.getName());
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(json.toString(), messageId);
        kafkaTemplate.send("create_reservation_topic", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            logger.info(response);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getFreeHotelsSet(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        JSONObject json = new JSONObject(message);

        LocalDate startDate = LocalDate.parse(json.optString("startDate"));
        LocalDate endDate = LocalDate.parse(json.optString("endDate"));
        if (startDate.isAfter(endDate)) {
            return new ResponseEntity<>(validateFieldsFromJson(json, "Starting date can't be after ending date!", messageId), HttpStatus.BAD_REQUEST);
        }

        kafkaTemplate.send("request_free_hotels", Base64.getEncoder().encodeToString(messageWithId.getBytes()));
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                response = removeMessageIdFromMessage(response);
                response = new StringBuilder(response).delete((response.length() - 4 ) ,response.length()-1).toString().replaceAll("\\\\", "");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    @KafkaListener(topics = "error_request_topic", groupId = "hotel_ml_apigateway_service")
    private void registerError(String message) {
        getRequestMessage(message);
    }

    @KafkaListener(topics = "success_request_topic", groupId = "hotel_ml_apigateway_service")
    private void registerSuccess(String message) {
        getRequestMessage(message);
    }

    @KafkaListener(topics = "jwt_topic", groupId = "hotel_ml_apigateway_service")
    public void earnJwtToken(String message) {
        getRequestMessage(decodeMessage(message));
    }

    @KafkaListener(topics = "user_details_request_topic", groupId = "hotel_ml_apigateway_service")
    public void earnUserDetails(String message) {
        getRequestMessage(decodeMessage(message));
    }

    @KafkaListener(topics = "response_free_hotels", groupId = "hotel_ml_apigateway_service")
    public void earnFreeHotelsSet(String message) {
        logger.severe(message);
        getRequestMessage(decodeMessage(message));
    }


    String decodeMessage(String message) {
        byte[] decodedBytes = Base64.getDecoder().decode(message);
        return new String(decodedBytes);
    }

    void getRequestMessage(String message) {
        String messageId = extractMessageId(message);
        CompletableFuture<String> responseFuture = responseFutures.get(messageId);
        if (responseFuture != null) {
            responseFuture.complete(message);
        }
    }

    String attachMessageId(String message, String messageId) {
        JSONObject json = new JSONObject(message);
        json.put("messageId", messageId);
        return json.toString();
    }

    String extractMessageId(String message) {
        JSONObject json = new JSONObject(message);
        return json.optString("messageId");
    }

    String removeMessageIdFromMessage(String message) {
        return new StringBuilder(message).delete(1, 65).toString();
    }

    String validateFieldsFromJson(JSONObject json, String message, String messageId) {
        json.clear();
        json.put("messageId", messageId);
        json.put("message", message);
        return json.toString();
    }


}
