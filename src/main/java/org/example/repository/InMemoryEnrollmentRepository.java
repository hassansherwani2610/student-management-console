package org.example.repository;

import org.example.exception.DuplicateEnrollmentException;
import org.example.exception.EnrollmentNotFoundException;
import org.example.model.Enrollment;

import java.util.*;

public class InMemoryEnrollmentRepository implements CrudRepository<Enrollment, Long> {

    private final Map<Long, Enrollment> enrollments = new LinkedHashMap<>();

    @Override
    public Enrollment save(Enrollment enrollment) {
        if (enrollments.containsKey(enrollment.getId())) {
            throw new DuplicateEnrollmentException("An Enrollment with ID " + enrollment.getId() + " already exists in our system.");
        }

        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        if (enrollments.containsKey(id)) {
            return Optional.of(enrollments.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    @Override
    public long count() {
        return enrollments.size();
    }

    @Override
    public Enrollment update(Enrollment enrollment) {
        if (!enrollments.containsKey(enrollment.getId())) {
            throw new EnrollmentNotFoundException("An enrollment with ID " + enrollment.getId() + " does not exist in our system.");
        }

        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    @Override
    public void deleteById(Long id) {
        if (!enrollments.containsKey(id)) {
            throw new EnrollmentNotFoundException("An Enrollment with ID " + id + " does not exist in our system.");
        }

        enrollments.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return enrollments.containsKey(id);
    }
}
