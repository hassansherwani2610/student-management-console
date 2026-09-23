package org.example.controller;

import org.example.model.Course;
import org.example.service.CourseService;

import java.util.List;

public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    public Course createCourse(Long id, String name, String code, int creditHours) {
        return courseService.createCourse(id, name, code, creditHours);
    }

    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    public Course getCourseById(Long id) {
        return courseService.getCourseById(id);
    }

    public Course updateCourse(Long id, String name, String code, int creditHours) {
        return courseService.updateCourse(id, name, code, creditHours);
    }

    public void deleteCourse(Long id) {
        courseService.deleteCourse(id);
    }
}