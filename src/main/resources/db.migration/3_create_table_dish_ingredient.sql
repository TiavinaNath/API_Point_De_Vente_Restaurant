do
$$
    begin
        if not exists(select from pg_type where typname = 'unit') then
            create type unit as enum ('G', 'L', 'U');
        end if;
    end
$$;

create table if not exists dish_ingredient
(
    id                bigserial primary key,
    id_dish           bigint,
    id_ingredient     bigint,
    required_quantity numeric,
    unit unit,
    constraint unique_dish_ingredient_quantity unique (id_dish, id_ingredient, unit),
    constraint fk_dish_to_dish_ingredient foreign key (id_dish) references dish (id),
    constraint fk_ingredient_to_dish_ingredient foreign key (id_ingredient) references ingredient (id)
);

SELECT setval(pg_get_serial_sequence('ingredient', 'id'), (SELECT max(id) FROM ingredient));
SELECT setval(pg_get_serial_sequence('dish_ingredient', 'id'), (SELECT max(id) FROM dish_ingredient));
SELECT setval(pg_get_serial_sequence('dish', 'id'), (SELECT max(id) FROM dish));
SELECT setval(pg_get_serial_sequence('price', 'id'), (SELECT max(id) FROM price));
SELECT setval(pg_get_serial_sequence('stock_movement', 'id'), (SELECT max(id) FROM stock_movement));
