package org.example.controller;

import org.example.model.Enrollment;
import org.example.service.EnrollmentService;

import java.util.List;

public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public Enrollment createEnrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        return enrollmentService.createEnrollment(id, studentId, courseId, semester, grade);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    public Enrollment updateEnrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        return enrollmentService.updateEnrollment(id, studentId, courseId, semester, grade);
    }

    public void deleteEnrollment(Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}