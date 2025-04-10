
CREATE TYPE status_order AS ENUM (
    'CREATED',
    'CONFIRMED',
    'IN_PROGRESS',
    'FINISHED',
    'DELIVERED'
);

CREATE TYPE status_dish_order AS ENUM (
    'CREATED',
    'CONFIRMED',
    'IN_PROGRESS',
    'FINISHED',
    'DELIVERED'
);

CREATE TABLE "order" (
    id BIGSERIAL PRIMARY KEY,
    reference VARCHAR(255) NOT NULL,
    creation_date_time TIMESTAMP NOT NULL
);

CREATE TABLE dish_order (
    id BIGSERIAL PRIMARY KEY,
    id_order BIGINT NOT NULL REFERENCES "order"(id) ON DELETE CASCADE,
    id_dish BIGINT NOT NULL REFERENCES dish(id),
    quantity numeric NOT NULL
);

CREATE TABLE dish_order_status_history (
     id BIGSERIAL PRIMARY KEY,
     id_dish_order BIGINT NOT NULL REFERENCES dish_order(id) ON DELETE CASCADE,
     status status_dish_order NOT NULL,
     creation_date_time TIMESTAMP NOT NULL
);

CREATE TABLE order_status_history (
     id BIGSERIAL PRIMARY KEY,
     id_order BIGINT NOT NULL REFERENCES "order"(id) ON DELETE CASCADE,
     status status_order NOT NULL,
     creation_date_time TIMESTAMP NOT NULL
);
