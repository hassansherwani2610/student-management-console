package org.example.ui;

import org.example.controller.CourseController;
import org.example.controller.DepartmentController;
import org.example.controller.StudentController;
import org.example.exception.DuplicateStudentException;
import org.example.exception.StudentNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.Course;
import org.example.model.Department;
import org.example.model.Student;

import java.util.List;

public class ConsoleApplication {

    private final StudentController studentController;
    private final DepartmentController departmentController;
    private final CourseController courseController;
    private final InputHandler input;
    private boolean running = true;

    public ConsoleApplication(StudentController studentController, DepartmentController departmentController, CourseController courseController, InputHandler input) {
        this.studentController = studentController;
        this.departmentController = departmentController;
        this.courseController = courseController;
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

            case 7 -> addDepartment();

            case 8 -> viewAllDepartments();

            case 9 -> updateDepartment();

            case 10 -> deleteDepartment();

            case 11 -> addCourse();

            case 12 -> viewAllCourses();

            case 13 -> updateCourse();

            case 14 -> deleteCourse();

            case 0 -> running = false;

            default -> System.out.println("Invalid menu option. Choose a number from 0 to 14.");
        }
    }

    private void addStudent() {

        System.out.println("\n--- Add Student ---");

        Long id = input.readLong("Student ID: ");

        String name = input.readRequiredText("Name: ");

        String seatNo = input.readRequiredText("Seat Number: ");

        String email = input.readRequiredText("Email: ");

        Long departmentId = selectDepartment();

        double gpa = input.readDouble("\nGPA (0.0 - 4.0): ");

        Student student = studentController.createStudent(id, name, seatNo, email, departmentId, gpa);

        System.out.println("\nStudent created successfully.");

        System.out.println(student.toString(getDepartmentName(student)));
    }

    private Long selectDepartment() {

        List<Department> departments =
                departmentController.getAllDepartments();

        if (departments.isEmpty()) {
            throw new ValidationException("No departments available. Please add a department first.");
        }

        System.out.println("\nAvailable Departments:");

        departments.forEach(System.out::println);

        return input.readLong("Department ID: ");
    }

    private void viewAllStudents() {

        System.out.println("\n--- All Students ---");

        List<Student> students = studentController.sortById();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        students.forEach(student -> System.out.println(student.toString(getDepartmentName(student))));

        System.out.println("\nTotal: " + students.size());
    }

    private void findStudentById() {

        System.out.println("\n--- Find Student ---");

        Long id = input.readLong("Student ID: ");

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent found:");

        System.out.println(student.toString(getDepartmentName(student)));
    }

    private void updateStudent() {

        System.out.println("\n--- Update Student ---");

        Long id = input.readLong("Student ID: ");

        Student current = studentController.getStudentById(id);

        System.out.println("\nCurrent record:");

        System.out.println(current.toString(getDepartmentName(current)));

        System.out.println("""
                
                Select field to update:
                1. Name
                2. Email
                3. Department
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
                Long departmentId = selectDepartment();

                updated = studentController.updateDepartment(id, departmentId);
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

                Long departmentId = selectDepartment();

                double gpa = input.readDouble("New GPA (0.0 - 4.0): ");

                updated = studentController.updateAll(id, name, seatNo, email, departmentId, gpa);
            }

            default -> {
                System.out.println("Invalid update option. Choose 1 to 6.");

                return;
            }
        }

        System.out.println("\nStudent updated successfully.");

        System.out.println(updated.toString(getDepartmentName(updated)));
    }

    private void deleteStudent() {

        System.out.println("\n--- Delete Student ---");

        Long id = input.readLong("Student ID: ");

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent to delete:");

        System.out.println(student.toString(getDepartmentName(student)));

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
                Long departmentId = selectDepartment();

                results = studentController.filterByDepartment(departmentId);
            }

            case 3 -> {
                double minimum = input.readDouble("Minimum GPA: ");

                double maximum = input.readDouble("Maximum GPA: ");

                if (minimum > maximum) {
                    throw new ValidationException(
                            "Minimum GPA cannot exceed maximum GPA.");
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

    private void addDepartment() {

        System.out.println("\n--- Add Department ---");

        Long id = input.readLong("Department ID: ");

        String name = input.readRequiredText("Department name: ");

        Department department = departmentController.createDepartment(id, name);

        System.out.println("\nDepartment created successfully.");

        System.out.println(department);
    }

    private void viewAllDepartments() {

        System.out.println("\n--- All Departments ---");

        List<Department> departments = departmentController.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments found.");

            return;
        }

        departments.forEach(System.out::println);

        System.out.println("\nTotal: " + departments.size());
    }

    private void updateDepartment() {

        System.out.println("\n--- Update Department ---");

        Long id = input.readLong("Department ID: ");

        Department current = departmentController.getDepartmentById(id);

        System.out.println("\nCurrent record:");

        System.out.println(current);

        String newName = input.readRequiredText("New department name: ");

        Department updated = departmentController.updateDepartment(id, newName);

        System.out.println("\nDepartment updated successfully.");

        System.out.println(updated);
    }

    private void deleteDepartment() {
        System.out.println("\n--- Delete Department ---");

        Long id = input.readLong("Department ID: ");

        Department department = departmentController.getDepartmentById(id);

        System.out.println("\nDepartment to delete:");

        System.out.println(department);

        if (input.readYesNo("Are you sure you want to delete this department?")) {
            departmentController.deleteDepartment(id);

            System.out.println("Department deleted successfully.");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private void addCourse() {

        System.out.println("\n--- Add Course ---");

        Long id = input.readLong("Course ID: ");

        String name = input.readRequiredText("Course name: ");

        String code = input.readRequiredText("Course code: ");

        int creditHours = input.readInt("Credit hours: ");

        Course course = courseController.createCourse(
                id,
                name,
                code,
                creditHours
        );

        System.out.println("\nCourse created successfully.");

        System.out.println(course);
    }

    private void viewAllCourses() {

        System.out.println("\n--- All Courses ---");

        List<Course> courses = courseController.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");

            return;
        }

        courses.forEach(System.out::println);

        System.out.println("\nTotal: " + courses.size());
    }

    private void updateCourse() {

        System.out.println("\n--- Update Course ---");

        Long id = input.readLong("Course ID: ");

        Course current = courseController.getCourseById(id);

        System.out.println("\nCurrent record:");

        System.out.println(current);

        String name = input.readRequiredText("New course name: ");

        String code = input.readRequiredText("New course code: ");

        int creditHours = input.readInt("New credit hours: ");

        Course updated = courseController.updateCourse(
                id,
                name,
                code,
                creditHours
        );

        System.out.println("\nCourse updated successfully.");

        System.out.println(updated);
    }

    private void deleteCourse() {

        System.out.println("\n--- Delete Course ---");

        Long id = input.readLong("Course ID: ");

        Course course = courseController.getCourseById(id);

        System.out.println("\nCourse to delete:");

        System.out.println(course);

        if (input.readYesNo("Are you sure you want to delete this course?")) {
            courseController.deleteCourse(id);

            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private String getDepartmentName(Student student) {
        return departmentController.getDepartmentById(student.getDepartment()).getName();
    }

    private void printResults(List<Student> students) {

        System.out.println();

        if (students.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }

        students.forEach(student -> System.out.println(student.toString(getDepartmentName(student))));

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
                7. Add Department
                8. View All Departments
                9. Update Department
                10. Delete Department
                11. Add Course
                12. View All Courses
                13. Update Course
                14. Delete Course
                0. Exit
                ----------------------------------------
                """);
    }
}