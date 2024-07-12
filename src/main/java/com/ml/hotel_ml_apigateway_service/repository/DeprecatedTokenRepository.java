package com.ml.hotel_ml_apigateway_service.repository;

import com.ml.hotel_ml_apigateway_service.model.DeprecatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeprecatedTokenRepository extends JpaRepository<DeprecatedToken, UUID> {

}
