package org.example.serviceImpl;

import org.example.exception.DepartmentNotFoundException;
import org.example.exception.DuplicateDepartmentException;
import org.example.model.Department;
import org.example.repository.CrudRepository;
import org.example.service.DepartmentService;

import java.util.List;
import java.util.Objects;

public class DepartmentServiceImpl implements DepartmentService {
    private final CrudRepository<Department, Long> repository;

    public DepartmentServiceImpl(CrudRepository<Department, Long> repository) {
        this.repository = repository;
    }

    private boolean departmentNameExists(String name, Long id) {
        String formattedName = name.trim();

        return repository
                .findAll()
                .stream()
                .anyMatch(department -> department.getName().equalsIgnoreCase(formattedName) && !Objects.equals(department.getId(), id));
    }

    @Override
    public Department createDepartment(Long id, String name) {
        if (repository.existsById(id)) {
            throw new DuplicateDepartmentException("A department with ID " + id + " already exists.");
        }

        if (departmentNameExists(name, null)) {
            throw new DuplicateDepartmentException("A department with name " + name + " already exists.");
        }

        Department department = repository.save(new Department(id, name));

        return department;
    }

    @Override
    public List<Department> getAllDepartments() {
        return repository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " was not found."));
    }

    @Override
    public Department updateDepartment(Long id, String name) {
        Department department = getDepartmentById(id);

        if (departmentNameExists(name, id)) {
            throw new DuplicateDepartmentException("A department with name " + name + " already exists.");
        }

        department.setName(name);

        return repository.update(department);
    }

    @Override
    public void deleteDepartmentById(Long id) {
        getDepartmentById(id);
        repository.deleteById(id);
    }
}
