CREATE TABLE users(
    id SERIAL PRIMARY KEY ,
    email VARCHAR(50) UNIQUE NOT NULL ,
    password VARCHAR(256) NOT NULL,
    first_name VARCHAR(50) NOT NULL ,
    last_name VARCHAR(50) NOT NULL ,
    birth_date DATE NOT NULL ,
    role VARCHAR(30) NOT NULL DEFAULT 'USER'
);