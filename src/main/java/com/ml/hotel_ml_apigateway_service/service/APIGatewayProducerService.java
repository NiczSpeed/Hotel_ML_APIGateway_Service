package com.ml.hotel_ml_apigateway_service.service;


import com.ml.hotel_ml_apigateway_service.dto.DeprecatedTokenDto;
import com.ml.hotel_ml_apigateway_service.exceptions.ErrorWhileEncodeException;
import com.ml.hotel_ml_apigateway_service.mapper.DeprecatedTokenMapper;
import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import com.ml.hotel_ml_apigateway_service.utils.EncryptorUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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

    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    Logger logger = Logger.getLogger(getClass().getName());

    private final DeprecatedTokenRepository deprecatedTokenRepository;
    private final EncryptorUtil encryptorUtil;


    public APIGatewayProducerService(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate, DeprecatedTokenRepository deprecatedTokenRepository, EncryptorUtil encryptorUtil) {
        this.kafkaTemplate = kafkaTemplate;
        this.deprecatedTokenRepository = deprecatedTokenRepository;
        this.encryptorUtil = encryptorUtil;
    }

    public ResponseEntity<String> registerUserMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(message, messageId);
        JSONObject json = new JSONObject(message);
        String email = json.getString("email");
        if (!email.contains("@")) {
            return new ResponseEntity<>(validateFieldsFromJson("Something is wrong with email address!", messageId), HttpStatus.BAD_REQUEST);
        }
        sendEncodedMessage(json.toString(), messageId, "register_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("User already Exist!")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(responseMessage(messageWithId), HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> loginUserMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        sendEncodedMessage(message, messageId, "login_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Invalid username or password!")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
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
        String message = new JSONObject().put("email", getAuthenticatedUser()).toString();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        sendEncodedMessage(message, messageId, "user_details_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            response = response.replaceAll("\\\\", "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> grantAdminMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonMessage = new JSONObject(message);
        jsonMessage.put("grantorEmail", authentication.getName());
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(jsonMessage.toString(), messageId);
        sendEncodedMessage(jsonMessage.toString(), messageId, "grant_admin_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(responseMessage(messageWithId), HttpStatus.OK);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createHotelMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        JSONObject jsonMessage = new JSONObject(message);
        String messageWithId = attachMessageId(message, messageId);
        sendEncodedMessage(jsonMessage.toString(), messageId, "create_hotel_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(responseMessage(messageWithId), HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new hotel!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createRoomMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        JSONObject jsonMessage = new JSONObject(message);
        String messageWithId = attachMessageId(message, messageId);
        sendEncodedMessage(jsonMessage.toString(), messageId, "create_room_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(responseMessage(messageWithId), HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            jsonMessage.clear();
            jsonMessage.put("message", "Timeout or Error while adding new room!");
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> createReservationMessage(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonMessage = new JSONObject(message);
        jsonMessage.put("clientEmail", authentication.getName());
        responseFutures.put(messageId, responseFuture);
        String messageWithId = attachMessageId(jsonMessage.toString(), messageId);
        sendEncodedMessage(jsonMessage.toString(), messageId, "create_reservation_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(responseMessage(messageWithId), HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getFreeHotelsSet(String city, LocalDate startDate, LocalDate endDate, Long numberOfBeds) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("city", city);
        jsonMessage.put("startDate", startDate);
        jsonMessage.put("endDate", endDate);
        jsonMessage.put("numberOfBeds", numberOfBeds);
        if (startDate.isAfter(endDate)) {
            logger.severe("Starting date can't be after ending date!");
            return new ResponseEntity<>(validateFieldsFromJson("Starting date can't be after ending date!", messageId), HttpStatus.BAD_REQUEST);
        }
        if (numberOfBeds < 1) {
            logger.severe("Number of beds can't be less than 1!");
            return new ResponseEntity<>(validateFieldsFromJson("Number of beds can't be less than 1!", messageId), HttpStatus.BAD_REQUEST);
        }
        sendEncodedMessage(jsonMessage.toString(), messageId, "request_free_hotels_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                response = response.replaceAll("\\\\", "");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getAllHotels(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("city", message);
        sendEncodedMessage(jsonMessage.toString(), messageId, "request_all_hotels_by_city_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                response = response.replaceAll("\\\\", "");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getHotelsCities() {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        sendEncodedMessage(null, messageId, "request_all_hotels_cities_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {

                response = response.replaceAll("\\\\", "");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getReservationPrice(String message) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        JSONObject jsonMessage = new JSONObject(message);
        responseFutures.put(messageId, responseFuture);
        sendEncodedMessage(jsonMessage.toString(), messageId, "check_room_reservation_price_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getAllUserReservations() {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String messageId = UUID.randomUUID().toString();
        responseFutures.put(messageId, responseFuture);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("email", authentication.getName());
        sendEncodedMessage(jsonMessage.toString(), messageId, "all_user_reservation_request_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {

                response = response.replaceAll("\\\\", "");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while processing registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteReservationByUuid(String uuid) {
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        String messageId = UUID.randomUUID().toString();
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("uuid", uuid);
        responseFutures.put(messageId, responseFuture);
        sendEncodedMessage(jsonMessage.toString(), messageId, "delete_reservation_topic");
        try {
            String response = responseFuture.get(5, TimeUnit.SECONDS);
            responseFutures.remove(messageId);
            if (response.contains("Error")) {
                response = response.replace("Error:", "");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new ResponseEntity<>("Timeout or Error while adding new room!", HttpStatus.INTERNAL_SERVER_ERROR);
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
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "response_create_reservation_topic", groupId = "hotel_ml_apigateway_service")
    public void earnReservationRequest(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "user_details_request_topic", groupId = "hotel_ml_apigateway_service")
    public void earnUserDetails(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "response_free_hotels_topic", groupId = "hotel_ml_apigateway_service")
    public void earnFreeHotelsSet(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "response_all_hotels_by_city_topic", groupId = "hotel_ml_apigateway_service")
    public void earnAllHotelsByCity(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "response_all_hotels_cities_topic", groupId = "hotel_ml_apigateway_service")
    public void earnAllHotelsCities(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "all_user_reservation_response_topic", groupId = "hotel_ml_apigateway_service")
    public void earnAllUserReservations(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @KafkaListener(topics = "room_price_topic", groupId = "hotel_ml_apigateway_service")
    public void earnReservationPrice(String message) {
        try {
            getRequestMessage(encryptorUtil.decrypt(message));
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    private void sendEncodedMessage(String message, String messageId, String topic) {
        try {
            JSONObject json = new JSONObject();
            json.put("messageId", messageId);
            if (message != null) {
                switch (message) {
                    case String s when s.contains("[") -> json.put("message", new JSONArray(s));
                    case String s when s.contains("{") -> json.put("message", new JSONObject(s));
                    default -> json.put("message", message);
                }
            }
            String encodedMessage = encryptorUtil.encrypt(json.toString());
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, encodedMessage);
            future.whenComplete((result, exception) -> {
                if (exception != null) logger.severe(exception.getMessage());
                else logger.info("Message send successfully!");
            });
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    private String decodeMessage(String message) {
        byte[] decodedBytes = Base64.getDecoder().decode(message);
        return new String(decodedBytes);
    }

    private void getRequestMessage(String message) {
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

    private String responseMessage(String message) {
        JSONObject newJson = new JSONObject();
        JSONObject messageJson = new JSONObject(message);
        String messageId = messageJson.getString("messageId");
        messageJson.remove("messageId");
        newJson.put("message", messageJson);
        newJson.put("messageId", messageId);
        return newJson.toString();
    }

    private String extractMessageId(String message) {
        JSONObject json = new JSONObject(message);
        return json.optString("messageId");
    }

    private String validateFieldsFromJson(String message, String messageId) {
        JSONObject json = new JSONObject();
        json.put("messageId", messageId);
        json.put("message", message);
        return json.toString();
    }
}
