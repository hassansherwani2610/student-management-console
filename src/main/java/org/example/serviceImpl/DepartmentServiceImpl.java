package org.example.serviceImpl;

import org.example.exception.DepartmentNotFoundException;
import org.example.exception.DuplicateDepartmentException;
import org.example.exception.EntityInUseException;
import org.example.model.Department;
import org.example.model.Student;
import org.example.repository.CrudRepository;
import org.example.service.DepartmentService;

import java.util.List;
import java.util.Objects;

public class DepartmentServiceImpl implements DepartmentService {
    private final CrudRepository<Department, Long> departmentRepository;
    private final CrudRepository<Student, Long> studentRepository;

    public DepartmentServiceImpl(CrudRepository<Department, Long> departmentRepository, CrudRepository<Student, Long> studentRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    private boolean departmentNameExists(String name, Long id) {
        String formattedName = name.trim();

        return departmentRepository
                .findAll()
                .stream()
                .anyMatch(department -> department.getName().equalsIgnoreCase(formattedName) && !Objects.equals(department.getId(), id));
    }

    @Override
    public Department createDepartment(Long id, String name) {
        if (departmentRepository.existsById(id)) {
            throw new DuplicateDepartmentException("A department with ID " + id + " already exists.");
        }

        if (departmentNameExists(name, null)) {
            throw new DuplicateDepartmentException("A department with name " + name + " already exists.");
        }

        Department department = departmentRepository.save(new Department(id, name));

        return department;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository
                .findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " was not found."));
    }

    @Override
    public Department updateDepartment(Long id, String name) {
        getDepartmentById(id);

        Department updated = new Department(id, name);

        if (departmentNameExists(updated.getName(), id)) {
            throw new DuplicateDepartmentException("A department with name " + name + " already exists.");
        }

        return departmentRepository.update(updated);
    }

    @Override
    public void deleteDepartmentById(Long id) {
        getDepartmentById(id);

        boolean hasStudent = studentRepository
                .findAll()
                .stream()
                .anyMatch(student -> student.getDepartment().equals(id));

        if (hasStudent) {
            throw new EntityInUseException("Department with ID " + id + " cannot be deleted because students still belong to it. Move or delete those students first.");
        }

        departmentRepository.deleteById(id);
    }
}
