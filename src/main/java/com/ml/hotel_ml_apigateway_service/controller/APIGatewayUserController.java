package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class APIGatewayUserController {

    private final APIGatewayProducerService apiGatewayProducerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String message) {
        return apiGatewayProducerService.registerUserMessage(message);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody String message) {
        return apiGatewayProducerService.loginUserMessage(message);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        return apiGatewayProducerService.logoutUser(request);
    }

    @GetMapping("/info")
    public String welcomeEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Welcome to Ml ApiGateWay Service: " + authentication.getName();
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody String message) {
        return apiGatewayProducerService.updateUserMessage(message);
    }

    @GetMapping("/details")
    public ResponseEntity<String> userDetails() {
        return apiGatewayProducerService.getUserDetails();
    }
}
