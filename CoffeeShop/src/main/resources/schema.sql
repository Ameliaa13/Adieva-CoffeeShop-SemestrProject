CREATE TABLE IF NOT EXISTS users (
    id              SERIAL PRIMARY KEY ,
    email           VARCHAR(50) UNIQUE NOT NULL ,
    password        VARCHAR(256) NOT NULL,
    first_name      VARCHAR(50) NOT NULL ,
    last_name       VARCHAR(50) NOT NULL ,
    birth_date      DATE NOT NULL ,
    role            VARCHAR(30) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS categories(
    id              SERIAL PRIMARY KEY ,
    name            VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS menu_items (
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(100) UNIQUE NOT NULL,
    category_id      INT REFERENCES categories(id) ON DELETE CASCADE,
    price            NUMERIC(10, 2) NOT NULL,
    description      TEXT,
    image_url        VARCHAR(256),
    calories         NUMERIC(8, 2),
    proteins         NUMERIC(8, 2),
    fats             NUMERIC(8, 2),
    carbohydrates    NUMERIC(8, 2)
);

ALTER TABLE menu_items
    ADD COLUMN IF NOT EXISTS calories NUMERIC(8, 2),
    ADD COLUMN IF NOT EXISTS proteins NUMERIC(8, 2),
    ADD COLUMN IF NOT EXISTS fats NUMERIC(8, 2),
    ADD COLUMN IF NOT EXISTS carbohydrates NUMERIC(8, 2);

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
    user_id             INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE ,
    menu_item_id        INTEGER NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE ,
    quantity            INTEGER NOT NULL DEFAULT 1 CHECK ( quantity > 0 ),

    PRIMARY KEY (user_id, menu_item_id)
);

CREATE TABLE IF NOT EXISTS favorites(
    user_id             INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE ,
    menu_item_id        INTEGER NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE ,

    PRIMARY KEY (user_id, menu_item_id)
);


CREATE TABLE IF NOT EXISTS events(
    id                  SERIAL PRIMARY KEY ,
    title               VARCHAR(50) NOT NULL ,
    description         TEXT,
    start_time          TIMESTAMP NOT NULL ,
    duration_minutes    INTEGER,
    capacity            INTEGER NOT NULL DEFAULT 15,
    visibility          VARCHAR(10) NOT NULL DEFAULT 'OPEN',
    status              VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    image_url           VARCHAR(500),

    CHECK ( capacity>0 ),
    CHECK ( visibility IN ('OPEN', 'SECRET')),
    CHECK ( status in('PLANNED', 'CANCELLED', 'COMPLETED') )
);

ALTER TABLE events
    ADD COLUMN IF NOT EXISTS riddle_theme VARCHAR(250),
    ADD COLUMN IF NOT EXISTS answer_aliases TEXT;

CREATE TABLE IF NOT EXISTS event_riddles (
    id                  SERIAL PRIMARY KEY,
    event_id            INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    user_id             INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    riddle_text         TEXT NOT NULL,
    riddle_hash         CHAR(64) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,

    UNIQUE (event_id, user_id, riddle_hash)
);


CREATE UNIQUE INDEX IF NOT EXISTS uq_event_riddles_one_current
    ON event_riddles(event_id, user_id)
    WHERE is_current = TRUE;



CREATE TABLE IF NOT EXISTS event_registrations(
    event_id            INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE ,
    user_id             INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    registered_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (event_id, user_id)
);
CREATE TABLE IF NOT EXISTS secret_event_solutions (
     event_id           INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
     user_id            INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     solved_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     PRIMARY KEY (event_id, user_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id                  SERIAL PRIMARY KEY,
    user_id             INTEGER        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    total_price         NUMERIC(10, 2) NOT NULL CHECK (total_price >= 0),
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_orders_user_created
    ON orders (user_id, created_at DESC);

CREATE TABLE IF NOT EXISTS order_items (
  id                SERIAL PRIMARY KEY,
  order_id          INTEGER        NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  menu_item_id      INTEGER        REFERENCES menu_items(id) ON DELETE SET NULL,
  item_name         VARCHAR(100)   NOT NULL,
  unit_price        NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
  quantity          INTEGER        NOT NULL CHECK (quantity > 0));

CREATE INDEX IF NOT EXISTS idx_order_items_order
    ON order_items (order_id);


