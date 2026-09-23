package org.example.repository;

import org.example.exception.DepartmentNotFoundException;
import org.example.exception.DuplicateDepartmentException;
import org.example.model.Department;

import java.util.*;

public class InMemoryDepartmentRepository implements CrudRepository<Department, Long> {

    private final Map<Long, Department> departments = new LinkedHashMap<>();

    @Override
    public Department save(Department department) {
        if (departments.containsKey(department.getId())) {
            throw new DuplicateDepartmentException("A department with ID " + department.getId() + " already exists in our system.");
        }

        departments.put(department.getId(), department);
        return department;
    }

    @Override
    public Optional<Department> findById(Long id) {
        if (departments.containsKey(id)) {
            return Optional.of(departments.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Department> findAll() {
        return new ArrayList<>(departments.values());
    }

    @Override
    public long count() {
        return departments.size();
    }

    @Override
    public Department update(Department department) {
        if (!departments.containsKey(department.getId())) {
            throw new DepartmentNotFoundException("A department with ID " + department.getId() + " does not exist in our system.");
        }

        departments.put(department.getId(), department);
        return department;
    }

    @Override
    public void deleteById(Long id) {
        if (!departments.containsKey(id)) {
            throw new DepartmentNotFoundException("A department with ID " + id + " does not exist in our system.");
        }

        departments.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return departments.containsKey(id);
    }
}
