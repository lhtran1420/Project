CREATE TABLE product (
                         product_id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         category VARCHAR(255),
                         added_at DATETIME,
                         added_by VARCHAR(255)
);
CREATE TABLE product_price (
                               price_id INT AUTO_INCREMENT PRIMARY KEY,
                               product_id INT,
                               price DECIMAL(10, 2),
                               discount_percent DECIMAL(5, 2) DEFAULT 0,
                               updated_at DATETIME,
                               updated_by VARCHAR(255),
                               FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE TABLE product_price_change_log (
                                          change_id INT AUTO_INCREMENT PRIMARY KEY,
                                          product_id INT,
                                          old_price DECIMAL(10, 2),
                                          new_price DECIMAL(10, 2),
                                          old_discount_percent DECIMAL(5, 2),
                                          new_discount_percent DECIMAL(5, 2),
                                          operation VARCHAR(10),
                                          performed_at DATETIME,
                                          performed_by VARCHAR(255)
);
SELECT
    p.name AS product_name,
    p.category,
    pp.price,
    pp.updated_at AS price_updated_at,
    pp.updated_by AS price_updated_by
FROM
    product p
        JOIN
    product_price pp ON p.product_id = pp.product_id;
