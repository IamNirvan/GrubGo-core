CREATE SEQUENCE IF NOT EXISTS address_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE address
(
    id              BIGINT NOT NULL,
    created         TIMESTAMP WITH TIME ZONE,
    updated         TIMESTAMP WITH TIME ZONE,
    street          VARCHAR(255),
    city            VARCHAR(255),
    province        VARCHAR(255),
    building_number VARCHAR(255),
    customer_id     BIGINT,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

CREATE SEQUENCE IF NOT EXISTS cart_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE cart
(
    id          BIGINT NOT NULL,
    customer_id BIGINT,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT uc_cart_customer UNIQUE (customer_id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

CREATE TABLE cart_dish
(
    cart_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    CONSTRAINT pk_cart_dish PRIMARY KEY (cart_id, dish_id)
);

ALTER TABLE cart_dish
    ADD CONSTRAINT fk_cart_dish_on_cart FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_dish
    ADD CONSTRAINT fk_cart_dish_on_dish FOREIGN KEY (dish_id) REFERENCES dish (id);

CREATE SEQUENCE IF NOT EXISTS category_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE category
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    updated    TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    name       VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS customer_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE customer
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    updated    TIMESTAMP WITH TIME ZONE,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    username   VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS customer_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE customer_allergen
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP WITH TIME ZONE,
    updated     TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(255),
    customer_id BIGINT,
    CONSTRAINT pk_customer_allergen PRIMARY KEY (id)
);

ALTER TABLE customer_allergen
    ADD CONSTRAINT FK_CUSTOMER_ALLERGEN_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

CREATE SEQUENCE IF NOT EXISTS dish_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE dish
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP WITH TIME ZONE,
    created_by  VARCHAR(255),
    updated     TIMESTAMP WITH TIME ZONE,
    updated_by  VARCHAR(255),
    name        VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT pk_dish PRIMARY KEY (id)
);

CREATE TABLE dish_category
(
    category_id BIGINT NOT NULL,
    dish_id     BIGINT NOT NULL,
    CONSTRAINT pk_dish_category PRIMARY KEY (category_id, dish_id)
);

ALTER TABLE dish_category
    ADD CONSTRAINT fk_discat_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE dish_category
    ADD CONSTRAINT fk_discat_on_dish FOREIGN KEY (dish_id) REFERENCES dish (id)

CREATE SEQUENCE IF NOT EXISTS dish_image_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE dish_image
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    updated    TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    name       VARCHAR(255),
    file_path  VARCHAR(255),
    dish_id    BIGINT,
    CONSTRAINT pk_dish_image PRIMARY KEY (id)
);

ALTER TABLE dish_image
    ADD CONSTRAINT FK_DISH_IMAGE_ON_DISH FOREIGN KEY (dish_id) REFERENCES dish (id);

CREATE SEQUENCE IF NOT EXISTS employee_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE employee
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP WITH TIME ZONE,
    updated     TIMESTAMP WITH TIME ZONE,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    designation SMALLINT,
    username    VARCHAR(255),
    password    VARCHAR(255),
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE order_dish
(
    dish_id  BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT pk_order_dish PRIMARY KEY (dish_id, order_id)
);

ALTER TABLE order_dish
    ADD CONSTRAINT fk_orddis_on_dish FOREIGN KEY (dish_id) REFERENCES dish (id);

ALTER TABLE order_dish
    ADD CONSTRAINT fk_orddis_on_order FOREIGN KEY (order_id) REFERENCES "order" (id);

CREATE TABLE order_portion
(
    order_id   BIGINT NOT NULL,
    portion_id BIGINT NOT NULL,
    CONSTRAINT pk_order_portion PRIMARY KEY (order_id, portion_id)
);

ALTER TABLE order_portion
    ADD CONSTRAINT fk_ordpor_on_order FOREIGN KEY (order_id) REFERENCES "order" (id);

ALTER TABLE order_portion
    ADD CONSTRAINT fk_ordpor_on_portion FOREIGN KEY (portion_id) REFERENCES portion (id);

CREATE SEQUENCE IF NOT EXISTS portion_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE portion
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    updated    TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    name       VARCHAR(255),
    CONSTRAINT pk_portion PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS review_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE review
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP WITH TIME ZONE,
    updated     TIMESTAMP WITH TIME ZONE,
    title       VARCHAR(255),
    content     VARCHAR(255),
    rating      INTEGER,
    dish_id     BIGINT,
    customer_id BIGINT,
    CONSTRAINT pk_review PRIMARY KEY (id)
);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_DISH FOREIGN KEY (dish_id) REFERENCES dish (id);

CREATE SEQUENCE IF NOT EXISTS dish_portion_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE dish_portion
(
    id         BIGINT NOT NULL,
    price      DOUBLE PRECISION,
    dish_id    BIGINT,
    portion_id BIGINT,
    CONSTRAINT pk_dish_portion PRIMARY KEY (id)
);

ALTER TABLE dish_portion
    ADD CONSTRAINT FK_DISH_PORTION_ON_DISH FOREIGN KEY (dish_id) REFERENCES dish (id);

ALTER TABLE dish_portion
    ADD CONSTRAINT FK_DISH_PORTION_ON_PORTION FOREIGN KEY (portion_id) REFERENCES portion (id);

CREATE SEQUENCE IF NOT EXISTS food_order_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE food_order
(
    id          BIGINT NOT NULL,
    notes       VARCHAR(255),
    quantity    INTEGER,
    status      SMALLINT,
    total       DOUBLE PRECISION,
    customer_id BIGINT,
    date        TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_food_order PRIMARY KEY (id)
);

ALTER TABLE food_order
    ADD CONSTRAINT FK_FOOD_ORDER_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);