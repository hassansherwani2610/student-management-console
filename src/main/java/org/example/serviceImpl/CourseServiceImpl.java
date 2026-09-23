package org.example.serviceImpl;

import org.example.exception.CourseNotFoundException;
import org.example.exception.DuplicateCourseException;
import org.example.model.Course;
import org.example.repository.CrudRepository;
import org.example.service.CourseService;

import java.util.List;
import java.util.Objects;

public class CourseServiceImpl implements CourseService {
    private final CrudRepository<Course, Long> repository;

    public CourseServiceImpl(CrudRepository<Course, Long> repository) {
        this.repository = repository;
    }

    private boolean courseCodeExists(String code, Long id) {
        String formattedCode = code.trim();

        return repository
                .findAll()
                .stream()
                .anyMatch(course -> course.getCode().equalsIgnoreCase(formattedCode) && !Objects.equals(course.getId(), id));
    }

    @Override
    public Course createCourse(Long id, String name, String code, int creditHours) {
        if (repository.existsById(id)) {
            throw new DuplicateCourseException("A course with ID " + id + " already exists in our system.");
        }

        if (courseCodeExists(code, null)) {
            throw new DuplicateCourseException("A course with code " + code + " already exists in our system.");
        }

        return repository.save(new Course(id, name, code, creditHours));
    }

    @Override
    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + id + " was not found in our system."));
    }

    @Override
    public Course updateCourse(Long id, String name, String code, int creditHours) {
        Course course = getCourseById(id);

        if (courseCodeExists(code, id)) {
            throw new DuplicateCourseException("A course with code " + code + " already exists in our sustem.");
        }

        course.setName(name);
        course.setCode(code);
        course.setCreditHours(creditHours);

        return repository.update(course);
    }

    @Override
    public void deleteCourse(Long id) {
        getCourseById(id);
        repository.deleteById(id);
    }
}
