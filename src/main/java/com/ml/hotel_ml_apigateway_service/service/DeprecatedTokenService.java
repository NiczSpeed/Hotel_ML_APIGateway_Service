package com.ml.hotel_ml_apigateway_service.service;

import com.ml.hotel_ml_apigateway_service.repository.DeprecatedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeprecatedTokenService {

    private final DeprecatedTokenRepository deprecatedTokenRepository;

    @Autowired
    public DeprecatedTokenService(DeprecatedTokenRepository deprecatedTokenRepository) {
        this.deprecatedTokenRepository = deprecatedTokenRepository;
    }

    @Scheduled(cron = "0 * /24 * * *")
    @Transactional
    protected void removeDeprecatedTokens() {
        deprecatedTokenRepository.deleteAll();
    }

}
