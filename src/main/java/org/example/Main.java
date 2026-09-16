package org.example;

import org.example.model.Student;

public class Main {
    public static void main(String[] args) {
        Student student = new Student(1L, "Hassan", "B22110006055", "hassan@gmail.com", "CS", 3.21);

        System.out.println(student.toString());;
    }
}