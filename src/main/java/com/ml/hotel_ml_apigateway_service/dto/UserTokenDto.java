package com.ml.hotel_ml_apigateway_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserTokenDto {
    private String email;
    private String token;
    private LocalDate expiryDate;
}
