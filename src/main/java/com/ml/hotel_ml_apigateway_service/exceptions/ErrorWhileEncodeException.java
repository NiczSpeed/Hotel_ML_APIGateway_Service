package com.ml.hotel_ml_apigateway_service.exceptions;

public class ErrorWhileEncodeException extends RuntimeException {
    public ErrorWhileEncodeException() {
        super("Error while encoding!");
    }
}
