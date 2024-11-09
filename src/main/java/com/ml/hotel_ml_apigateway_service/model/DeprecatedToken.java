package com.ml.hotel_ml_apigateway_service.model;

import com.ml.hotel_ml_apigateway_service.utils.encryptors.StringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Data
@Table(name = "DeprecatedTokens")
public class DeprecatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "name", length = 500)
    @Convert(converter = StringConverter.class)
    private String token;

}
