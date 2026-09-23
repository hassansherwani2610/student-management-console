package org.example.service;

import org.example.model.Course;

import java.util.List;

public interface CourseService {
    public Course createCourse(Long id, String name, String code, int creditHours);
    public List<Course> getAllCourses();
    public Course getCourseById(Long id);
    public Course updateCourse(Long id, String name, String code, int creditHours);
    public void deleteCourse(Long id);

}
