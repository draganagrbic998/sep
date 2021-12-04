insert into role (name) values ('psp-admin');
insert into role (name) values ('ws-admin');
insert into role (name) values ('merchant');

-- password is 'asd'
insert into user_table (email, password, role)
values ('psp@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'psp-admin');


insert into payment_method (name) values ('card');
insert into payment_method (name) values ('qr');
insert into payment_method (name) values ('paypal');
insert into payment_method (name) values ('bitcoin');


