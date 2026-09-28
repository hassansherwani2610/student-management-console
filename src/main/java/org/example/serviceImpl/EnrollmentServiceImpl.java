package org.example.serviceImpl;

import org.example.exception.DuplicateEnrollmentException;
import org.example.exception.EnrollmentNotFoundException;
import org.example.model.Enrollment;
import org.example.repository.CrudRepository;
import org.example.service.CourseService;
import org.example.service.EnrollmentService;
import org.example.service.StudentService;

import java.util.List;
import java.util.Objects;

public class EnrollmentServiceImpl implements EnrollmentService {

    private final CrudRepository<Enrollment, Long> enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentServiceImpl(CrudRepository<Enrollment, Long> enrollmentRepository, StudentService studentService, CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    private boolean enrollmentExists(Long studentId, Long courseId, String semester, Long id) {
        return enrollmentRepository
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
        if (enrollmentRepository.existsById(id)) {
            throw new DuplicateEnrollmentException("An enrollment with ID " + id + " already exists.");
        }

        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);

        if (enrollmentExists(studentId, courseId, semester, null)) {
            throw new DuplicateEnrollmentException("This student is already enrolled in this course for this semester.");
        }

        Enrollment enrollment = enrollmentRepository.save(new Enrollment(id, studentId, courseId, semester, grade));

        return enrollment;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment with ID " + id + " was not found."));
    }

    @Override
    public Enrollment updateEnrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        getEnrollmentById(id);

        Enrollment updated = new Enrollment(id, studentId, courseId, semester, grade);

        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);

        if (enrollmentExists(studentId, courseId, updated.getSemester(), id)) {
            throw new DuplicateEnrollmentException("This student is already enrolled in this course for this semester.");
        }

        return enrollmentRepository.update(updated);
    }

    @Override
    public void deleteEnrollment(Long id) {
        getEnrollmentById(id);
        enrollmentRepository.deleteById(id);
    }
}
