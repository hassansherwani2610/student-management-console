package org.example.ui;

import org.example.exception.common.OperationCancelledException;

import java.util.Locale;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readMenuChoice(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new OperationCancelledException();
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid integer number.");
            }
        }
    }

    public double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new OperationCancelledException();
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public String readRequiredText(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            throw new OperationCancelledException();
        }

        return input;
    }

    public boolean readYesNo(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            if (input.equals("y") || input.equals("yes")) {
                return true;
            }

            if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("Please enter y or n.");
        }
    }
}