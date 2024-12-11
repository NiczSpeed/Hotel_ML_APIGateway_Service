package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class APIGatewayReservationController {

    private final APIGatewayProducerService apiGatewayProducerService;

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

    @PatchMapping("/update")
    public ResponseEntity<String> updateReservation(@RequestBody String message){
        return  apiGatewayProducerService.updateReservationMessage(message);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteReservation(@PathVariable String uuid){
        return apiGatewayProducerService.deleteReservationByUuid(uuid);
    }

}
