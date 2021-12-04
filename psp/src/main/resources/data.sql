insert into role (name) values ('psp-admin');
insert into role (name) values ('ws-admin');
insert into role (name) values ('merchant');

-- password is 'asd'
insert into user_table (email, password, role)
values ('psp@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'psp-admin');
