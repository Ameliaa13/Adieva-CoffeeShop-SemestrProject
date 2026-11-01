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

/*
CREATE TABLE If NOT EXISTS reviews(
    id SERIAL PRIMARY KEY ,
    user_first_name VARCHAR(50) REFERENCES users(first_name) ON DELETE CASCADE ,
    user_last_name VARCHAR(50) REFERENCES users(last_name) ON DELETE CASCADE ,
    description TEXT
)*/

CREATE TABLE IF NOT EXISTS events(
    id              SERIAL PRIMARY KEY ,
    title           VARCHAR(50) NOT NULL ,
    description     TEXT,
    start_time      TIMESTAMP NOT NULL ,
    duration_minutes INTEGER,
    capacity        INTEGER NOT NULL DEFAULT 15,
    visibility      VARCHAR(10) NOT NULL DEFAULT 'OPEN',
    status          VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    image_url       VARCHAR(500),

    CHECK ( capacity>0 ),
    CHECK ( visibility IN ('OPEN', 'SECRET')),
    CHECK ( status in('PLANNED', 'CANCELLED', 'COMPLETED') )
);

INSERT INTO categories (name) VALUES ('Сезонные напитки'), ('Классика'),
                                     ('Авторские'),('Холодный кофе'),
                                     ('Не кофе'),('Десерты');

INSERT INTO menu_items(name, category_id, price, description, image_url) VALUES
                    ('Капучино' ,  2, 240.0, 'Вкусный классический капучино!', '/img/cappuccino.jpg');



INSERT INTO menu_items(name, category_id, price, description, image_url) VALUES
('Американо', 2, 165.0, 'Классический черный кофе с насыщенным ароматом, мягким вкусом и приятным продолжительным послевкусием. Идеальный выбор для тех, кто любит чистый вкус кофе без добавления молока.', '/img/Americano.jpg'),
('Эспрессо', 2, 109.0, 'Крепкий концентрированный кофе, приготовленный под высоким давлением. Обладает насыщенным вкусом, плотной текстурой и ярким кофейным ароматом — настоящая основа для истинных ценителей кофе.', '/img/espresso.jpg'),
('Латте', 2, 235.0, 'Нежный кофейный напиток с насыщенным эспрессо и большим количеством молока. Мягкий сливочный вкус и бархатистая текстура делают его идеальным выбором для тех, кто любит деликатный кофейный акцент.', '/img/latte.jpg'),
('Раф', 2, 159.0, 'Нежный кофейный напиток на основе эспрессо и сливок, взбитых до воздушной кремовой текстуры. Мягкий, насыщенный вкус с деликатной сладостью и бархатистым послевкусием.', '/img/raf.jpg'),
('Флэт уайт', 2, 205.0, 'Насыщенный кофейный напиток с двойной порцией эспрессо и тонким слоем шелковистой молочной микропены. Яркий вкус кофе, дополненный мягкой сливочной текстурой, для идеального баланса крепости и нежности.', '/img/flat white.jpg'),
('Апельсиновый эспрессо', 2, 185.0, 'Освежающий кофейный напиток, сочетающий крепкий эспрессо и яркий цитрусовый вкус апельсина. Бодрящий контраст сочной сладости и кофейной горчинки с приятным послевкусием.', '/img/orange espresso.jpg');

INSERT INTO menu_items(name, category_id, price, description, image_url) VALUES
('')


Select * from categories;