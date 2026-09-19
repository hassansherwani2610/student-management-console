package org.example.serviceImpl;

import org.example.exception.DuplicateStudentException;
import org.example.exception.StudentNotFoundException;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.service.StudentService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StudentServiceImpl implements StudentService {
    private final CrudRepository<Student, Long> repository;

    public StudentServiceImpl(CrudRepository<Student, Long> repository) {
        this.repository = repository;
    }

    private boolean emailExists(String email, Long id) {
        String formattedEmail = email.trim().toLowerCase(Locale.ROOT);

        return repository
                .findAll()
                .stream()
                .anyMatch(student -> student.getEmail().equalsIgnoreCase(formattedEmail) && !Objects.equals(student.getId(), id));
    }


    @Override
    public Student createStudent(Long id, String name, String seatNo, String email, String department, double gpa) {
        if (repository.existsById(id)) {
            throw new DuplicateStudentException("A student with ID " + id + " already exists in our system.");
        }

        if (emailExists(email, null)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        Student student = repository.save(new Student(id, name, seatNo, email, department, gpa));

        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student with ID " + id + " was not found in our system.")
                );
    }

    @Override
    public Student updateName(Long id, String newName) {
        Student student = getStudentById(id);
        student.setName(newName);
        return repository.save(student);
    }
}
