package org.example.enums;

public enum DepartmentEnum {
    COMPUTER_SCIENCE("CS", "Computer Science"),
    SOFTWARE_ENGINEERING("SE", "Software Engineering"),
    INFORMATION_TECHNOLOGY("IT", "Information Technology"),
    ARTIFICIAL_INTELLIGENCE("AI", "Artificial Intelligence"),
    DATA_SCIENCE("DS", "Data Science");

    private final String code;
    private final String displayDepartName;

    DepartmentEnum(String code, String displayDepartName) {
        this.code = code;
        this.displayDepartName = displayDepartName;
    }

    public static DepartmentEnum fromInput(String input) {
        for (DepartmentEnum department : values()) {
            if (department.code.equalsIgnoreCase(input) || department.displayDepartName.equalsIgnoreCase(input)) {
                return department;
            }
        }

        throw new IllegalArgumentException("Invalid department.");
    }
    @Override
    public String toString() {
        return displayDepartName;
    }
}
