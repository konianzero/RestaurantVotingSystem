DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
    ('Mike', 'admin@gmail.com', '{noop}admin'),
    ('Nick', 'user@gmail.com', '{noop}userpass');

INSERT INTO user_roles (user_id, role) VALUES
(100000, 'ADMIN'),
(100000, 'USER'),
(100001, 'USER');

INSERT INTO restaurants (name) VALUES
    ('Restaurant1'),
    ('Restaurant2');

INSERT INTO dishes (restaurant_id, name, price, date) VALUES
    (100002, 'Salad', 5, '2020-12-20'),
    (100002, 'Water', 1, '2020-12-20'),
    (100003, 'Burger', 40, '2020-12-20'),
    (100003, 'Potato', 10, '2020-12-20'),
    (100002, 'IceCream', 20, '2020-12-19'),
    (100002, 'Orange fresh', 40, '2020-12-19'),
    (100003, 'Soup', 8, '2020-12-19'),
    (100003, 'Cheeseburger', 40, '2020-12-19');

INSERT INTO votes (user_id, restaurant_id, voting_date) VALUES
    (100000, 100003, '2020-12-20'),
    (100001, 100002, '2020-12-20'),
    (100000, 100003, '2020-12-19'),
    (100001, 100003, '2020-12-19');
