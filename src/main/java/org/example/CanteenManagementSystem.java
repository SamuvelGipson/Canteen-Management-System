package org.example;

import java.util.*;
import java.util.stream.Collectors;

class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Price: $%.2f, Stock: %d", id, name, price, stock);
    }
}

class Order {
    private static int orderCounter = 1;
    private int id;
    private List<Product> items;
    private double total;

    public Order() {
        this.id = orderCounter++;
        this.items = new ArrayList<>();
        this.total = 0.0;
    }

    public void addItem(Product product) {
        items.add(product);
        total += product.getPrice();
    }

    public int getId() { return id; }
    public List<Product> getItems() { return items; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id).append("\n");
        for (Product item : items) {
            sb.append(item.getName()).append(" - $").append(String.format("%.2f", item.getPrice())).append("\n");
        }
        sb.append("Total: $").append(String.format("%.2f", total));
        return sb.toString();
    }
}

class ReportGenerator {
    public static void generateSalesReport(List<Order> orders) {
        System.out.println("\n--- Sales Report ---");
        double totalRevenue = orders.stream().mapToDouble(Order::getTotal).sum();
        long totalOrders = orders.size();
        double averageOrderValue = totalRevenue / totalOrders;

        System.out.printf("Total Revenue: $%.2f\n", totalRevenue);
        System.out.printf("Total Orders: %d\n", totalOrders);
        System.out.printf("Average Order Value: $%.2f\n", averageOrderValue);
    }

    public static void generatePopularItemsReport(List<Order> orders) {
        System.out.println("\n--- Popular Items Report ---");
        Map<String, Long> itemCounts = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

        itemCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.printf("%s: %d orders\n", entry.getKey(), entry.getValue()));
    }

    public static void generateInventoryReport(List<Product> inventory) {
        System.out.println("\n--- Inventory Report ---");
        inventory.forEach(product -> {
            System.out.printf("%s - Stock: %d, Value: $%.2f\n",
                    product.getName(), product.getStock(), product.getPrice() * product.getStock());
        });

        double totalInventoryValue = inventory.stream()
                .mapToDouble(product -> product.getPrice() * product.getStock())
                .sum();
        System.out.printf("Total Inventory Value: $%.2f\n", totalInventoryValue);
    }
}

public class CanteenManagementSystem {
    private List<Product> inventory;
    private List<Order> orders;
    private Scanner scanner;
    private UserAuth userAuth;
    private MenuCustomizer menuCustomizer;

    public CanteenManagementSystem() {
        inventory = new ArrayList<>();
        orders = new ArrayList<>();
        scanner = new Scanner(System.in);
        userAuth = new UserAuth();
        menuCustomizer = new MenuCustomizer(inventory);
        initializeInventory();
    }

    private void initializeInventory() {
        inventory.add(new Product(1, "Burger", 5.99, 20));
        inventory.add(new Product(2, "Pizza", 8.99, 15));
        inventory.add(new Product(3, "Salad", 4.99, 25));
        inventory.add(new Product(4, "Soda", 1.99, 50));
        FileHandler.saveInventory(inventory);
    }

    public void run() {
        while (true) {
            if (!userAuth.login()) {
                continue;
            }

            while (true) {
                System.out.println("\n--- College Canteen Management System ---");
                System.out.println("1. View Menu");
                System.out.println("2. Place Order");
                System.out.println("3. View Orders");
                System.out.println("4. Manage Inventory");
                System.out.println("5. Generate Reports");
                System.out.println("6. Customize Menu");
                System.out.println("7. Change Password");
                System.out.println("8. Save and Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewMenu();
                        break;
                    case 2:
                        placeOrder();
                        break;
                    case 3:
                        viewOrders();
                        break;
                    case 4:
                        manageInventory();
                        break;
                    case 5:
                        generateReports();
                        break;
                    case 6:
                        menuCustomizer.customizeMenu();
                        break;
                    case 7:
                        changePassword();
                        break;
                    case 8:
                        saveAndExit();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private void viewMenu() {
        System.out.println("\n--- Menu ---");
        for (Product product : inventory) {
            System.out.println(product);
        }
    }

    private void placeOrder() {
        Order order = new Order();
        while (true) {
            viewMenu();
            System.out.print("Enter product ID to add to order (0 to finish): ");
            int productId = scanner.nextInt();
            if (productId == 0) break;

            Product product = findProductById(productId);
            if (product != null && product.getStock() > 0) {
                order.addItem(product);
                product.setStock(product.getStock() - 1);
                System.out.println("Added " + product.getName() + " to the order.");
            } else {
                System.out.println("Invalid product ID or out of stock.");
            }
        }

        if (!order.getItems().isEmpty()) {
            orders.add(order);
            System.out.println("Order placed successfully.");
            System.out.println(order);
            FileHandler.saveOrders(orders);
            FileHandler.saveInventory(inventory);
        } else {
            System.out.println("Order cancelled - no items added.");
        }
    }

    private void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
            return;
        }

        System.out.println("\n--- Orders ---");
        for (Order order : orders) {
            System.out.println(order);
            System.out.println("--------------------");
        }
    }

    private void manageInventory() {
        while (true) {
            System.out.println("\n--- Manage Inventory ---");
            System.out.println("1. View Inventory");
            System.out.println("2. Add New Product");
            System.out.println("3. Update Product Stock");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    updateProductStock();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void generateReports() {
        System.out.println("\n--- Generate Reports ---");
        System.out.println("1. Sales Report");
        System.out.println("2. Popular Items Report");
        System.out.println("3. Inventory Report");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                ReportGenerator.generateSalesReport(orders);
                break;
            case 2:
                ReportGenerator.generatePopularItemsReport(orders);
                break;
            case 3:
                ReportGenerator.generateInventoryReport(inventory);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void changePassword() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        userAuth.changePassword(username);
    }

    private void addNewProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter initial stock: ");
        int stock = scanner.nextInt();

        int newId = inventory.size() + 1;
        Product newProduct = new Product(newId, name, price, stock);
        inventory.add(newProduct);
        System.out.println("New product added: " + newProduct);
        FileHandler.saveInventory(inventory);
    }

    private void updateProductStock() {
        viewMenu();
        System.out.print("Enter product ID to update stock: ");
        int productId = scanner.nextInt();
        Product product = findProductById(productId);

        if (product != null) {
            System.out.print("Enter new stock quantity: ");
            int newStock = scanner.nextInt();
            product.setStock(newStock);
            System.out.println("Stock updated for " + product.getName());
            FileHandler.saveInventory(inventory);
        } else {
            System.out.println("Invalid product ID.");
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

    private void saveAndExit() {
        FileHandler.saveInventory(inventory);
        FileHandler.saveOrders(orders);
        System.out.println("Data saved. Thank you for using the College Canteen Management System. Goodbye!");
    }

    public static void main(String[] args) {
        CanteenManagementSystem system = new CanteenManagementSystem();
        system.run();
    }
}