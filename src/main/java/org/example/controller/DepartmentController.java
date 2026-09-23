package org.example.controller;

import org.example.model.Department;
import org.example.service.DepartmentService;

import java.util.List;

public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public Department createDepartment(Long id, String name) {
        return departmentService.createDepartment(id, name);
    }

    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    public Department getDepartmentById(Long id) {
        return departmentService.getDepartmentById(id);
    }

    public Department updateDepartment(Long id, String name) {
        return departmentService.updateDepartment(id, name);
    }

    public void deleteDepartment(Long id) {
        departmentService.deleteDepartmentById(id);
    }
}
