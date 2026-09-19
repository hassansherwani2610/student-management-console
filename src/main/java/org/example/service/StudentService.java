package org.example.service;

import org.example.model.Student;

import java.util.List;

public interface StudentService {
    public Student createStudent(Long id, String name, String seatNo, String email, String department, double gpa);

    public List<Student> getAllStudents();

    public Student getStudentById(Long id);

    public Student updateName(Long id, String newName);
}
