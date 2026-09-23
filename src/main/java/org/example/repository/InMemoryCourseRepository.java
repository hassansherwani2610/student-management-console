package org.example.repository;

import org.example.exception.CourseNotFoundException;
import org.example.exception.DuplicateCourseException;
import org.example.model.Course;

import java.util.*;

public class InMemoryCourseRepository implements CrudRepository<Course, Long> {

    private final Map<Long, Course> courses = new LinkedHashMap<>();

    @Override
    public Course save(Course course) {
        if (courses.containsKey(course.getId())) {
            throw new DuplicateCourseException("Course with ID " + course.getId() + " already exists in our system.");
        }

        courses.put(course.getId(), course);
        return course;
    }

    @Override
    public Optional<Course> findById(Long id) {
        if (courses.containsKey(id)) {
            return Optional.of(courses.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public long count() {
        return courses.size();
    }

    @Override
    public Course update(Course course) {
        if (!courses.containsKey(course.getId())) {
            throw new CourseNotFoundException("Course with ID " + course.getId() + " does not exist in our system.");
        }

        courses.put(course.getId(), course);
        return course;
    }

    @Override
    public void deleteById(Long id) {
        if (!courses.containsKey(id)) {
            throw new CourseNotFoundException("Course with ID " + id + " does not exist in our system.");
        }

        courses.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return courses.containsKey(id);
    }
}
