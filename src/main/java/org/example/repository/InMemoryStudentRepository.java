package org.example.repository;

import org.example.exception.DuplicateStudentException;
import org.example.exception.StudentNotFoundException;
import org.example.model.Student;

import java.util.*;

public class InMemoryStudentRepository implements CrudRepository<Student, Long>{

    private final Map<Long, Student> students = new LinkedHashMap<>();

    @Override
    public Student save(Student student) {
        if (students.containsKey(student.getId())) {
            throw new DuplicateStudentException("A student with ID " + student.getId() + " already exists in our system.");
        }

        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        if (students.containsKey(id)) {
            return Optional.of(students.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public long count() {
        return students.size();
    }

    @Override
    public Student update(Student student) {
        if (!students.containsKey(student.getId())) {
            throw new StudentNotFoundException("A student with ID " + student.getId() + " does not exist in our system.");
        }

        students.put(student.getId(), student);
        return student;
    }

    @Override
    public void deleteById(Long id) {
        if (!students.containsKey(id)){
            throw new StudentNotFoundException("A student with ID " + id + " does not exist in our system.");
        }

        students.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return students.containsKey(id);
    }
}
