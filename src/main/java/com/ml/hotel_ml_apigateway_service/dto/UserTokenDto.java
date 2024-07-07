package com.ml.hotel_ml_apigateway_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserTokenDto {
    private String email;
    private String token;
    private Date expiryDate;
    private String roles;
}
