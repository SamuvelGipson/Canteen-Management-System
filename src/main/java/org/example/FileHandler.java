package org.example;

import java.util.*;
import java.io.*;

public class FileHandler {
    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String ORDERS_FILE = "orders.txt";

    public static void saveInventory(List<Product> inventory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            for (Product product : inventory) {
                writer.println(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getStock());
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static List<Product> loadInventory() {
        List<Product> inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int stock = Integer.parseInt(parts[3]);
                    inventory.add(new Product(id, name, price, stock));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
        return inventory;
    }

    public static void saveOrders(List<Order> orders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) {
                writer.println("Order ID: " + order.getId());
                for (Product item : order.getItems()) {
                    writer.println(item.getId() + "," + item.getName() + "," + item.getPrice());
                }
                writer.println("Total: " + order.getTotal());
                writer.println("---");
            }
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    public static List<Order> loadOrders(List<Product> inventory) {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            Order currentOrder = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID:")) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                    }
                    currentOrder = new Order();
                } else if (line.equals("---")) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                        currentOrder = null;
                    }
                } else if (!line.startsWith("Total:")) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        Product product = findProductById(inventory, id);
                        if (product != null && currentOrder != null) {
                            currentOrder.addItem(product);
                        }
                    }
                }
            }
            if (currentOrder != null) {
                orders.add(currentOrder);
            }
        } catch (IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
        return orders;
    }

    private static Product findProductById(List<Product> inventory, int id) {
        for (Product product : inventory) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}