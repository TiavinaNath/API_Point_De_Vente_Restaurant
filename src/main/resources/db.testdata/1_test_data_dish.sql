insert into dish(id,name, price) values (1, 'Hot dog', '15000')
on conflict do nothing;


insert into dish(id,name, price) values
                                     (1, 'Hot dog', '15000'),
                                     (2, 'Omelette', '5000'),
                                     (3, 'Saucisse frit', '3500')