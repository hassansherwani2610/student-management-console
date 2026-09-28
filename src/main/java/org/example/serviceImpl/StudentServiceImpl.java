package org.example.serviceImpl;

import org.example.exception.DuplicateStudentException;
import org.example.exception.EntityInUseException;
import org.example.exception.StudentNotFoundException;
import org.example.model.Enrollment;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.service.DepartmentService;
import org.example.service.StudentService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StudentServiceImpl implements StudentService {
    private final CrudRepository<Student, Long> studentRepository;
    private final CrudRepository<Enrollment, Long> enrollmentRepository;
    private final DepartmentService departmentService;

    public StudentServiceImpl(CrudRepository<Student, Long> studentRepository, CrudRepository<Enrollment, Long> enrollmentRepository, DepartmentService departmentService) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.departmentService = departmentService;
    }

    private boolean emailExists(String email, Long id) {
        String formattedEmail = email.trim();

        return studentRepository
                .findAll()
                .stream()
                .anyMatch(student -> student.getEmail().equalsIgnoreCase(formattedEmail) && !Objects.equals(student.getId(), id));
    }


    @Override
    public Student createStudent(Long id, String name, String seatNo, String email, Long departmentId, double gpa) {
        if (studentRepository.existsById(id)) {
            throw new DuplicateStudentException("A student with ID " + id + " already exists in our system.");
        }

        if (emailExists(email, null)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        departmentService.getDepartmentById(departmentId);

        Student student = studentRepository.save(new Student(id, name, seatNo, email, departmentId, gpa));

        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student with ID " + id + " was not found in our system.")
                );
    }

    @Override
    public Student updateName(Long id, String newName) {
        Student student = getStudentById(id);
        student.setName(newName);
        return studentRepository.save(student);
    }

    @Override
    public Student updateEmail(Long id, String email) {
        Student student = getStudentById(id);

        if (emailExists(email, id)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        student.setEmail(email);
        return studentRepository.update(student);
    }

    @Override
    public Student updateDepartment(Long id, Long departmentId) {
        Student student = getStudentById(id);

        departmentService.getDepartmentById(departmentId);

        student.setDepartmentId(departmentId);
        return studentRepository.update(student);
    }

    @Override
    public Student updateGpa(Long id, double gpa) {
        Student student = getStudentById(id);
        student.setGpa(gpa);
        return studentRepository.update(student);
    }

    @Override
    public Student updateSeatNo(Long id, String seatNo) {
        Student student = getStudentById(id);
        student.setSeatNo(seatNo);
        return studentRepository.update(student);
    }

    @Override
    public Student updateAll(Long id, String name, String seatNo, String email, Long departmentId, double gpa) {
        getStudentById(id);

        Student updated = new Student(id, name, seatNo, email, departmentId, gpa);

        if (emailExists(updated.getEmail(), id)) {
            throw new DuplicateStudentException("A student with email " + email + " already exists in our system.");
        }

        departmentService.getDepartmentById(departmentId);

        return studentRepository.update(updated);
    }

    @Override
    public void deleteStudentById(Long id) {
        getStudentById(id);

        boolean hasEnrollments = enrollmentRepository
                .findAll()
                .stream()
                .anyMatch(enrollment -> enrollment.getStudentId().equals(id));

        if (hasEnrollments) {
            throw new EntityInUseException("Student with ID " + id + " cannot be deleted because they still have enrollments. Delete those enrollments first.");
        }

        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchByName(String keyword) {
        String formattedKeyword = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);

        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getName().toLowerCase(Locale.ROOT).contains(formattedKeyword))
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> filterByDepartment(Long departmentId) {
        departmentService.getDepartmentById(departmentId);

        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDepartmentId().equals(departmentId))
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> filterByGpa(double min, double max) {
        return studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getGpa() >= min && student.getGpa() <= max)
                .sorted((student1, student2) -> Double.compare(student1.getGpa(), student2.getGpa()))
                .toList();
    }

    @Override
    public List<Student> sortByName() {
        return studentRepository
                .findAll()
                .stream()
                .sorted((student1, student2) -> student1.getName().compareToIgnoreCase(student2.getName()))
                .toList();
    }

    @Override
    public List<Student> sortById() {
        return studentRepository
                .findAll()
                .stream()
                .sorted((student1, student2) -> student1.getId().compareTo(student2.getId()))
                .toList();
    }

    @Override
    public List<Student> sortByGpaDescending() {
        return studentRepository
                .findAll()
                .stream()
                .sorted((student1, student2) -> Double.compare(student2.getGpa(), student1.getGpa()))
                .toList();
    }


}
