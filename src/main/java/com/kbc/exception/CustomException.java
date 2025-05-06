package com.kbc.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
    
    // You can add more constructors if needed
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}