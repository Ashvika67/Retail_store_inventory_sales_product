CREATE TABLE category (
 category_id SERIAL PRIMARY KEY,
 category_name VARCHAR(60) NOT NULL UNIQUE
);
CREATE TABLE product (
 product_id SERIAL PRIMARY KEY,
 product_name VARCHAR(100) NOT NULL,
 category_id INT NOT NULL REFERENCES category(category_id),
 price NUMERIC(10,2) NOT NULL CHECK(price > 0),
 stock_quantity INT NOT NULL DEFAULT 0 CHECK(stock_quantity >= 0),
 reorder_level INT NOT NULL DEFAULT 5 CHECK(reorder_level >= 0)
);
CREATE TABLE sale (
 sale_id SERIAL PRIMARY KEY,
 sale_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE sale_item (
 sale_item_id SERIAL PRIMARY KEY,
 sale_id INT NOT NULL REFERENCES sale(sale_id) ON DELETE CASCADE,
 product_id INT NOT NULL REFERENCES product(product_id),
 quantity INT NOT NULL CHECK(quantity > 0),
 unit_price NUMERIC(10,2) NOT NULL CHECK(unit_price > 0)
);

INSERT INTO category(category_name) VALUES
('Grocery'),('Stationery'),('Household')
ON CONFLICT(category_name) DO NOTHING;

INSERT INTO product(product_name,category_id,price,stock_quantity,reorder_level)
SELECT 'Rice 1 kg',category_id,65,40,8 FROM category WHERE category_name='Grocery';
INSERT INTO product(product_name,category_id,price,stock_quantity,reorder_level)
SELECT 'Notebook',category_id,45,25,5 FROM category WHERE category_name='Stationery';
INSERT INTO product(product_name,category_id,price,stock_quantity,reorder_level)
SELECT 'Bath Soap',category_id,35,4,5 FROM category WHERE category_name='Household';

-- All products
SELECT p.product_id,p.product_name,c.category_name,p.price,p.stock_quantity
FROM product p JOIN category c ON p.category_id=c.category_id;

-- Low stock
SELECT product_id,product_name,stock_quantity,reorder_level
FROM product WHERE stock_quantity <= reorder_level;

-- Daily sales
SELECT DATE(s.sale_date) AS sales_day,
 SUM(si.quantity*si.unit_price) AS total_sales
FROM sale s JOIN sale_item si ON s.sale_id=si.sale_id
GROUP BY DATE(s.sale_date) ORDER BY sales_day;

-- Best-selling products
SELECT p.product_name,SUM(si.quantity) AS quantity_sold
FROM sale_item si JOIN product p ON si.product_id=p.product_id
GROUP BY p.product_id,p.product_name ORDER BY quantity_sold DESC;

-- Normalization:
-- 1NF: Atomic fields and primary keys.
-- 2NF: Sale header and sale line data are separated; non-key values depend on the key.
-- 3NF: Category name is stored once in CATEGORY; PRODUCT stores only category_id.
