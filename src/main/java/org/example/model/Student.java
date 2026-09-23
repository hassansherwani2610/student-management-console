package org.example.model;

import org.example.exception.ValidationException;

import java.util.Locale;
import java.util.regex.Pattern;

public class Student {
    private final Long id;
    private String name;
    private String seatNo;
    private String email;
    private Long departmentId;
    private double gpa;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Student(Long id, String name, String seatNo, String email, Long departmentId, double gpa) {
        if (id == null || id <= 0) {
            throw new ValidationException("Student ID must be greater than zero.");
        }

        this.id = id;
        setName(name);
        setSeatNo(seatNo);
        setEmail(email);
        setDepartment(departmentId);
        setGpa(gpa);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name cannot be empty.");
        }

        this.name = name.trim();
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        if (seatNo == null || seatNo.isBlank()) {
            throw new ValidationException("Seat number cannot be empty.");
        }

        this.seatNo = seatNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be empty.");
        }

        String trimmedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new ValidationException("Please enter a valid email address.");
        }

        this.email = trimmedEmail;
    }

    public Long getDepartment() {
        return departmentId;
    }

    public void setDepartment(Long departmentId) {
        if (departmentId == null) {
            throw new ValidationException("DepartmentEnum cannot be empty.");
        }

        this.departmentId = departmentId;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (Double.isNaN(gpa) || Double.isInfinite(gpa) || gpa < 0.0 || gpa > 4.0) {
            throw new ValidationException("GPA must be between 0.0 and 4.0.");
        }

        this.gpa = gpa;
    }

    public String academicStanding() {
        if (gpa >= 3.5) {
            return "Excellent";
        }
        if (gpa >= 3.0) {
            return "Good";
        }
        if (gpa >= 2.0) {
            return "Satisfactory";
        }
        return "At Risk";
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Student Name: " + name + " | Seat No: " + seatNo + " | Email: " + email + " | Department: " + departmentId + " | GPA: " + gpa + " | Standing (as per your GPA): " + academicStanding();
    }

    public String toString(String departmentName) {
        return "ID: " + id + " | Student Name: " + name + " | Seat No: " + seatNo + " | Email: " + email + " | Department: " + departmentName + " | GPA: " + gpa + " | Standing (as per your GPA): " + academicStanding();
    }
}
