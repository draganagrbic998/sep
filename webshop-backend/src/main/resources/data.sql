insert into role (name) values ('admin');
insert into role (name) values ('seller');
insert into role (name) values ('customer');

insert into user_table (email, password)
values ('admin@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra');
insert into user_table (email, password)
values ('user@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra');

insert into user_role (user_id, role_id)
values (1, 1);
insert into user_role (user_id, role_id)
values (2, 2);
insert into user_role (user_id, role_id)
values (2, 3);
