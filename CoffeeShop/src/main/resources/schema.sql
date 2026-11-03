CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY ,
    email VARCHAR(50) UNIQUE NOT NULL ,
    password VARCHAR(256) NOT NULL,
    first_name VARCHAR(50) NOT NULL ,
    last_name VARCHAR(50) NOT NULL ,
    birth_date DATE NOT NULL ,
    role VARCHAR(30) NOT NULL DEFAULT 'USER'
);

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

ALTER TABLE menu_items
    ADD COLUMN calories NUMERIC(8, 2),
    ADD COLUMN proteins NUMERIC(8, 2),
    ADD COLUMN fats NUMERIC(8, 2),
    ADD COLUMN carbohydrates NUMERIC(8, 2);

CREATE TABLE IF NOT EXISTS promotions(
    id                  SERIAL PRIMARY KEY,
    title               VARCHAR(100) NOT NULL ,
    short_description   VARCHAR(250),
    description         TEXT NOT NULL ,
    image_url           VARCHAR(256),
    start_date          DATE NOT NULL,
    end_date            DATE NOT NULL,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

    CHECK (end_date >= start_date)
);

CREATE TABLE If NOT EXISTS cart_items(
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE ,
    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE ,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK ( quantity > 0 ),
    PRIMARY KEY (user_id, menu_item_id)
);

CREATE TABLE IF NOT EXISTS favorites(
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE ,
    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE ,
    PRIMARY KEY (user_id, menu_item_id)
);


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


Select * from categories;
Select * from menu_items;
