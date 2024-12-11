package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class ApiGateWayRoomController {

    private final APIGatewayProducerService apiGatewayProducerService;

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody String message) {
        return apiGatewayProducerService.createRoomMessage(message);
    }

}
