CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY ,
    email VARCHAR(50) UNIQUE NOT NULL ,
    password VARCHAR(256) NOT NULL,
    first_name VARCHAR(50) NOT NULL ,
    last_name VARCHAR(50) NOT NULL ,
    birth_date DATE NOT NULL ,
    role VARCHAR(30) NOT NULL DEFAULT 'USER'
);
SELECT * FROM users;

CREATE TABLE IF NOT EXISTS categories(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(100) UNIQUE NOT NULL ,
    category_id INT REFERENCES categories(id) ON DELETE CASCADE ,
    price NUMERIC(10,2) NOT NULL ,
    description TEXT,
    image_url VARCHAR(256)
);

INSERT INTO categories (name) VALUES ('Сезонные напитки'), ('Классика'),
                                     ('Авторские'),('Холодный кофе'),
                                     ('Не кофе'),('Десерты');

INSERT INTO menu_items(name, category_id, price, description, image_url) VALUES
                    ('Капучино' ,  2, 240.0, 'Вкусный классический капучино!', '/img/cappuccino.jpg')