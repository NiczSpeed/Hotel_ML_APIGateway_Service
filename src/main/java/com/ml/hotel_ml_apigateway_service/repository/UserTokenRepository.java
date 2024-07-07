package com.ml.hotel_ml_apigateway_service.repository;

import com.ml.hotel_ml_apigateway_service.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

}
