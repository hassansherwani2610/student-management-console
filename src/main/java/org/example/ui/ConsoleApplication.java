package org.example.ui;

import org.example.controller.CourseController;
import org.example.controller.DepartmentController;
import org.example.controller.EnrollmentController;
import org.example.controller.StudentController;
import org.example.exception.common.EntityInUseException;
import org.example.exception.common.OperationCancelledException;
import org.example.exception.common.ValidationException;
import org.example.exception.course.CourseNotFoundException;
import org.example.exception.course.DuplicateCourseException;
import org.example.exception.department.DepartmentNotFoundException;
import org.example.exception.department.DuplicateDepartmentException;
import org.example.exception.enrollment.DuplicateEnrollmentException;
import org.example.exception.enrollment.EnrollmentNotFoundException;
import org.example.exception.student.DuplicateStudentException;
import org.example.exception.student.StudentNotFoundException;
import org.example.model.Course;
import org.example.model.Department;
import org.example.model.Enrollment;
import org.example.model.Student;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ConsoleApplication {

    private final StudentController studentController;
    private final DepartmentController departmentController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final InputHandler input;
    private boolean running = true;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public ConsoleApplication(StudentController studentController, DepartmentController departmentController, CourseController courseController, EnrollmentController enrollmentController, InputHandler input) {
        this.studentController = studentController;
        this.departmentController = departmentController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        this.input = input;
    }

    public void start() {

        seedDefaults();
        printWelcome();

        while (running) {
            try {
                printMainMenu();

                int choice = input.readMenuChoice("Choose an option: ");

                switch (choice) {
                    case 1 -> studentMenu();
                    case 2 -> departmentMenu();
                    case 3 -> courseMenu();
                    case 4 -> enrollmentMenu();
                    case 0 -> running = false;
                    default -> System.out.println("\nPlease choose a number from 0 to 4.");
                }

            } catch (RuntimeException exception) {
                reportError(exception);
            }
        }

        System.out.println("\nThank you for using Student Management System. Goodbye!");
    }

    private void safely(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            reportError(exception);
        }
    }

    private void reportError(RuntimeException exception) {

        if (exception instanceof OperationCancelledException) {
            System.out.println("\nCancelled.");
        } else if (exception instanceof EntityInUseException) {
            System.out.println("\nCannot delete: " + exception.getMessage());
            System.out.println("Tip: remove the related records first, then try again.");
        } else if (exception instanceof ValidationException
                || exception instanceof StudentNotFoundException
                || exception instanceof DuplicateStudentException
                || exception instanceof DepartmentNotFoundException
                || exception instanceof DuplicateDepartmentException
                || exception instanceof CourseNotFoundException
                || exception instanceof DuplicateCourseException
                || exception instanceof EnrollmentNotFoundException
                || exception instanceof DuplicateEnrollmentException) {
            System.out.println("\nError: " + exception.getMessage());
        } else {
            System.out.println("\nUnexpected error: " + exception.getMessage());
        }
    }

    private void seedDefaults() {

        if (departmentController.getAllDepartments().isEmpty()) {
            List<String> names = List.of(
                    "Computer Science",
                    "Software Engineering",
                    "Electrical Engineering",
                    "Business Administration",
                    "Mathematics");

            long id = 1;
            for (String name : names) {
                try {
                    departmentController.createDepartment(id++, name);
                } catch (RuntimeException ignored) {
                }
            }
        }

        if (courseController.getAllCourses().isEmpty()) {
            seedCourse(1L, "Programming Fundamentals", "CS101", 3);
            seedCourse(2L, "Data Structures", "CS201", 3);
            seedCourse(3L, "Database Systems", "CS301", 3);
            seedCourse(4L, "Calculus I", "MT101", 3);
        }
    }

    private void seedCourse(Long id, String name, String code, int creditHours) {
        try {
            courseController.createCourse(id, name, code, creditHours);
        } catch (RuntimeException ignored) {
        }
    }

    private void studentMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("""
                    
                    ------------- STUDENTS -------------
                    1. Register a new student
                    2. View all students
                    3. Find a student
                    4. Update a student
                    5. Delete a student
                    6. Search / filter / sort
                    0. Back to main menu
                    ------------------------------------
                    """);

            int choice = input.readMenuChoice("Choose an option: ");

            switch (choice) {
                case 1 -> safely(this::addStudent);
                case 2 -> safely(this::viewAllStudents);
                case 3 -> safely(this::findStudent);
                case 4 -> safely(this::updateStudent);
                case 5 -> safely(this::deleteStudent);
                case 6 -> safely(this::searchStudents);
                case 0 -> back = true;
                default -> System.out.println("\nPlease choose a number from 0 to 6.");
            }
        }
    }

    private void departmentMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("""
                    
                    ----------- DEPARTMENTS ------------
                    1. Add a department
                    2. View all departments
                    3. Rename a department
                    4. Delete a department
                    0. Back to main menu
                    ------------------------------------
                    """);

            int choice = input.readMenuChoice("Choose an option: ");

            switch (choice) {
                case 1 -> safely(this::addDepartment);
                case 2 -> safely(this::viewAllDepartments);
                case 3 -> safely(this::updateDepartment);
                case 4 -> safely(this::deleteDepartment);
                case 0 -> back = true;
                default -> System.out.println("\nPlease choose a number from 0 to 4.");
            }
        }
    }

    private void courseMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("""
                    
                    -------------- COURSES -------------
                    1. Add a course
                    2. View all courses
                    3. Update a course
                    4. Delete a course
                    0. Back to main menu
                    ------------------------------------
                    """);

            int choice = input.readMenuChoice("Choose an option: ");

            switch (choice) {
                case 1 -> safely(this::addCourse);
                case 2 -> safely(this::viewAllCourses);
                case 3 -> safely(this::updateCourse);
                case 4 -> safely(this::deleteCourse);
                case 0 -> back = true;
                default -> System.out.println("\nPlease choose a number from 0 to 4.");
            }
        }
    }

    private void enrollmentMenu() {

        boolean back = false;

        while (!back) {
            System.out.println("""
                    
                    ------------ ENROLLMENTS -----------
                    1. Enroll a student in a course
                    2. View all enrollments
                    3. Update an enrollment
                    4. Delete an enrollment
                    0. Back to main menu
                    ------------------------------------
                    """);

            int choice = input.readMenuChoice("Choose an option: ");

            switch (choice) {
                case 1 -> safely(this::addEnrollment);
                case 2 -> safely(this::viewAllEnrollments);
                case 3 -> safely(this::updateEnrollment);
                case 4 -> safely(this::deleteEnrollment);
                case 0 -> back = true;
                default -> System.out.println("\nPlease choose a number from 0 to 4.");
            }
        }
    }

    private Long selectDepartment() {

        while (true) {
            List<Department> departments = departmentController.getAllDepartments();

            System.out.println("\nSelect a department:");

            if (departments.isEmpty()) {
                System.out.println("  (no departments yet)");
            }

            for (int i = 0; i < departments.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + departments.get(i).getName());
            }

            System.out.println("  0. + Add a new department");

            int choice = input.readInt("Your choice: ");

            if (choice == 0) {
                Department created = addDepartment();
                if (created != null) {
                    return created.getId();
                }
            } else if (choice >= 1 && choice <= departments.size()) {
                return departments.get(choice - 1).getId();
            } else {
                System.out.println("Please choose a number from 0 to " + departments.size() + ".");
            }
        }
    }

    private Long selectStudent(boolean allowAdd) {

        while (true) {
            List<Student> students = studentController.sortById();

            if (students.isEmpty() && !allowAdd) {
                throw new ValidationException("There are no students yet. Register a student first.");
            }

            System.out.println("\nSelect a student:");

            if (students.isEmpty()) {
                System.out.println("  (no students yet)");
            }

            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                System.out.println("  " + (i + 1) + ". " + s.getName() + "  [ID " + s.getId() + "]");
            }

            if (allowAdd) {
                System.out.println("  0. + Register a new student");
            }

            int choice = input.readInt("Your choice: ");

            if (allowAdd && choice == 0) {
                Student created = addStudent();
                if (created != null) {
                    return created.getId();
                }
            } else if (choice >= 1 && choice <= students.size()) {
                return students.get(choice - 1).getId();
            } else {
                System.out.println("Please choose a valid number from the list.");
            }
        }
    }

    private Long selectCourse(boolean allowAdd) {

        while (true) {
            List<Course> courses = courseController.getAllCourses();

            if (courses.isEmpty() && !allowAdd) {
                throw new ValidationException("There are no courses yet. Add a course first.");
            }

            System.out.println("\nSelect a course:");

            if (courses.isEmpty()) {
                System.out.println("  (no courses yet)");
            }

            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                System.out.println("  " + (i + 1) + ". " + c.getCode() + " - " + c.getName());
            }

            if (allowAdd) {
                System.out.println("  0. + Add a new course");
            }

            int choice = input.readInt("Your choice: ");

            if (allowAdd && choice == 0) {
                Course created = addCourse();
                if (created != null) {
                    return created.getId();
                }
            } else if (choice >= 1 && choice <= courses.size()) {
                return courses.get(choice - 1).getId();
            } else {
                System.out.println("Please choose a valid number from the list.");
            }
        }
    }

    private Long selectEnrollment() {

        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();

        if (enrollments.isEmpty()) {
            throw new ValidationException("There are no enrollments yet.");
        }

        while (true) {
            System.out.println("\nSelect an enrollment:");

            for (int i = 0; i < enrollments.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + describe(enrollments.get(i)));
            }

            int choice = input.readInt("Your choice: ");

            if (choice >= 1 && choice <= enrollments.size()) {
                return enrollments.get(choice - 1).getId();
            }

            System.out.println("Please choose a number from 1 to " + enrollments.size() + ".");
        }
    }

    private Student addStudent() {

        System.out.println("\n--- Register Student ---");
        System.out.println("Fill in the details below. A student ID is generated automatically.");

        while (true) {
            String name = input.readRequiredText("Full name: ");
            String seatNo = input.readRequiredText("Seat number: ");
            String email = readValidEmail("Email: ");
            Long departmentId = selectDepartment();
            double gpa = readValidGpa("\nGPA (0.0 - 4.0): ");

            try {
                Student student = studentController.createStudent(nextStudentId(), name, seatNo, email, departmentId, gpa);

                System.out.println("\nStudent registered successfully.");
                System.out.println(student.toString(getDepartmentName(student)));

                return student;

            } catch (ValidationException | DuplicateStudentException exception) {
                System.out.println("\nError: " + exception.getMessage());

                if (!input.readYesNo("Re-enter the details?")) {
                    return null;
                }
            }
        }
    }

    private void viewAllStudents() {

        System.out.println("\n--- All Students ---");

        List<Student> students = studentController.sortById();

        if (students.isEmpty()) {
            System.out.println("No students yet. Choose \"Register a new student\" to add the first one.");
            return;
        }

        students.forEach(student -> System.out.println(student.toString(getDepartmentName(student))));

        System.out.println("\nTotal: " + students.size());
    }

    private void findStudent() {

        System.out.println("\n--- Find Student ---");

        Long id = selectStudent(false);

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent found:");
        System.out.println(student.toString(getDepartmentName(student)));
    }

    private void updateStudent() {

        System.out.println("\n--- Update Student ---");

        Long id = selectStudent(false);

        Student current = studentController.getStudentById(id);

        System.out.println("\nCurrent record:");
        System.out.println(current.toString(getDepartmentName(current)));

        System.out.println("""
                
                What would you like to update?
                1. Name
                2. Email
                3. Department
                4. GPA
                5. Seat number
                6. Everything
                0. Cancel
                """);

        int choice = input.readInt("Choose field: ");

        Student updated;

        switch (choice) {

            case 1 -> updated = studentController.updateName(id, input.readRequiredText("New name: "));

            case 2 -> updated = updateEmailWithRetry(id);

            case 3 -> updated = studentController.updateDepartment(id, selectDepartment());

            case 4 -> updated = studentController.updateGpa(id, readValidGpa("New GPA (0.0 - 4.0): "));

            case 5 -> updated = studentController.updateSeatNo(id, input.readRequiredText("New seat number: "));

            case 6 -> updated = updateAllWithRetry(id);

            case 0 -> {
                System.out.println("Update cancelled.");
                return;
            }

            default -> {
                System.out.println("Please choose a number from 0 to 6.");
                return;
            }
        }

        System.out.println("\nStudent updated successfully.");
        System.out.println(updated.toString(getDepartmentName(updated)));
    }

    private Student updateEmailWithRetry(Long id) {
        while (true) {
            String email = readValidEmail("New email: ");
            try {
                return studentController.updateEmail(id, email);
            } catch (DuplicateStudentException exception) {
                System.out.println("\nError: " + exception.getMessage());
            }
        }
    }

    private Student updateAllWithRetry(Long id) {
        while (true) {
            String name = input.readRequiredText("New name: ");
            String seatNo = input.readRequiredText("New seat number: ");
            String email = readValidEmail("New email: ");
            Long departmentId = selectDepartment();
            double gpa = readValidGpa("New GPA (0.0 - 4.0): ");

            try {
                return studentController.updateAll(id, name, seatNo, email, departmentId, gpa);
            } catch (ValidationException | DuplicateStudentException exception) {
                System.out.println("\nError: " + exception.getMessage());
            }
        }
    }

    private void deleteStudent() {

        System.out.println("\n--- Delete Student ---");

        Long id = selectStudent(false);

        Student student = studentController.getStudentById(id);

        System.out.println("\nStudent to delete:");
        System.out.println(student.toString(getDepartmentName(student)));

        if (input.readYesNo("Are you sure you want to delete this student?")) {
            studentController.deleteStudent(id);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
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
                0. Cancel
                """);

        int choice = input.readInt("Choose an option: ");

        List<Student> results;

        switch (choice) {

            case 1 -> results = studentController.searchByName(input.readRequiredText("Name keyword: "));

            case 2 -> results = studentController.filterByDepartment(selectDepartment());

            case 3 -> {
                double minimum = readValidGpa("Minimum GPA (0.0 - 4.0): ");
                double maximum = readValidGpa("Maximum GPA (0.0 - 4.0): ");

                if (minimum > maximum) {
                    throw new ValidationException("Minimum GPA cannot exceed maximum GPA.");
                }

                results = studentController.filterByGpa(minimum, maximum);
            }

            case 4 -> results = studentController.sortByName();

            case 5 -> results = studentController.sortByGpaDescending();

            case 6 -> results = studentController.sortById();

            case 0 -> {
                return;
            }

            default -> {
                System.out.println("Please choose a number from 0 to 6.");
                return;
            }
        }

        printResults(results);
    }

    private Department addDepartment() {

        System.out.println("\n--- Add Department ---");

        while (true) {
            String name = input.readRequiredText("Department name: ");

            if (departmentNameExists(name, null)) {
                System.out.println("\nA department named \"" + name + "\" is already present in the system. Please enter a different name.");
                continue;
            }

            try {
                Department department = departmentController.createDepartment(nextDepartmentId(), name);

                System.out.println("\nDepartment added: " + department.getName());

                return department;

            } catch (ValidationException | DuplicateDepartmentException exception) {
                System.out.println("\nError: " + exception.getMessage());

                if (!input.readYesNo("Re-enter the details?")) {
                    return null;
                }
            }
        }
    }

    private void viewAllDepartments() {

        System.out.println("\n--- All Departments ---");

        List<Department> departments = departmentController.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments yet.");
            return;
        }

        departments.forEach(System.out::println);

        System.out.println("\nTotal: " + departments.size());
    }

    private void updateDepartment() {

        System.out.println("\n--- Rename Department ---");

        Long id = selectDepartment();

        Department current = departmentController.getDepartmentById(id);

        System.out.println("\nCurrent name: " + current.getName());

        while (true) {
            String newName = input.readRequiredText("New department name: ");

            if (departmentNameExists(newName, id)) {
                System.out.println("\nA department named \"" + newName + "\" is already present in the system. Please enter a different name.");
                continue;
            }

            try {
                Department updated = departmentController.updateDepartment(id, newName);

                System.out.println("\nDepartment updated successfully.");
                System.out.println(updated);

                return;

            } catch (ValidationException | DuplicateDepartmentException exception) {
                System.out.println("\nError: " + exception.getMessage());
            }
        }
    }

    private void deleteDepartment() {

        System.out.println("\n--- Delete Department ---");

        Long id = selectDepartment();

        Department department = departmentController.getDepartmentById(id);

        System.out.println("\nDepartment to delete: " + department.getName());

        if (input.readYesNo("Are you sure you want to delete this department?")) {
            departmentController.deleteDepartment(id);
            System.out.println("Department deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private Course addCourse() {

        System.out.println("\n--- Add Course ---");
        System.out.println("A course ID is generated automatically.");

        while (true) {
            String name = input.readRequiredText("Course name (e.g. Data Structures): ");

            if (courseNameExists(name, null)) {
                System.out.println("\nA course named \"" + name + "\" is already present in the system. Please enter a different name.");
                continue;
            }

            String code = input.readRequiredText("Course code (e.g. CS201): ");
            int creditHours = input.readInt("Credit hours (e.g. 3): ");

            try {
                Course course = courseController.createCourse(nextCourseId(), name, code, creditHours);

                System.out.println("\nCourse added successfully.");
                System.out.println(course);

                return course;

            } catch (ValidationException | DuplicateCourseException exception) {
                System.out.println("\nError: " + exception.getMessage());

                if (!input.readYesNo("Re-enter the details?")) {
                    return null;
                }
            }
        }
    }

    private void viewAllCourses() {

        System.out.println("\n--- All Courses ---");

        List<Course> courses = courseController.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses yet. Choose \"Add a course\" to create one.");
            return;
        }

        courses.forEach(System.out::println);

        System.out.println("\nTotal: " + courses.size());
    }

    private void updateCourse() {

        System.out.println("\n--- Update Course ---");

        Long id = selectCourse(false);

        Course current = courseController.getCourseById(id);

        System.out.println("\nCurrent record:");
        System.out.println(current);

        while (true) {
            String name = input.readRequiredText("New course name: ");

            if (courseNameExists(name, id)) {
                System.out.println("\nA course named \"" + name + "\" is already present in the system. Please enter a different name.");
                continue;
            }

            String code = input.readRequiredText("New course code: ");
            int creditHours = input.readInt("New credit hours: ");

            try {
                Course updated = courseController.updateCourse(id, name, code, creditHours);

                System.out.println("\nCourse updated successfully.");
                System.out.println(updated);

                return;

            } catch (ValidationException | DuplicateCourseException exception) {
                System.out.println("\nError: " + exception.getMessage());
            }
        }
    }

    private void deleteCourse() {

        System.out.println("\n--- Delete Course ---");

        Long id = selectCourse(false);

        Course course = courseController.getCourseById(id);

        System.out.println("\nCourse to delete:");
        System.out.println(course);

        if (input.readYesNo("Are you sure you want to delete this course?")) {
            courseController.deleteCourse(id);
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void addEnrollment() {

        System.out.println("\n--- Enroll a Student in a Course ---");
        System.out.println("Pick the student and course. You can add new ones from the lists if needed.");

        Long studentId = selectStudent(true);
        Long courseId = selectCourse(true);

        while (true) {
            String semester = input.readRequiredText("\nSemester (e.g. 4th, 5th, 6th): ");
            String grade = input.readRequiredText("Grade (A, B, C, D, F): ");

            try {
                Enrollment enrollment = enrollmentController.createEnrollment(nextEnrollmentId(), studentId, courseId, semester, grade);

                System.out.println("\nEnrollment created successfully.");
                System.out.println(describe(enrollment));

                return;

            } catch (ValidationException | DuplicateEnrollmentException exception) {
                System.out.println("\nError: " + exception.getMessage());

                if (!input.readYesNo("Re-enter semester and grade?")) {
                    return;
                }
            }
        }
    }

    private void viewAllEnrollments() {

        System.out.println("\n--- All Enrollments ---");

        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments yet. Choose \"Enroll a student in a course\" to add one.");
            return;
        }

        enrollments.forEach(enrollment -> System.out.println(describe(enrollment)));

        System.out.println("\nTotal: " + enrollments.size());
    }

    private void updateEnrollment() {

        System.out.println("\n--- Update Enrollment ---");

        Long id = selectEnrollment();

        Enrollment current = enrollmentController.getEnrollmentById(id);

        System.out.println("\nCurrent record:");
        System.out.println(describe(current));

        Long studentId = selectStudent(false);
        Long courseId = selectCourse(false);

        while (true) {
            String semester = input.readRequiredText("\nSemester (e.g. 4th, 5th, 6th): ");
            String grade = input.readRequiredText("Grade (A, B, C, D, F): ");

            try {
                Enrollment updated = enrollmentController.updateEnrollment(id, studentId, courseId, semester, grade);

                System.out.println("\nEnrollment updated successfully.");
                System.out.println(describe(updated));

                return;

            } catch (ValidationException | DuplicateEnrollmentException exception) {
                System.out.println("\nError: " + exception.getMessage());

                if (!input.readYesNo("Re-enter semester and grade?")) {
                    return;
                }
            }
        }
    }

    private void deleteEnrollment() {

        System.out.println("\n--- Delete Enrollment ---");

        Long id = selectEnrollment();

        Enrollment enrollment = enrollmentController.getEnrollmentById(id);

        System.out.println("\nEnrollment to delete:");
        System.out.println(describe(enrollment));

        if (input.readYesNo("Are you sure you want to delete this enrollment?")) {
            enrollmentController.deleteEnrollment(id);
            System.out.println("Enrollment deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private String readValidEmail(String prompt) {
        while (true) {
            String email = input.readRequiredText(prompt);

            if (isValidEmail(email)) {
                return email;
            }

            System.out.println("Please enter a correct email address (e.g. name@example.com).");
        }
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private double readValidGpa(String prompt) {
        while (true) {
            double gpa = input.readDouble(prompt);

            if (gpa >= 0.0 && gpa <= 4.0) {
                return gpa;
            }

            System.out.println("Please enter a correct GPA between 0.0 and 4.0.");
        }
    }

    private boolean departmentNameExists(String name, Long excludeId) {
        return departmentController.getAllDepartments().stream()
                .anyMatch(department -> department.getName().equalsIgnoreCase(name)
                        && !department.getId().equals(excludeId));
    }

    private boolean courseNameExists(String name, Long excludeId) {
        return courseController.getAllCourses().stream()
                .anyMatch(course -> course.getName().equalsIgnoreCase(name)
                        && !course.getId().equals(excludeId));
    }

    private long nextStudentId() {
        return studentController.sortById().stream().mapToLong(Student::getId).max().orElse(0) + 1;
    }

    private long nextDepartmentId() {
        return departmentController.getAllDepartments().stream().mapToLong(Department::getId).max().orElse(0) + 1;
    }

    private long nextCourseId() {
        return courseController.getAllCourses().stream().mapToLong(Course::getId).max().orElse(0) + 1;
    }

    private long nextEnrollmentId() {
        return enrollmentController.getAllEnrollments().stream().mapToLong(Enrollment::getId).max().orElse(0) + 1;
    }

    private String getDepartmentName(Student student) {
        return departmentController.getDepartmentById(student.getDepartmentId()).getName();
    }

    private String describe(Enrollment enrollment) {

        String studentName = lookup(() -> studentController.getStudentById(enrollment.getStudentId()).getName());

        String courseLabel = lookup(() -> {
            Course course = courseController.getCourseById(enrollment.getCourseId());
            return course.getCode() + " - " + course.getName();
        });

        return "Enrollment #" + enrollment.getId() + ": " + studentName + " -> " + courseLabel
                + " | " + enrollment.getSemester() + " | Grade: " + enrollment.getGrade();
    }

    private String lookup(Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException exception) {
            return "(unknown)";
        }
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
                Tip: type the number of an option and press Enter.
                At any question, press Enter on an empty line
                to cancel and go back.
                Sample departments and courses are already loaded,
                so you can register your first student right away.
                """);
    }

    private void printMainMenu() {

        int students = studentController.sortById().size();
        int departments = departmentController.getAllDepartments().size();
        int courses = courseController.getAllCourses().size();
        int enrollments = enrollmentController.getAllEnrollments().size();

        System.out.println("""
                
                ============== MAIN MENU ==============
                """
                + "  Total Students: " + students
                + "  |  Total Departments: " + departments
                + "  |  Total Courses: " + courses
                + "  |  Total Enrollments: " + enrollments + "\n");

        System.out.println("""
                  1. Students
                  2. Departments
                  3. Courses
                  4. Enrollments
                  0. Exit
                =======================================
                """);

        if (students == 0) {
            System.out.println("Next step: open Students -> Register a new student.");
        } else if (enrollments == 0) {
            System.out.println("Next step: open Enrollments -> Enroll a student in a course.");
        }
    }
}