package org.example.model;

import org.example.exception.ValidationException;

import java.util.List;
import java.util.Locale;

public class Enrollment {

    private static final List<String> VALID_GRADES = List.of( "A", "B","C", "D", "F");

    private final Long id;
    private Long studentId;
    private Long courseId;
    private String semester;
    private String grade;

    public Enrollment(Long id, Long studentId, Long courseId, String semester, String grade) {
        if (id == null || id <= 0) {
            throw new ValidationException("Enrollment ID must be greater than zero.");
        }

        this.id = id;

        setStudentId(studentId);
        setCourseId(courseId);
        setSemester(semester);
        setGrade(grade);
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new ValidationException("Student ID must be greater than zero.");
        }

        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new ValidationException("Course ID must be greater than zero.");
        }

        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        if (semester == null || semester.isBlank()) {
            throw new ValidationException("Semester cannot be empty.");
        }

        this.semester = semester.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        if (grade == null || grade.isBlank()) {
            throw new ValidationException("Grade cannot be empty.");
        }

        String formattedGrade = grade.trim().toUpperCase(Locale.ROOT);

        if (!VALID_GRADES.contains(formattedGrade)) {
            throw new ValidationException("Invalid grade \"" + grade.trim() + "\". Allowed grades: " + String.join(", ", VALID_GRADES) + ".");
        }

        this.grade = formattedGrade;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Student ID: " + studentId + " | Course ID: " + courseId + " | Semester: " + semester + " | Grade: " + grade;
    }
}
