package org.example;

import org.example.controller.CourseController;
import org.example.controller.DepartmentController;
import org.example.controller.EnrollmentController;
import org.example.controller.StudentController;
import org.example.model.Course;
import org.example.model.Department;
import org.example.model.Enrollment;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.repository.InMemoryCourseRepository;
import org.example.repository.InMemoryDepartmentRepository;
import org.example.repository.InMemoryEnrollmentRepository;
import org.example.repository.InMemoryStudentRepository;
import org.example.service.CourseService;
import org.example.service.DepartmentService;
import org.example.service.EnrollmentService;
import org.example.service.StudentService;
import org.example.serviceImpl.CourseServiceImpl;
import org.example.serviceImpl.DepartmentServiceImpl;
import org.example.serviceImpl.EnrollmentServiceImpl;
import org.example.serviceImpl.StudentServiceImpl;
import org.example.ui.ConsoleApplication;
import org.example.ui.InputHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // All entities Repositories
        CrudRepository<Department, Long> departmentRepository = new InMemoryDepartmentRepository();
        CrudRepository<Course, Long> courseRepository = new InMemoryCourseRepository();
        CrudRepository<Student, Long> studentRepository = new InMemoryStudentRepository();
        CrudRepository<Enrollment, Long> enrollmentRepository = new InMemoryEnrollmentRepository();

        // For Department
        DepartmentService departmentService = new DepartmentServiceImpl(departmentRepository, studentRepository);
        DepartmentController departmentController = new DepartmentController(departmentService);

        // For Course
        CourseService courseService = new CourseServiceImpl(courseRepository, enrollmentRepository);
        CourseController courseController = new CourseController(courseService);

        // For Student
        StudentService studentService = new StudentServiceImpl(studentRepository, enrollmentRepository, departmentService);
        StudentController studentController = new StudentController(studentService);

        // For Enrollment
        EnrollmentService enrollmentService = new EnrollmentServiceImpl(enrollmentRepository, studentService, courseService);
        EnrollmentController enrollmentController = new EnrollmentController(enrollmentService);

        InputHandler input = new InputHandler(new Scanner(System.in));

        ConsoleApplication application = new ConsoleApplication(studentController, departmentController, courseController, enrollmentController, input);

        application.start();
    }
}