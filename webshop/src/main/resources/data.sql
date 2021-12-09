insert into currency (name) values ('USD');
insert into currency (name) values ('EUR');
insert into currency (name) values ('RSD');

insert into category (name) values ('CATEGORY 1');
insert into category (name) values ('CATEGORY 2');
insert into category (name) values ('CATEGORY 3');

insert into user_table (email, password, role, api_key)
values ('merchant1@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'merchant', 'merchant_api_key_1');
insert into user_table (email, password, role, api_key)
values ('merchant2@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'merchant', 'merchant_api_key_2');
insert into user_table (email, password, role, api_key)
values ('merchant3@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'merchant', 'merchant_api_key_3');

insert into product (user_id, name, description, category, price, currency, image_location)
values (1, 'NAME 1', 'DESCRIPTION 1', 'CATEGORY 1', 1, 'RSD', 'IMAGE LOCATION 1');
