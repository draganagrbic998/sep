insert into currency (name) values ('USD');
insert into currency (name) values ('EUR');
insert into currency (name) values ('RSD');

insert into category (name) values ('CATEGORY 1');
insert into category (name) values ('CATEGORY 2');
insert into category (name) values ('CATEGORY 3');

-- password is 'asd'
insert into user_table (email, password, role)
values ('admin@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'ws-admin');
insert into user_table (email, password, role)
values ('user@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'merchant');
