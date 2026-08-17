-- =============================================
-- IMS SEED DATA
-- INSERT IGNORE = skip if duplicate (safe to re-run)
-- =============================================

-- ── USERS ──
-- Passwords are BCrypt hashes of "demo"
-- You can generate these at: https://bcrypt-generator.com/
-- $2a$10$... = BCrypt hash with cost factor 10
--INSERT IGNORE INTO users (username, name, password, role) VALUES
--//('admin', 'Vaibhav Bisht', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');

-- ── CATEGORIES ──
INSERT IGNORE INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Furniture', 'Office and home furniture'),
('Clothing', 'Apparel and accessories'),
('Food & Beverages', 'Consumable products'),
('Stationery', 'Office supplies and stationery');

-- ── UNITS ──
INSERT IGNORE INTO units (name, abbreviation) VALUES
('Piece', 'pc'),
('Kilogram', 'kg'),
('Liter', 'L'),
('Box', 'box'),
('Meter', 'm');

-- ── WAREHOUSES ──
INSERT IGNORE INTO warehouses (name, location, capacity, active) VALUES
('Main Warehouse', 'Mumbai, Maharashtra', 1000, true),
('North Hub', 'Delhi, NCR', 500, true),
('South Center', 'Bangalore, Karnataka', 750, true);

-- ── SUPPLIERS ──
INSERT IGNORE INTO suppliers (name, contact_person, email, phone, address, active) VALUES
('TechCorp Supplies', 'Amit Kumar', 'amit@techcorp.com', '9876543210', 'Noida, UP', true),
('FurnishPro', 'Sanjay Gupta', 'sanjay@furnishpro.com', '9876543211', 'Pune, MH', true),
('GreenFoods', 'Meera Patel', 'meera@greenfoods.com', '9876543212', 'Ahmedabad, GJ', true),
('StyleWear', 'Kavita Reddy', 'kavita@stylewear.com', '9876543213', 'Hyderabad, TS', true),
('OfficeHub', 'Ravi Verma', 'ravi@officehub.com', '9876543214', 'Jaipur, RJ', true);

-- ── CUSTOMERS ──
INSERT IGNORE INTO customers (name, contact_person, email, phone, address, active) VALUES
('Acme Corp', 'Vikram Singh', 'vikram@acmecorp.com', '8765432100', 'Mumbai, MH', true),
('GlobalTech', 'Neha Agarwal', 'neha@globaltech.com', '8765432101', 'Gurgaon, HR', true),
('RetailMax', 'Karan Mehta', 'karan@retailmax.com', '8765432102', 'Chennai, TN', true),
('BuildRight', 'Deepak Joshi', 'deepak@buildright.com', '8765432103', 'Kolkata, WB', true),
('QuickMart', 'Anita Desai', 'anita@quickmart.com', '8765432104', 'Lucknow, UP', true);

-- ── PRODUCTS ──
INSERT IGNORE INTO products (sku, name, description, price, category_id, unit_id, active) VALUES
('ELEC-001', 'Laptop Pro 15"', 'High performance laptop with 16GB RAM', 999.99, 1, 1, true),
('ELEC-002', 'Wireless Mouse', 'Ergonomic wireless mouse', 29.99, 1, 1, true),
('ELEC-003', 'USB-C Hub', '7-in-1 USB-C multiport adapter', 49.99, 1, 1, true),
('ELEC-004', 'Mechanical Keyboard', 'RGB mechanical keyboard', 79.99, 1, 1, true),
('FURN-001', 'Office Chair', 'Ergonomic office chair with lumbar support', 299.99, 2, 1, true),
('FURN-002', 'Standing Desk', 'Adjustable height standing desk', 499.99, 2, 1, true),
('CLTH-001', 'Polo T-Shirt', 'Cotton polo t-shirt', 24.99, 3, 1, true),
('CLTH-002', 'Formal Shirt', 'Slim fit formal shirt', 39.99, 3, 1, true),
('FOOD-001', 'Green Tea Pack', 'Organic green tea - 100 bags', 12.99, 4, 4, true),
('FOOD-002', 'Coffee Beans', 'Premium Arabica coffee beans 1kg', 18.99, 4, 2, true),
('STAT-001', 'Notebook Set', 'Set of 5 ruled notebooks', 9.99, 5, 4, true),
('STAT-002', 'Pen Box', 'Box of 20 ballpoint pens', 7.99, 5, 4, true);

-- ── STOCK LEVELS ──
INSERT IGNORE INTO stock_levels (product_id, warehouse_id, quantity, min_stock, max_stock) VALUES
(1, 1, 50, 10, 200),
(1, 2, 30, 5, 100),
(2, 1, 200, 50, 500),
(3, 1, 150, 30, 300),
(4, 1, 75, 20, 200),
(5, 1, 25, 5, 50),
(5, 3, 15, 5, 40),
(6, 1, 10, 3, 30),
(7, 2, 300, 100, 1000),
(8, 2, 150, 50, 500),
(9, 3, 500, 100, 2000),
(10, 3, 200, 50, 500),
(11, 1, 400, 100, 1000),
(12, 1, 250, 50, 500);

-- ── PURCHASE ORDERS ──
INSERT IGNORE INTO purchase_orders (order_number, supplier_id, warehouse_id, product_id, quantity, order_date, expected_date, status, total_amount, notes) VALUES
('PO-001', 1, 1, 1, 100, '2026-07-15', '2026-07-25', 'RECEIVED', 49999.50, '100 Laptops for Q3'),
('PO-002', 2, 1, 5, 30, '2026-07-20', '2026-08-01', 'SHIPPED', 14999.50, 'Office furniture order'),
('PO-003', 3, 3, 9, 500, '2026-07-28', '2026-08-10', 'PENDING', 6500.00, 'Monthly food supplies'),
('PO-004', 1, 2, 2, 50, '2026-08-01', '2026-08-15', 'APPROVED', 3999.50, 'Peripherals restock'),
('PO-005', 4, 2, 7, 200, '2026-08-03', '2026-08-20', 'PENDING', 8750.00, 'Clothing inventory');

UPDATE purchase_orders SET product_id = 1, quantity = 100 WHERE order_number = 'PO-001' AND product_id IS NULL;
UPDATE purchase_orders SET product_id = 5, quantity = 30 WHERE order_number = 'PO-002' AND product_id IS NULL;
UPDATE purchase_orders SET product_id = 9, quantity = 500 WHERE order_number = 'PO-003' AND product_id IS NULL;
UPDATE purchase_orders SET product_id = 2, quantity = 50 WHERE order_number = 'PO-004' AND product_id IS NULL;
UPDATE purchase_orders SET product_id = 7, quantity = 200 WHERE order_number = 'PO-005' AND product_id IS NULL;

-- ── SALES ORDERS ──
INSERT IGNORE INTO sales_orders (order_number, customer_id, warehouse_id, product_id, quantity, order_date, delivery_date, status, total_amount, notes) VALUES
('SO-001', 1, 1, 1, 20, '2026-07-18', '2026-07-22', 'DELIVERED', 19999.80, '20 Laptops for Acme Corp'),
('SO-002', 2, 1, 2, 50, '2026-07-25', '2026-08-01', 'SHIPPED', 2399.20, 'Office setup - GlobalTech'),
('SO-003', 3, 2, 7, 100, '2026-07-30', NULL, 'CONFIRMED', 7499.70, 'Clothing order - RetailMax'),
('SO-004', 4, 1, 11, 50, '2026-08-02', NULL, 'PENDING', 1599.60, 'Stationery supplies'),
('SO-005', 5, 3, 10, 30, '2026-08-05', NULL, 'PENDING', 3249.75, 'Mixed order - QuickMart');

UPDATE sales_orders SET product_id = 1, quantity = 20 WHERE order_number = 'SO-001' AND product_id IS NULL;
UPDATE sales_orders SET product_id = 2, quantity = 50 WHERE order_number = 'SO-002' AND product_id IS NULL;
UPDATE sales_orders SET product_id = 7, quantity = 100 WHERE order_number = 'SO-003' AND product_id IS NULL;
UPDATE sales_orders SET product_id = 11, quantity = 50 WHERE order_number = 'SO-004' AND product_id IS NULL;
UPDATE sales_orders SET product_id = 10, quantity = 30 WHERE order_number = 'SO-005' AND product_id IS NULL;

-- ── STOCK MOVEMENTS ──
INSERT IGNORE INTO stock_movements (product_id, warehouse_id, type, quantity, reference, notes, created_at) VALUES
(1, 1, 'IN', 100, 'PO-001', 'Initial stock from TechCorp', '2026-07-15 10:00:00'),
(1, 1, 'OUT', -20, 'SO-001', 'Sold to Acme Corp', '2026-07-18 14:00:00'),
(2, 1, 'IN', 250, 'PO-001', 'Mice from TechCorp', '2026-07-15 10:30:00'),
(2, 1, 'OUT', -50, 'SO-002', 'Sold to GlobalTech', '2026-07-25 11:00:00'),
(5, 1, 'IN', 30, 'PO-002', 'Chairs from FurnishPro', '2026-07-22 09:00:00'),
(5, 1, 'ADJUSTMENT', -5, NULL, 'Damaged in transit', '2026-07-23 16:00:00'),
(9, 3, 'IN', 500, 'PO-003', 'Tea from GreenFoods', '2026-07-28 08:00:00');