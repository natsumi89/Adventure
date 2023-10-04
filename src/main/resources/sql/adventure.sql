CREATE TABLE regions(
region_id SERIAL PRIMARY KEY,
region_name VARCHAR(255) UNIQUE NOT NULL,
description TEXT NOT NULL
);

CREATE TABLE producers(
producer_id SERIAL PRIMARY KEY,
producer_name VARCHAR(255) UNIQUE NOT NULL,
description TEXT NOT NULL,
contact_info TEXT NOT NULL
);
CREATE TABLE users(
user_id SERIAL PRIMARY KEY,
username VARCHAR(255) UNIQUE NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL,
created_at TIMESTAMPTZ DEFAULT current_timestamp,
updated_at TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE TABLE products(
product_id SERIAL PRIMARY KEY,
region_id INTEGER REFERENCES regions(region_id),
producer_id INTEGER REFERENCES producers(producer_id),
product_name VARCHAR(255) NOT NULL,
description TEXT NOT NULL,
price INTEGER NOT NULL,
image_url TEXT NOT NULL,
created_at TIMESTAMPTZ DEFAULT current_timestamp,
updated_at TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE TABLE orders(
order_id SERIAL PRIMARY KEY,
user_id INTEGER REFERENCES users(user_id),
total_price INTEGER NOT NULL,
order_date TIMESTAMPTZ DEFAULT current_timestamp,
status VARCHAR(50) CHECK (status IN ('processing', 'shipped', 'delivered'))
);

CREATE TABLE order_details(
order_detail_id SERIAL PRIMARY KEY,
order_id INTEGER REFERENCES orders(order_id),
product_id INTEGER REFERENCES products(product_id),
quantity INTEGER NOT NULL,
subtotal_price INTEGER NOT NULL
);

CREATE TABLE stamps(
stamp_id SERIAL PRIMARY KEY,
user_id INTEGER REFERENCES users(user_id),
region_id INTEGER REFERENCES regions(region_id),
stamp_date TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE TABLE events(
event_id SERIAL PRIMARY KEY,
region_id INTEGER REFERENCES regions(region_id),
event_name VARCHAR(255) NOT NULL,
description TEXT NOT NULL,
event_date TIMESTAMPTZ DEFAULT current_timestamp,
event_location TEXT NOT NULL
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
NEW.updated_at = now();
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_products_updated_at
BEFORE UPDATE ON products
FOR EACH ROW
WHEN (OLD.* IS DISTINCT FROM NEW.*)
EXECUTE FUNCTION update_updated_at_column();

------------------------------
-- Regions Table
INSERT INTO regions (region_name, description)
VALUES
    ('北海道', '日本の最北の地域で、自然が豊かです。'),
    ('青森県', 'りんごの生産が特に有名な地域です。');

-- Producers Table
INSERT INTO producers (producer_name, description, contact_info)
VALUES
    ('田中農場', '新鮮な野菜と果物の生産を行っています。', 'tanaka@example.com'),
    ('佐藤漁業', '新鮮な魚と海産物の提供を行っています。', 'sato@example.com');

-- Users Table
INSERT INTO users (username, email, password)
VALUES
    ('yamada_taro', 'yamada_taro@example.com', 'password123'),
    ('suzuki_hanako', 'suzuki_hanako@example.com', 'password123');

-- Products Table
INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
VALUES
    (1, 1, '北海道産じゃがいも', '新鮮で美味しいじゃがいもです。', 300, '/static/img/potato.png'),
    (2, 2, '青森産りんご', '甘くてジューシーなりんごです。', 500, '/static/img/apple.png');

-- Orders Table
INSERT INTO orders (user_id, total_price, status)
VALUES
    (1, 1000, 'processing'),
    (2, 1500, 'shipped');

-- Order Details Table
INSERT INTO order_details (order_id, product_id, quantity, subtotal_price)
VALUES
    (1, 1, 2, 600),
    (2, 2, 3, 1500);

-- Stamps Table
INSERT INTO stamps (user_id, region_id, stamp_date)
VALUES
    (1, 1, now()),
    (2, 2, now());

-- Events Table
INSERT INTO events (region_id, event_name, description, event_location)
VALUES
    (1, '北海道食材フェア', '北海道の新鮮な食材を堪能できるイベントです。', 'Sapporo Dome'),
    (2, '青森りんご祭り', '青森のおいしいりんごを味わえるイベントです。', 'Aomori City Hall');

