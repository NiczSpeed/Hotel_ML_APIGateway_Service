package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import com.ml.hotel_ml_apigateway_service.service.ApiGateWayHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel")
public class ApiGateWayHotelController {

    private final APIGatewayProducerService apiGatewayProducerService;

    @Autowired
    public ApiGateWayHotelController(APIGatewayProducerService apiGatewayProducerService) {
        this.apiGatewayProducerService = apiGatewayProducerService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> registerUser(@RequestBody String message) {
        return apiGatewayProducerService.createHotelMessage(message);
    }

}
