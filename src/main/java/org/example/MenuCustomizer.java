package org.example;

import java.util.List;
import java.util.Scanner;

public class MenuCustomizer {
    private List<Product> inventory;
    private Scanner scanner;

    public MenuCustomizer(List<Product> inventory) {
        this.inventory = inventory;
        this.scanner = new Scanner(System.in);
    }

    public void customizeMenu() {
        while (true) {
            System.out.println("\n--- Menu Customization ---");
            System.out.println("1. Add new category");
            System.out.println("2. Remove category");
            System.out.println("3. Add product to category");
            System.out.println("4. Remove product from category");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    removeCategory();
                    break;
                case 3:
                    addProductToCategory();
                    break;
                case 4:
                    removeProductFromCategory();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addCategory() {
        System.out.print("Enter new category name: ");
        String categoryName = scanner.nextLine();
        // In a real implementation, you would add the category to a data structure
        System.out.println("Category '" + categoryName + "' added successfully.");
    }

    private void removeCategory() {
        System.out.print("Enter category name to remove: ");
        String categoryName = scanner.nextLine();
        // In a real implementation, you would remove the category from a data structure
        System.out.println("Category '" + categoryName + "' removed successfully.");
    }

    private void addProductToCategory() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        System.out.print("Enter product ID to add: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product product = findProductById(productId);
        if (product != null) {
            // In a real implementation, you would add the product to the specified category
            System.out.println(product.getName() + " added to category '" + categoryName + "'.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void removeProductFromCategory() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        System.out.print("Enter product ID to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product product = findProductById(productId);
        if (product != null) {
            // In a real implementation, you would remove the product from the specified category
            System.out.println(product.getName() + " removed from category '" + categoryName + "'.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private Product findProductById(int id) {
        for (Product product : inventory) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
