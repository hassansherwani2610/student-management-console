package org.example;

import org.example.controller.CourseController;
import org.example.controller.DepartmentController;
import org.example.controller.StudentController;
import org.example.model.Course;
import org.example.model.Department;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.repository.InMemoryCourseRepository;
import org.example.repository.InMemoryDepartmentRepository;
import org.example.repository.InMemoryStudentRepository;
import org.example.service.CourseService;
import org.example.service.DepartmentService;
import org.example.service.StudentService;
import org.example.serviceImpl.CourseServiceImpl;
import org.example.serviceImpl.DepartmentServiceImpl;
import org.example.serviceImpl.StudentServiceImpl;
import org.example.ui.ConsoleApplication;
import org.example.ui.InputHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // For Department
        CrudRepository<Department, Long> departmentRepository = new InMemoryDepartmentRepository();
        DepartmentService departmentService = new DepartmentServiceImpl(departmentRepository);
        DepartmentController departmentController = new DepartmentController(departmentService);

        // For Course
        CrudRepository<Course, Long> courseRepository = new InMemoryCourseRepository();
        CourseService courseService = new CourseServiceImpl(courseRepository);
        CourseController courseController = new CourseController(courseService);

        // For Student
        CrudRepository<Student, Long> studentRepository = new InMemoryStudentRepository();
        StudentService studentService = new StudentServiceImpl(studentRepository, departmentService);
        StudentController studentController = new StudentController(studentService);


        InputHandler input = new InputHandler(new Scanner(System.in));

        ConsoleApplication application = new ConsoleApplication(studentController, departmentController, courseController, input);

        application.start();
    }
}