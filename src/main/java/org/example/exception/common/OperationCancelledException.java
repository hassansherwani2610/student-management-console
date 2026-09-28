package org.example.exception.common;

public class OperationCancelledException extends RuntimeException {

    public OperationCancelledException() {
        super("Operation cancelled.");
    }
}
