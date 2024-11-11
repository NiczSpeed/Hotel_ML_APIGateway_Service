package com.ml.hotel_ml_apigateway_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml.hotel_ml_apigateway_service.exceptions.ErrorWhileDecodeException;
import com.ml.hotel_ml_apigateway_service.exceptions.ErrorWhileEncodeException;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Encryptor<T> implements AttributeConverter<T, String> {


    private final EncryptorUtil encryptorUtil;
    private final ObjectMapper objectMapper;


    @Override
    public String convertToDatabaseColumn(T t) {
        try {
            String json = objectMapper.writeValueAsString(t);
            return encryptorUtil.encrypt(json);
        } catch (Exception e) {
            throw new ErrorWhileEncodeException();
        }
    }

    @Override
    public T convertToEntityAttribute(String s) {
        try {
            String json = encryptorUtil.decrypt(s);
            return objectMapper.readValue(json, getTargetClass());
        } catch (Exception e) {
            throw new ErrorWhileDecodeException();
        }
    }

    protected Class<T> getTargetClass() {
        throw new UnsupportedOperationException("This method need to be override!");
    }

}
