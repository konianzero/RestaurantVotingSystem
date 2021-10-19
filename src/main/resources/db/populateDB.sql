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

INSERT INTO restaurants (name, address) VALUES
    ('Restaurant1', 'ул. Мира, 67'),
    ('Restaurant2', 'ул. Бумажная, д.20');

INSERT INTO dishes (restaurant_id, name, price, date) VALUES
    (100002, 'IceCream', 20, DATE_SUB(NOW, 1 DAY)),
    (100002, 'Orange fresh', 40, DATE_SUB(NOW, 1 DAY)),
    (100003, 'Soup', 8, DATE_SUB(NOW, 1 DAY)),
    (100003, 'Cheeseburger', 40, DATE_SUB(NOW, 1 DAY)),
    (100002, 'Salad', 5, NOW),
    (100002, 'Water', 1, NOW),
    (100003, 'Burger', 40, NOW),
    (100003, 'Potato', 10, NOW);

INSERT INTO votes (user_id, restaurant_id, voting_date) VALUES
    (100000, 100003, DATE_SUB(NOW, 1 DAY)),
    (100001, 100003, DATE_SUB(NOW, 1 DAY)),
    (100000, 100003, NOW);
--     (100001, 100002, NOW);
