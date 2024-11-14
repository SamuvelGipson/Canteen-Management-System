-- Create Products table
CREATE TABLE Products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);

-- Create Orders table
CREATE TABLE Orders (
    id INT PRIMARY KEY,
    total DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create OrderItems table (to represent the many-to-many relationship between Orders and Products)
CREATE TABLE OrderItems (
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    PRIMARY KEY (order_id, product_id)
);

-- Insert sample data into Products table
INSERT INTO Products (id, name, price, stock) VALUES
(1, 'Burger', 5.99, 20),
(2, 'Pizza', 8.99, 15),
(3, 'Salad', 4.99, 25),
(4, 'Soda', 1.99, 50);

-- Insert a sample order
INSERT INTO Orders (id, total) VALUES (1, 14.98);

-- Insert sample order items
INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 5.99),  -- One burger
(1, 4, 2, 1.99);  -- Two sodas