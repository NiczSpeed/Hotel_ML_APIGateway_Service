package com.ml.hotel_ml_apigateway_service.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_TOKEN")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "email")
    private String email;
    @Column(name = "token")
    private String token;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "roles")
    private String roles;
}
