-- START OF ROLES
CREATE SEQUENCE IF NOT EXISTS roles_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE roles
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);
-- END OF ROLES

-- START OF ACCOUNT
CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 50;

CREATE TABLE account
(
    id       BIGINT NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    active   BOOLEAN,
    roles_id BIGINT,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_ROLES FOREIGN KEY (roles_id) REFERENCES roles (id);
-- END OF ACCOUNT

-- START OF CUSTOMER
CREATE SEQUENCE IF NOT EXISTS customer_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE customer
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    updated    TIMESTAMP WITH TIME ZONE,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    account_id BIGINT,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);
-- END OF CUSTOMER

-- START OF ADDRESS
CREATE SEQUENCE IF NOT EXISTS address_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE address
(
    id              BIGINT  NOT NULL,
    created         TIMESTAMP WITH TIME ZONE,
    updated         TIMESTAMP WITH TIME ZONE,
    street          VARCHAR(255),
    city            VARCHAR(255),
    province        VARCHAR(255),
    building_number VARCHAR(255),
    is_main         BOOLEAN NOT NULL,
    customer_id     BIGINT,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);
-- END OF ADDRESS

-- START OF CUSTOMER_ALLERGEN
CREATE SEQUENCE IF NOT EXISTS customer_allergen_sequence START WITH 1 INCREMENT BY 1;

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
-- END OF CUSTOMER_ALLERGEN

-- START OF DISH
CREATE SEQUENCE IF NOT EXISTS dish_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE dish
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP WITH TIME ZONE,
    updated     TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(255),
    description VARCHAR(255),
    image       BYTEA,
    CONSTRAINT pk_dish PRIMARY KEY (id)
);
-- END OF DISH

-- START OF PORTION
CREATE SEQUENCE IF NOT EXISTS portion_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE portion
(
    id      BIGINT NOT NULL,
    created TIMESTAMP WITH TIME ZONE,
    updated TIMESTAMP WITH TIME ZONE,
    name    VARCHAR(255),
    CONSTRAINT pk_portion PRIMARY KEY (id)
);

ALTER TABLE portion
    ADD CONSTRAINT uc_portion_name UNIQUE (name);
-- END OF PORTION

-- START OF DISH_PORTION
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
-- END OF DISH_PORTION

-- START OF REVIEW
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
-- END OF REVIEW

-- START OF CART
CREATE SEQUENCE IF NOT EXISTS cart_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE cart
(
    id          BIGINT NOT NULL,
    status      VARCHAR(255),
    customer_id BIGINT,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);
-- END OF CART

-- START OF DISH_PORTION_CART
CREATE SEQUENCE IF NOT EXISTS dish_portion_cart_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE dish_portion_cart
(
    id              BIGINT NOT NULL,
    quantity        INTEGER,
    cart_id         BIGINT,
    dish_portion_id BIGINT,
    CONSTRAINT pk_dish_portion_cart PRIMARY KEY (id)
);

ALTER TABLE dish_portion_cart
    ADD CONSTRAINT FK_DISH_PORTION_CART_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE dish_portion_cart
    ADD CONSTRAINT FK_DISH_PORTION_CART_ON_DISHPORTION FOREIGN KEY (dish_portion_id) REFERENCES dish_portion (id);
-- END OF DISH_PORTION_CART

-- START OF FOOD_ORDER
CREATE SEQUENCE IF NOT EXISTS food_order_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE food_order
(
    id      BIGINT NOT NULL,
    notes   VARCHAR(255),
    status  VARCHAR(255),
    total   DOUBLE PRECISION,
    cart_id BIGINT,
    date    TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_food_order PRIMARY KEY (id)
);

ALTER TABLE food_order
    ADD CONSTRAINT uc_food_order_cart UNIQUE (cart_id);

ALTER TABLE food_order
    ADD CONSTRAINT FK_FOOD_ORDER_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);
-- END OF FOOD_ORDER

-- START OF EMPLOYEE
CREATE SEQUENCE IF NOT EXISTS employee_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE employee
(
    id         BIGINT NOT NULL,
    created    TIMESTAMP WITH TIME ZONE,
    updated    TIMESTAMP WITH TIME ZONE,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    account_id BIGINT,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);
-- END OF EMPLOYEE