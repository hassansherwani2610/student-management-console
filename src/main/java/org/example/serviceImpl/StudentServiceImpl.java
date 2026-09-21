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

    @Override
    public Student updateEmail(Long id, String email) {
        Student student = getStudentById(id);

        if (emailExists(email, id)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        student.setEmail(email);
        return repository.update(student);
    }

    @Override
    public Student updateDepartment(Long id, String department) {
        Student student = getStudentById(id);
        student.setDepartment(department);
        return repository.update(student);
    }

    @Override
    public Student updateGpa(Long id, double gpa) {
        Student student = getStudentById(id);
        student.setGpa(gpa);
        return repository.update(student);
    }

    @Override
    public Student updateSeatNo(Long id, String seatNo) {
        Student student = getStudentById(id);
        student.setSeatNo(seatNo);
        return repository.update(student);
    }

    @Override
    public Student updateAll(Long id, String name, String seatNo, String email, String department, double gpa) {
        Student student = getStudentById(id);

        if (emailExists(email, id)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        student.setName(name);
        student.setSeatNo(seatNo);
        student.setEmail(email);
        student.setDepartment(department);
        student.setGpa(gpa);

        return repository.update(student);
    }

    @Override
    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Student> searchByName(String keyword) {
        String formattedKeyword = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);

        return repository
                .findAll()
                .stream()
                .filter(student -> student.getName().toLowerCase(Locale.ROOT).contains(formattedKeyword))
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> filterByDepartment(String department) {
        String formattedDepartment = department.trim().toLowerCase(Locale.ROOT);

        return repository
                .findAll()
                .stream()
                .filter(student -> student.getDepartment().toLowerCase(Locale.ROOT).equals(formattedDepartment))
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> filterByGpa(double min, double max) {
        return repository
                .findAll()
                .stream()
                .filter(student -> student.getGpa() >= min && student.getGpa() <= max)
                .sorted((student1, student2) -> Double.compare(student1.getGpa(), student2.getGpa()))
                .toList();
    }

    @Override
    public List<Student> sortByName() {
        return repository
                .findAll()
                .stream()
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> sortById() {
        return repository
                .findAll()
                .stream()
                .sorted((student1, student2) -> student1.getId().compareTo(student2.getId()))
                .toList();
    }

    @Override
    public List<Student> sortByGpaDescending() {
        return repository
                .findAll()
                .stream()
                .sorted((student1, student2) -> Double.compare(student2.getGpa(), student1.getGpa()))
                .toList();
    }


}
