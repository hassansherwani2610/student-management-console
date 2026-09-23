package org.example.service;

import org.example.enums.DepartmentEnum;
import org.example.model.Student;

import java.util.List;

public interface StudentService {
    public Student createStudent(Long id, String name, String seatNo, String email, DepartmentEnum department, double gpa);

    public List<Student> getAllStudents();

    public Student getStudentById(Long id);

    public Student updateName(Long id, String newName);

    public Student updateEmail(Long id, String email);

    public Student updateDepartment(Long id, DepartmentEnum email);

    public Student updateGpa(Long id, double gpa);

    public Student updateSeatNo(Long id, String seatNo);

    public Student updateAll(Long id, String name, String seatNo, String email, DepartmentEnum department, double gpa);

    public void deleteStudent(Long id);

    public List<Student> searchByName(String keyword);

    public List<Student> filterByDepartment(DepartmentEnum department);

    public List<Student> filterByGpa(double min, double max);

    public List<Student> sortByName();

    public List<Student> sortById();

    public List<Student> sortByGpaDescending();
}
