package org.example.exception.department;

public class DuplicateDepartmentException extends RuntimeException{

    public DuplicateDepartmentException(String message) {
        super(message);
    }
}
