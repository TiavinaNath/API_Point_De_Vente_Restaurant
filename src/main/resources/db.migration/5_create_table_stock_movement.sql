do
$$
    begin
        if not exists(select from pg_type where typname = 'stock_movement_type') then
            create type stock_movement_type as enum ('IN', 'OUT');
        end if;
    end
$$;

create table if not exists stock_movement
(
    id                bigserial primary key,
    quantity          numeric,
    unit              unit,
    movement_type     stock_movement_type,
    creation_datetime timestamp without time zone,
    id_ingredient     bigint,
    constraint fk_stock_movement_id_ingredient foreign key (id_ingredient) references ingredient (id)
);

-- Vérifier la séquence associée au `bigserial`
SELECT pg_get_serial_sequence('stock_movement', 'id');

-- Ajuster la séquence pour reprendre après le dernier id existant
SELECT setval(pg_get_serial_sequence('stock_movement', 'id'), (SELECT max(id) FROM stock_movement));
