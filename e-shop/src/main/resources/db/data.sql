-- Categories
INSERT INTO categories (name) VALUES
('Electronics'),
('Books'),
('Clothing');

-- Products
INSERT INTO products (name, brand ,description, price, image_path, features, category_id, stock) VALUES
('Smartphone X', 'kCn','Latest smartphone with OLED display', 999.99, '/uploads/products/smartphone_x.jpg', 'OLED display, 5G, 128GB', 1, 100),
('Laptop Pro', 'kCn','High performance laptop', 1499.99, '/uploads/products/laptop_pro.jpg', '16GB RAM, SSD 512GB, i7 CPU', 1, 50),
('Fantasy Book', 'kCn','Epic fantasy novel', 19.99, '/uploads/products/fantasy_book.jpg', 'Hardcover, 500 pages', 2, 200),
('T-Shirt', 'kCn','Comfortable cotton t-shirt', 14.99, '/uploads/products/tshirt.jpg', '100% Cotton, Sizes S-XL', 3, 300);

