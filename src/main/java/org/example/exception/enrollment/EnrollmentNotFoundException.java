package org.example.exception.enrollment;

public class EnrollmentNotFoundException extends RuntimeException{

    public EnrollmentNotFoundException(String message) {
        super(message);
    }
}
