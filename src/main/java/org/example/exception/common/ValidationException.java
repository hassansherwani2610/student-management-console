package org.example.exception.common;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}
