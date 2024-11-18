package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservation")
public class APIGatewayReservationController {

    private final APIGatewayProducerService apiGatewayProducerService;

    @Autowired
    public APIGatewayReservationController(APIGatewayProducerService apiGatewayProducerService) {
        this.apiGatewayProducerService = apiGatewayProducerService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> registerUser(@RequestBody String message) {
        return apiGatewayProducerService.createReservationMessage(message);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getAllUserReservations(){
        return apiGatewayProducerService.getAllUserReservations();
    }

    @PostMapping("/price")
    public ResponseEntity<String> getReservationsPrice(@RequestBody String message){
        return apiGatewayProducerService.getReservationPrice(message);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteReservation(@PathVariable String uuid){
        return apiGatewayProducerService.deleteReservationByUuid(uuid);
    }

}
