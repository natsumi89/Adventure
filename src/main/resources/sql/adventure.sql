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
last_name VARCHAR(255)  NOT NULL,
first_name VARCHAR(255)  NOT NULL,
birth_date DATE NOT NULL,
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

CREATE TABLE shopping_carts(
    cart_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    product_id INTEGER REFERENCES products(product_id),
    quantity INTEGER NOT NULL,
    added_date TIMESTAMPTZ DEFAULT current_timestamp
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
        ('東京都', '日本の首都で、ビジネスと文化の中心です。'),
        ('大阪府', '関西の中心で、歴史と現代が融合しています。'),
        ('京都府', '古都であり、多くの歴史的な寺院や神社があります。'),
        ('福岡県', '九州の中心地で、美味しい食べ物が豊富です。'),
        ('沖縄県', '南の島で、美しい海と自然が魅力です。');


-- Producers Table
INSERT INTO producers (producer_name, description, contact_info)
VALUES
    ('田中農場', '新鮮な野菜と果物の生産を行っています。', 'tanaka@example.com'),
    ('佐藤漁業', '新鮮な魚と海産物の提供を行っています。', 'sato@example.com');

-- Users Table
INSERT INTO users (last_name,first_name,birth_date, email, password)
VALUES
    ('yamada','taro','1976-07-22', 'yamada_taro@example.com', 'password123'),
    ('suzuki','hanako','1992-09-19', 'suzuki_hanako@example.com', 'password123');

-- Products Table
INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
VALUES
    (1, 1, '北海道産じゃがいも', '新鮮で美味しいじゃがいもです。', 300, 'potato.png'),
    (2, 2, '青森産りんご', '甘くてジューシーなりんごです。', 500, 'apple.png');

 -- 東京都の商品
 INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
 VALUES
     (3, 1, '東京バナナ', '東京の有名なお土産で、甘くて美味しいです。', 1000, 'tokyo_banana.png'),
     (3, 2, '東京産ブリ', '新鮮で脂ののったブリです。', 1500, 'buri.png');

 -- 大阪府の商品
 INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
 VALUES
     (4, 1, 'たこ焼き', '大阪の代表的なストリートフードです。', 600, 'takoyaki.png'),
     (4, 2, 'ふぐ料理', '特別な日のごちそうとして人気があります。', 5000, 'fugu.png');

 -- 京都府の商品
 INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
 VALUES
     (5, 1, '抹茶スイーツ', '京都の伝統的な抹茶を使用したスイーツです。', 1200, 'matcha_sweets.png'),
     (5, 2, '納豆', '発酵させた大豆で、健康効果があります。', 200, 'natto.png');

 -- 福岡県の商品
 INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
 VALUES
     (6, 1, '明太子', '辛くて美味しい、福岡の名物です。', 1800, 'mentaiko.png'),
     (6, 2, 'とんこつラーメン', 'クリーミーなスープが特徴のラーメンです。', 800, 'ramen.png');

 -- 沖縄県の商品
 INSERT INTO products (region_id, producer_id, product_name, description, price, image_url)
 VALUES
     (7, 1, '沖縄そば', '沖縄独特のソウルフードで、麺が太くて美味しいです。', 700, 'okinawa_soba.png'),
     (7, 2, 'ゴーヤチャンプルー', 'ゴーヤの苦味が特徴的な沖縄料理です。', 650, 'goya.png');

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

