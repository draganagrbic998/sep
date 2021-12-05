insert into role (name) values ('psp-admin');
insert into role (name) values ('ws-admin');
insert into role (name) values ('merchant');

-- password is 'asd'
insert into user_table (email, password, role)
values ('psp@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'psp-admin');


insert into user_table (email, password, role, api_key, webshop, webshop_id)
values ('test@gmail.com', '$2a$10$LGMypZ0/SdnoRotrQXYAweCPhCymDjA2vqoGc3D75RtqXVE44cuVW', 'merchant', 'jsJpiP7BGETc8ZBgWkEei6aAJMZilDf1la3hSdhQ6qXwl5q9vZMvrQEkCj1XsgzT', 'https://localhost:8080', 1);

