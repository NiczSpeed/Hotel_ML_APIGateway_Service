package com.ml.hotel_ml_apigateway_service.service;

import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeprecatedTokenService {

    private final DeprecatedTokenRepository deprecatedTokenRepository;

    @Scheduled(cron = "0 1 * * * *")
    @Transactional
    protected void removeDeprecatedTokens() {
        deprecatedTokenRepository.deleteAll();
    }

}
