package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserAuth {
    private Map<String, String> users;
    private Scanner scanner;

    public UserAuth() {
        users = new HashMap<>();
        users.put("admin", "admin123");
        users.put("cashier", "cash123");
        scanner = new Scanner(System.in);
    }

    public boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }

    public void register() {
        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine();
        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        users.put(newUsername, newPassword);
        System.out.println("User registered successfully!");
    }

    public void changePassword(String username) {
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        if (!users.get(username).equals(currentPassword)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        users.put(username, newPassword);
        System.out.println("Password changed successfully!");
    }
}
