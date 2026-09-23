package org.example;

import org.example.controller.DepartmentController;
import org.example.controller.StudentController;
import org.example.model.Department;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.repository.InMemoryDepartmentRepository;
import org.example.repository.InMemoryStudentRepository;
import org.example.service.DepartmentService;
import org.example.service.StudentService;
import org.example.serviceImpl.DepartmentServiceImpl;
import org.example.serviceImpl.StudentServiceImpl;
import org.example.ui.ConsoleApplication;
import org.example.ui.InputHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CrudRepository<Student, Long> studentRepository = new InMemoryStudentRepository();
        CrudRepository<Department, Long> departmentRepository = new InMemoryDepartmentRepository();

        // For Department
        DepartmentService departmentService = new DepartmentServiceImpl(departmentRepository);
        DepartmentController departmentController = new DepartmentController(departmentService);

        // For Student
        StudentService studentService = new StudentServiceImpl(studentRepository, departmentService);
        StudentController studentController = new StudentController(studentService);

        InputHandler input = new InputHandler(new Scanner(System.in));

        ConsoleApplication application = new ConsoleApplication(studentController, departmentController, input);

        application.start();
    }
}