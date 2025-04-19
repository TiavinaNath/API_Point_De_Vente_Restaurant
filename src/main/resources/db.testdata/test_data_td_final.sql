insert into dish(id,name, price) values
                                     (1, 'Hot dog', '15000'),
                                     (2, 'Omelette', '5000'),
                                     (3, 'Saucisse frit', '3500');


insert into ingredient(id, name)
values (1, 'Saucisse'),
       (2, 'Huile'),
       (3, 'Oeuf'),
       (4, 'Pain')
    on conflict do nothing;

insert into dish_ingredient(id, id_dish, id_ingredient, required_quantity, unit)
values (1, 1, 1, 100, 'G'),
       (2, 1, 2, 0.15, 'L'),
       (3, 1, 3, 1, 'U'),
       (4, 1, 4, 1, 'U')
    on conflict do nothing ;

insert into dish_ingredient(id, id_dish, id_ingredient, required_quantity, unit)
values (5, 2, 2, 0.15, 'L'),
       (6, 2, 3, 3, 'U')
    on conflict do nothing ;

insert into dish_ingredient(id, id_dish, id_ingredient, required_quantity, unit)
values (7, 3, 2, 0.2, 'L'),
       (8, 3, 1, 200, 'G')
    on conflict do nothing ;

SELECT setval(pg_get_serial_sequence('ingredient', 'id'), (SELECT max(id) FROM ingredient));
SELECT setval(pg_get_serial_sequence('dish_ingredient', 'id'), (SELECT max(id) FROM dish_ingredient));
SELECT setval(pg_get_serial_sequence('dish', 'id'), (SELECT max(id) FROM dish));
SELECT setval(pg_get_serial_sequence('price', 'id'), (SELECT max(id) FROM price));
SELECT setval(pg_get_serial_sequence('stock_movement', 'id'), (SELECT max(id) FROM stock_movement));

-- CMD-001 - Hot Dog
UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 08:00:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-001' AND d.name = 'Hot Dog'
)
  AND status = 'IN_PROGRESS';

UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 08:10:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-001' AND d.name = 'Hot Dog'
)
  AND status = 'FINISHED';

-- CMD-001 - Saucisse frit
UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 08:00:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-001' AND d.name = 'Saucisse frit'
)
  AND status = 'IN_PROGRESS';

UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 08:05:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-001' AND d.name = 'Saucisse frit'
)
  AND status = 'FINISHED';

-- CMD-002 - Omelette
UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 09:00:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-002' AND d.name = 'Omelette'
)
  AND status = 'IN_PROGRESS';

UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 09:10:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-002' AND d.name = 'Omelette'
)
  AND status = 'FINISHED';

-- CMD-003 - Hot Dog
UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 10:00:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-003' AND d.name = 'Hot Dog'
)
  AND status = 'IN_PROGRESS';

UPDATE dish_order_status_history
SET creation_date_time = '2025-04-19 10:08:00'
WHERE id_dish_order IN (
    SELECT dio.id
    FROM dish_order dio
             JOIN "order" o ON o.id = dio.id_order
             JOIN dish d ON d.id = dio.id_dish
    WHERE o.reference = 'CMD-003' AND d.name = 'Hot Dog'
)
  AND status = 'FINISHED';

