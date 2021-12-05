insert into currency (name) values ('USD');
insert into currency (name) values ('EUR');
insert into currency (name) values ('RSD');

insert into category (name) values ('CATEGORY 1');
insert into category (name) values ('CATEGORY 2');
insert into category (name) values ('CATEGORY 3');

-- password is 'asd'
--insert into user_table (email, password, role)
--values ('admin@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'ws-admin');
--insert into user_table (email, password, role)
--values ('user@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'merchant');

insert into user_table (email, password, role, api_key)
values ('test@gmail.com', '$2a$10$LGMypZ0/SdnoRotrQXYAweCPhCymDjA2vqoGc3D75RtqXVE44cuVW', 'merchant', 'jsJpiP7BGETc8ZBgWkEei6aAJMZilDf1la3hSdhQ6qXwl5q9vZMvrQEkCj1XsgzT');
