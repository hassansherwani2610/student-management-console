package org.example.serviceImpl;

import org.example.exception.course.CourseNotFoundException;
import org.example.exception.course.DuplicateCourseException;
import org.example.exception.common.EntityInUseException;
import org.example.model.Course;
import org.example.model.Enrollment;
import org.example.repository.CrudRepository;
import org.example.service.CourseService;

import java.util.List;
import java.util.Objects;

public class CourseServiceImpl implements CourseService {
    private final CrudRepository<Course, Long> courseRepository;
    private final CrudRepository<Enrollment, Long> enrollmentRepository;

    public CourseServiceImpl(CrudRepository<Course, Long> courseRepository, CrudRepository<Enrollment, Long> enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    private boolean courseCodeExists(String code, Long id) {
        String formattedCode = code.trim();

        return courseRepository
                .findAll()
                .stream()
                .anyMatch(course -> course.getCode().equalsIgnoreCase(formattedCode) && !Objects.equals(course.getId(), id));
    }

    private boolean courseNameExists(String name, Long id) {
        String formattedName = name.trim();

        return courseRepository
                .findAll()
                .stream()
                .anyMatch(course -> course.getName().equalsIgnoreCase(formattedName) && !Objects.equals(course.getId(), id));
    }

    @Override
    public Course createCourse(Long id, String name, String code, int creditHours) {
        if (courseRepository.existsById(id)) {
            throw new DuplicateCourseException("A course with ID " + id + " already exists in our system.");
        }

        if (courseCodeExists(code, null)) {
            throw new DuplicateCourseException("A course with code " + code + " already exists in our system.");
        }

        if (courseNameExists(name, null)) {
            throw new DuplicateCourseException("A course named " + name + " already exists in our system.");
        }

        return courseRepository.save(new Course(id, name, code, creditHours));
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository
                .findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + id + " was not found in our system."));
    }

    @Override
    public Course updateCourse(Long id, String name, String code, int creditHours) {
        getCourseById(id);

        Course updated = new Course(id, name, code, creditHours);

        if (courseCodeExists(updated.getCode(), id)) {
            throw new DuplicateCourseException("A course with code " + code + " already exists in our system.");
        }

        if (courseNameExists(updated.getName(), id)) {
            throw new DuplicateCourseException("A course named " + name + " already exists in our system.");
        }

        return courseRepository.update(updated);
    }

    @Override
    public void deleteCourse(Long id) {
        getCourseById(id);

        boolean hasEnrollments = enrollmentRepository
                .findAll()
                .stream()
                .anyMatch(enrollment -> enrollment.getCourseId().equals(id));

        if (hasEnrollments) {
            throw new EntityInUseException("Course with ID " + id + " cannot be deleted because students are still enrolled in it. Delete those enrollments first.");
        }

        courseRepository.deleteById(id);
    }
}
