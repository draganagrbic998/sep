insert into role (name) values ('psp-admin');
insert into role (name) values ('ws-admin');
insert into role (name) values ('merchant');

-- password is 'asd'
insert into user_table (email, password)
values ('psp@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra');

insert into user_role (user_id, role_id) values (1, 1);

insert into payment_method (name) values ('card');
insert into payment_method (name) values ('qr');
insert into payment_method (name) values ('paypal');
insert into payment_method (name) values ('bitcoin');
