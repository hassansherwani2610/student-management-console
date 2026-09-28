package org.example.exception.student;

public class DuplicateStudentException extends RuntimeException{

    public DuplicateStudentException(String message) {
        super(message);
    }
}
