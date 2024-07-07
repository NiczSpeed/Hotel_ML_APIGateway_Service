package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class APIGatewayUserController {

    private final APIGatewayProducerService apiGatewayService;

    @Autowired
    public APIGatewayUserController(APIGatewayProducerService apiGatewayService) {
        this.apiGatewayService = apiGatewayService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody String message) {
        JSONObject json = apiGatewayService.registerUserMessage(message);
        return new ResponseEntity<>(json.toString(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody String message) {
        try {
            return new ResponseEntity<>(apiGatewayService.loginUserMessage(message), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/info")
    public String welcomeEndpoint(){
        return "Welcome to Ml ApiGateWay Service, ur role is USER!";
    }
}
