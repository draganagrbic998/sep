insert into role (name) values ('psp-admin');
insert into role (name) values ('ws-admin');
insert into role (name) values ('merchant');

insert into payment_method (name) values ('card');
insert into payment_method (name) values ('qr');
insert into payment_method (name) values ('paypal');
insert into payment_method (name) values ('bitcoin');

insert into user_table (email, password, role)
values ('psp@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'psp-admin');
insert into user_table (email, password, role, api_key, webshop, webshop_id)
values ('merchant@gmail.com', '$2a$10$LGMypZ0/SdnoRotrQXYAweCPhCymDjA2vqoGc3D75RtqXVE44cuVW', 'merchant', 'merchant_api_key_1', 'https://localhost:8080', 1);

insert into user_table_methods (user_id, methods_id) values (2, 1);
insert into user_table_methods (user_id, methods_id) values (2, 2);
insert into user_table_methods (user_id, methods_id) values (2, 3);
insert into user_table_methods (user_id, methods_id) values (2, 4);

