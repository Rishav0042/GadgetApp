-- ============================================
-- GadgetZone - Mobile Accessories Online Shop
-- Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS gadgetzone;
USE gadgetzone;

-- --------------------------------------------
-- Users Table
-- --------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    address TEXT,
    role ENUM('admin', 'user') NOT NULL DEFAULT 'user',
    status ENUM('active', 'inactive', 'pending') NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- Categories Table
-- --------------------------------------------
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image VARCHAR(255),
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- Products Table
-- --------------------------------------------
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category_id INT NOT NULL,
    brand VARCHAR(100),
    image VARCHAR(255),
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    INDEX idx_product_category (category_id),
    INDEX idx_product_brand (brand),
    INDEX idx_product_status (status)
);

-- --------------------------------------------
-- Orders Table
-- --------------------------------------------
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending',
    shipping_address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_order_user (user_id),
    INDEX idx_order_status (status)
);

-- --------------------------------------------
-- Order Items Table
-- --------------------------------------------
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_orderitem_order (order_id),
    INDEX idx_orderitem_product (product_id)
);

-- --------------------------------------------
-- Wishlist Table
-- --------------------------------------------
CREATE TABLE wishlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_wishlist (user_id, product_id),
    INDEX idx_wishlist_user (user_id)
);

-- --------------------------------------------
-- Reviews Table
-- --------------------------------------------
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_review (user_id, product_id),
    INDEX idx_review_product (product_id)
);

-- --------------------------------------------
-- Contacts Table
-- --------------------------------------------
CREATE TABLE contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('unread', 'read', 'replied') NOT NULL DEFAULT 'unread',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- Insert Default Admin User
-- Password: admin123 (BCrypt hashed)
-- --------------------------------------------
INSERT INTO users (username, email, password, full_name, phone, address, role, status)
VALUES ('admin', 'admin@gadgetzone.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrqJ6J7GqH7mYFQVQJ6b8J7GqH7mYFq', 'System Admin', '9800000000', 'GadgetZone HQ', 'admin', 'active');

-- --------------------------------------------
-- Insert Sample Categories
-- --------------------------------------------
INSERT INTO categories (name, description, status) VALUES
('Phone Cases', 'Protective cases for all phone models', 'active'),
('Screen Protectors', 'Tempered glass and film protectors', 'active'),
('Chargers', 'Wall chargers, car chargers, and wireless chargers', 'active'),
('Earphones & Headphones', 'Wired and wireless audio accessories', 'active'),
('Power Banks', 'Portable charging solutions', 'active'),
('Cables & Adapters', 'USB cables, charging cables, and adapters', 'active'),
('Mounts & Holders', 'Car mounts, desk stands, and holders', 'active'),
('Smartwatch Bands', 'Replacement bands for smartwatches', 'active');

-- --------------------------------------------
-- Insert Sample Products
-- --------------------------------------------
INSERT INTO products (name, description, price, stock, category_id, brand, status) VALUES
('Silicone Shockproof Case', 'Premium silicone case with shock absorption technology. Compatible with iPhone 15 series.', 12.99, 150, 1, 'Spigen', 'active'),
('Clear Transparent Case', 'Ultra-thin clear case showing the original phone design. Anti-yellowing technology.', 9.99, 200, 1, 'Ringke', 'active'),
('Leather Wallet Case', 'Genuine leather wallet case with card slots and kickstand feature.', 24.99, 80, 1, 'Mujjo', 'active'),
('Tempered Glass 9H', '9H hardness tempered glass screen protector with oleophobic coating.', 8.99, 300, 2, 'amFilm', 'active'),
('Privacy Screen Protector', 'Anti-spy privacy glass that blocks viewing from angles.', 14.99, 120, 2, 'Spigen', 'active'),
('20W Fast Charger', '20W USB-C fast charger with PD 3.0 support.', 19.99, 100, 3, 'Anker', 'active'),
('Wireless Charging Pad', '15W Qi wireless charging pad with LED indicator.', 24.99, 75, 3, 'Samsung', 'active'),
('Car Charger Dual USB', 'Dual USB car charger with 2.4A total output.', 11.99, 130, 3, 'Baseus', 'active'),
('Wireless Earbuds Pro', 'Active noise cancelling wireless earbuds with 30hr battery.', 49.99, 60, 4, 'Soundcore', 'active'),
('Over-Ear Headphones', 'Premium over-ear headphones with deep bass and 40hr battery.', 79.99, 40, 4, 'Sony', 'active'),
('Wired Earphones', 'Hi-fi wired earphones with inline mic and volume control.', 14.99, 200, 4, 'Panasonic', 'active'),
('10000mAh Power Bank', 'Slim 10000mAh portable charger with dual USB output.', 22.99, 90, 5, 'Anker', 'active'),
('20000mAh Power Bank', 'High capacity 20000mAh power bank with fast charging.', 34.99, 50, 5, 'Xiaomi', 'active'),
('USB-C to USB-C Cable', 'Braided USB-C cable 1m with 100W PD support.', 9.99, 250, 6, 'Anker', 'active'),
('Lightning to USB Cable', 'MFi certified lightning cable 1.2m for iPhone.', 12.99, 180, 6, 'Belkin', 'active'),
('Car Phone Mount', 'Magnetic car phone mount with 360 rotation.', 15.99, 110, 7, 'iOttie', 'active'),
('Desk Phone Stand', 'Adjustable aluminum desk stand for phones and tablets.', 18.99, 70, 7, 'Lamicall', 'active'),
('Silicone Sport Band', 'Premium silicone sport band for Apple Watch. Multiple colors.', 19.99, 140, 8, 'Apple', 'active'),
('Stainless Steel Band', 'Elegant stainless steel band for smartwatches.', 29.99, 50, 8, 'Samsung', 'active');
