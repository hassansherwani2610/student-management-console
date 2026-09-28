package org.example.serviceImpl;

import org.example.exception.DuplicateEnrollmentException;
import org.example.exception.EnrollmentNotFoundException;
import org.example.model.Enrollment;
import org.example.repository.CrudRepository;
import org.example.service.EnrollmentService;

import java.util.List;
import java.util.Objects;

public class EnrollmentServiceImpl implements EnrollmentService {

    private final CrudRepository<Enrollment, Long> repository;

    public EnrollmentServiceImpl(CrudRepository<Enrollment, Long> repository) {
        this.repository = repository;
    }

    private boolean enrollmentExists(Long studentId, Long courseId, String semester, Long id) {
        return repository
                .findAll()
                .stream()
                .anyMatch(enrollment ->
                        enrollment.getStudentId().equals(studentId)
                        && enrollment.getCourseId().equals(courseId)
                        && enrollment.getSemester().equalsIgnoreCase(semester.trim())
                        && !Objects.equals(enrollment.getId(), id)
                );
    }

    @Override
    public Enrollment createEnrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        if (repository.existsById(id)) {
            throw new DuplicateEnrollmentException("An enrollment with ID " + id + " already exists.");
        }

        if (enrollmentExists(studentId, courseId, semester, null)) {
            throw new DuplicateEnrollmentException("This student is already enrolled in this course for this semester.");
        }

        Enrollment enrollment = repository.save(new Enrollment(id, studentId, courseId, semester, grade));

        return enrollment;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return repository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment with ID " + id + " was not found."));
    }

    @Override
    public Enrollment updateEnrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        Enrollment enrollment = getEnrollmentById(id);

        if (enrollmentExists(studentId, courseId, semester, id)) {
            throw new DuplicateEnrollmentException("This student is already enrolled in this course for this semester.");
        }

        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSemester(semester);
        enrollment.setGrade(grade);

        return repository.update(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        getEnrollmentById(id);
        repository.deleteById(id);
    }
}
