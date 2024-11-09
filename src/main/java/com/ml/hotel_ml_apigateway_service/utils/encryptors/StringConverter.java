package com.ml.hotel_ml_apigateway_service.utils.encryptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml.hotel_ml_apigateway_service.utils.Encryptor;
import com.ml.hotel_ml_apigateway_service.utils.EncryptorUtil;

public class StringConverter extends Encryptor<String> {

    public StringConverter(EncryptorUtil encryptorUtil, ObjectMapper objectMapper) {
        super(encryptorUtil, objectMapper);
    }

    @Override
    protected Class<String> getTargetClass() {
        return String.class;
    }
}
