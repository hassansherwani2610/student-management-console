package org.example.model;

import org.example.exception.ValidationException;

public class Department {
    private final Long id;
    private String name;

    public Department(Long id, String name) {
        if (id == null || id <= 0) {
            throw new ValidationException("Department ID must be greater than zero.");
        }

        this.id = id;
        setName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Department name cannot be empty.");
        }

        this.name = name.trim();
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Department: " + name;
    }
}
