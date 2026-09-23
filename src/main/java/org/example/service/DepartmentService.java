package org.example.service;

import org.example.model.Department;

import java.util.List;

public interface DepartmentService {
    public Department createDepartment(Long id, String name);
    public List<Department> getAllDepartments();
    public Department getDepartmentById(Long id);
    public Department updateDepartment(Long id, String name);
    public void deleteDepartmentById(Long id);
}
