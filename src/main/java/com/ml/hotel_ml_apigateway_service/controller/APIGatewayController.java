package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/")
public class APIGatewayController {

    private final APIGatewayService apiGatewayService;

    @Autowired
    public APIGatewayController(APIGatewayService apiGatewayService) {
        this.apiGatewayService = apiGatewayService;
    }


//    @GetMapping("/register")
//    public void registerUser(@RequestParam String name) {
//        apiGatewayService.registerUserMessage(name);
//    }

//    @PostMapping("/register")
//    public void registerUser(@RequestBody String message) {
//        apiGatewayService.registerUserMessage(message);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody String message) {
        return new ResponseEntity<>(apiGatewayService.registerUserMessage(message), HttpStatus.CREATED);
    }




}
