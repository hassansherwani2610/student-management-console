package org.example.service;

import org.example.model.Enrollment;

import java.util.List;

public interface EnrollmentService {

    Enrollment createEnrollment(Long id, Long studentId, Long courseId, String semester, String grade);
    List<Enrollment> getAllEnrollments();
    Enrollment getEnrollmentById(Long id);
    Enrollment updateEnrollment(Long id, Long studentId, Long courseId, String semester, String grade);
    void deleteEnrollment(Long id);

}
