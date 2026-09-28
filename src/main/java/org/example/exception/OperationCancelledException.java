package org.example.exception;

public class OperationCancelledException extends RuntimeException {

    public OperationCancelledException() {
        super("Operation cancelled.");
    }
}
