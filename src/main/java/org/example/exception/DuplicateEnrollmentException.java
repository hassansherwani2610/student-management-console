package org.example.exception;

public class DuplicateEnrollmentException extends RuntimeException{

    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
