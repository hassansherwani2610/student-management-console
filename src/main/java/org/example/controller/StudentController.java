package org.example.controller;

import org.example.model.Student;
import org.example.service.StudentService;

import java.util.List;

public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public Student createStudent(Long id, String name, String seatNo, String email, Long departmentId, double gpa) {
        return studentService.createStudent(id, name, seatNo, email, departmentId, gpa);
    }

    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    public Student getStudentById(Long id) {
        return studentService.getStudentById(id);
    }

    public Student updateName(Long id, String newName) {
        return studentService.updateName(id, newName);
    }

    public Student updateEmail(Long id, String email) {
        return studentService.updateEmail(id, email);
    }

    public Student updateDepartment(Long id, Long departmentId) {
        return studentService.updateDepartment(id, departmentId);
    }

    public Student updateGpa(Long id, double gpa) {
        return studentService.updateGpa(id, gpa);
    }

    public Student updateSeatNo(Long id, String seatNo) {
        return studentService.updateSeatNo(id, seatNo);
    }

    public Student updateAll(Long id, String name, String seatNo, String email, Long departmentId, double gpa) {
        return studentService.updateAll(id, name, seatNo, email, departmentId, gpa);
    }

    public void deleteStudent(Long id) {
        studentService.deleteStudentById(id);
    }

    public List<Student> searchByName(String keyword) {
        return studentService.searchByName(keyword);
    }

    public List<Student> filterByDepartment(Long departmentId) {
        return studentService.filterByDepartment(departmentId);
    }

    public List<Student> filterByGpa(double min, double max) {
        return studentService.filterByGpa(min, max);
    }

    public List<Student> sortByName() {
        return studentService.sortByName();
    }

    public List<Student> sortById() {
        return studentService.sortById();
    }

    public List<Student> sortByGpaDescending() {
        return studentService.sortByGpaDescending();
    }
}
