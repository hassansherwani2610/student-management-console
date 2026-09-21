package org.example;

import org.example.controller.StudentController;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.repository.InMemoryStudentRepository;
import org.example.service.StudentService;
import org.example.serviceImpl.StudentServiceImpl;
import org.example.ui.ConsoleApplication;
import org.example.ui.InputHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CrudRepository<Student, Long> crudRepository = new InMemoryStudentRepository();

        StudentService studentService = new StudentServiceImpl(crudRepository);

        StudentController studentController = new StudentController(studentService);

        InputHandler input = new InputHandler(new Scanner(System.in));

        ConsoleApplication application = new ConsoleApplication(studentController, input);

        application.start();
    }
}