package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayConsumerService;
import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class APIGatewayUserController {

    private final APIGatewayProducerService apiGatewayProducerService;
    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public APIGatewayUserController(APIGatewayProducerService apiGatewayService, APIGatewayConsumerService apiGatewayConsumerService) {
        this.apiGatewayProducerService = apiGatewayService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String message) {
        return apiGatewayProducerService.registerUserMessage(message);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody String message) {
//        try {
//            return new ResponseEntity<>(apiGatewayProducerService.loginUserMessage(message), HttpStatus.OK);
//        }catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody String message) {
        return apiGatewayProducerService.loginUserMessage(message);
    }

    @GetMapping("/info")
    public String welcomeEndpoint(){
        return "Welcome to Ml ApiGateWay Service, ur role is USER!";
    }
}
