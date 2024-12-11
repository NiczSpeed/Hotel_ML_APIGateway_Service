package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class APIGatewayAdminController {

    private final APIGatewayProducerService apiGatewayService;

    @GetMapping("/info")
    public String welcomeEndpoint() {
        return "Welcome to Ml ApiGateWay Service, ur role is ADMIN!";
    }

    @PatchMapping("/grant")
    public ResponseEntity<String> grantAdmin(@RequestBody String message) {
        return apiGatewayService.grantAdminMessage(message);
    }

}
