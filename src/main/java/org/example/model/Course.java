package org.example.model;

import org.example.exception.ValidationException;

import java.util.Locale;

public class Course {

    private final Long id;
    private String name;
    private String code;
    private int creditHours;

    public Course(Long id, String name, String code, int creditHours) {
        if (id == null || id <= 0) {
            throw new ValidationException("Course ID must be greater than zero.");
        }

        this.id = id;
        setName(name);
        setCode(code);
        setCreditHours(creditHours);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Course name cannot be empty.");
        }

        this.name = name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Course code cannot be empty.");
        }

        this.code = code.trim().toUpperCase(Locale.ROOT);
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        if (creditHours <= 0) {
            throw new ValidationException("Credit hours must be greater than zero.");
        }

        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Course Name: " + name + " | Course Code: " + code + " | Credit Hours: " + creditHours;
    }
}
