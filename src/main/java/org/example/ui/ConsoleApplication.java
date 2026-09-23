package org.example.ui;

import org.example.controller.StudentController;
import org.example.enums.DepartmentEnum;
import org.example.exception.DuplicateStudentException;
import org.example.exception.StudentNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.Student;

import java.util.List;

public class ConsoleApplication {

    private final StudentController studentController;
    private final InputHandler input;
    private boolean running = true;

    public ConsoleApplication(StudentController studentController, InputHandler input) {
        this.studentController = studentController;
        this.input = input;
    }

    public void start() {
        printWelcome();

        while (running) {
            try {
                printMenu();

                int choice = input.readInt("Choose an option: ");

                handleChoice(choice);

            } catch (ValidationException exception) {
                System.out.println("\nError: " + exception.getMessage());
            } catch (DuplicateStudentException exception) {
                System.out.println("\nError: " + exception.getMessage());
            } catch (StudentNotFoundException exception) {
                System.out.println("\nError: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("\nUnexpected error: " + exception.getMessage());
            }
        }

        System.out.println("\nThank you for using Student Management System.");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> addStudent();
            case 2 -> viewAllStudents();
            case 3 -> findStudentById();
            case 4 -> updateStudent();
            case 5 -> deleteStudent();
            case 6 -> searchStudents();
            case 0 -> running = false;
            default -> System.out.println("Invalid menu option. Choose a number from 0 to 6.");
        }
    }

    private void addStudent() {
        System.out.println("\n--- Add Student ---");

        Long id = input.readLong("Student ID: ");
        String name = input.readRequiredText("Name: ");
        String seatNo = input.readRequiredText("Seat Number: ");
        String email = input.readRequiredText("Email: ");
        DepartmentEnum department = input.readDepartment("Department: ");
        double gpa = input.readDouble("GPA (0.0 - 4.0): ");

        Student student = studentController.createStudent(id, name, seatNo, email, department, gpa);

        System.out.println("\nStudent created successfully.");
        System.out.println(student);
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");

        List<Student> students = studentController.sortById();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        students.forEach(System.out::println);

        System.out.println("\nTotal: " + students.size());
    }

    private void findStudentById() {
        System.out.println("\n--- Find Student ---");

        Long id = input.readLong("Student ID: ");

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent found:");
        System.out.println(student);
    }

    private void updateStudent() {
        System.out.println("\n--- Update Student ---");

        Long id = input.readLong("Student ID: ");

        Student current = studentController.getStudentById(id);

        System.out.println("\nCurrent record:");
        System.out.println(current);

        System.out.println("""
                
                Select field to update:
                1. Name
                2. Email
                3. DepartmentEnum
                4. GPA
                5. Seat Number
                6. All fields
                """);

        int choice = input.readInt("Choose field: ");

        Student updated;

        switch (choice) {

            case 1 -> {
                String name = input.readRequiredText("New name: ");
                updated = studentController.updateName(id, name);
            }

            case 2 -> {
                String email = input.readRequiredText("New email: ");
                updated = studentController.updateEmail(id, email);
            }

            case 3 -> {
                DepartmentEnum department = input.readDepartment("Department: ");
                updated = studentController.updateDepartment(id, department);
            }

            case 4 -> {
                double gpa = input.readDouble("New GPA (0.0 - 4.0): ");
                updated = studentController.updateGpa(id, gpa);
            }

            case 5 -> {
                String seatNo = input.readRequiredText("New seat number: ");
                updated = studentController.updateSeatNo(id, seatNo);
            }

            case 6 -> {
                String name = input.readRequiredText("New name: ");
                String seatNo = input.readRequiredText("New seat number: ");
                String email = input.readRequiredText("New email: ");
                DepartmentEnum department = input.readDepartment("Department: ");
                double gpa = input.readDouble("New GPA (0.0 - 4.0): ");

                updated = studentController.updateAll(id, name, seatNo, email, department, gpa);
            }

            default -> {
                System.out.println("Invalid update option. Choose 1 to 6.");
                return;
            }
        }

        System.out.println("\nStudent updated successfully.");
        System.out.println(updated);
    }

    private void deleteStudent() {
        System.out.println("\n--- Delete Student ---");

        Long id = input.readLong("Student ID: ");

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent to delete:");
        System.out.println(student);

        if (input.readYesNo("Are you sure you want to delete this student?")) {
            studentController.deleteStudent(id);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private void searchStudents() {
        System.out.println("""
                
                --- Search Students ---
                1. Search by name
                2. Filter by department
                3. Filter by GPA range
                4. Sort by name
                5. Sort by GPA (highest first)
                6. Sort by ID
                """);

        int choice = input.readInt("Choose search option: ");

        List<Student> results;

        switch (choice) {

            case 1 -> {
                String keyword = input.readRequiredText("Name keyword: ");
                results = studentController.searchByName(keyword);
            }

            case 2 -> {
                DepartmentEnum department = input.readDepartment("Department: ");
                results = studentController.filterByDepartment(department);
            }

            case 3 -> {
                double minimum = input.readDouble("Minimum GPA: ");
                double maximum = input.readDouble("Maximum GPA: ");

                if (minimum > maximum) {
                    throw new ValidationException("Minimum GPA cannot exceed maximum GPA.");
                }

                results = studentController.filterByGpa(minimum, maximum);
            }

            case 4 -> results = studentController.sortByName();

            case 5 -> results = studentController.sortByGpaDescending();

            case 6 -> results = studentController.sortById();

            default -> {
                System.out.println("Invalid search option. Choose 1 to 6.");
                return;
            }
        }

        printResults(results);
    }

    private void printResults(List<Student> students) {
        System.out.println();

        if (students.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }

        students.forEach(System.out::println);

        System.out.println("\nResults: " + students.size());
    }

    private void printWelcome() {
        System.out.println("""
                
                ========================================
                   STUDENT MANAGEMENT SYSTEM
                ========================================
                """);
    }

    private void printMenu() {
        System.out.println("""
                
                ----------------------------------------
                1. Add Student
                2. View All Students
                3. Find Student By ID
                4. Update Student
                5. Delete Student
                6. Search Students
                0. Exit
                ----------------------------------------
                """);
    }
}