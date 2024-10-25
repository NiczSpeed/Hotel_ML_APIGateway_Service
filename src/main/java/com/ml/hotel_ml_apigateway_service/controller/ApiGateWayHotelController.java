package com.ml.hotel_ml_apigateway_service.controller;

import com.ml.hotel_ml_apigateway_service.service.APIGatewayProducerService;
import com.ml.hotel_ml_apigateway_service.service.ApiGateWayHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/free/{city}/{startDate}/{endDate}")
    public ResponseEntity<String> getFreeHotels(@PathVariable String city, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        return apiGatewayProducerService.getFreeHotelsSet(city, startDate, endDate);
    }

    @GetMapping("/all/{city}")
    public ResponseEntity<String> getAllHotels(@PathVariable String city) {
        return apiGatewayProducerService.getAllHotels(city);
    }

//    @GetMapping("/all")
//    public ResponseEntity<String> getAllHotels(@RequestBody String city) {
//        return apiGatewayProducerService.getAllHotels(city);
//    }

    @GetMapping("/cities")
    public ResponseEntity<String> getCities() {
        return  apiGatewayProducerService.getHotelsCities();
    }

}
