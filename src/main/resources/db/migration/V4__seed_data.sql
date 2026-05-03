INSERT INTO categories (id, name) VALUES
    (0, 'Sports'),
    (1, 'Finance'),
    (2, 'Movies');

INSERT INTO channel_types (id, name) VALUES
    (0, 'SMS'),
    (1, 'Email'),
    (2, 'Push Notification');

INSERT INTO users (id, name, email, phone) VALUES
    (1, 'Alice Johnson',  'alice@example.com',  '+1-555-0101'),
    (2, 'Bob Martinez',   'bob@example.com',    '+1-555-0102'),
    (3, 'Carol Williams', 'carol@example.com',  '+1-555-0103'),
    (4, 'David Chen',     'david@example.com',  '+1-555-0104'),
    (5, 'Eva Rodriguez',  'eva@example.com',    '+1-555-0105'),
    (6, 'Frank Kim',      'frank@example.com',  '+1-555-0106'),
    (7, 'Grace Lee',      'grace@example.com',  '+1-555-0107'),
    (8, 'Henry Park',     'henry@example.com',  '+1-555-0108'),
    (9, 'Iris Wang',      'iris@example.com',   '+1-555-0109'),
    (10, 'Jack Brown',    'jack@example.com',   '+1-555-0110');

INSERT INTO user_category_subscriptions (user_id, category_id) VALUES
    (1, 0), (1, 1),
    (2, 0),
    (3, 1), (3, 2),
    (4, 0), (4, 2),
    (5, 1), (5, 2),
    (6, 0), (6, 1), (6, 2),
    (7, 0),
    (8, 1),
    (9, 2),
    (10, 0), (10, 1), (10, 2);

INSERT INTO user_channels (user_id, channel_type_id) VALUES
    (1, 0), (1, 1),
    (2, 1),
    (3, 0), (3, 2),
    (4, 1), (4, 2),
    (5, 0),
    (6, 0), (6, 1), (6, 2),
    (7, 0), (7, 1),
    (8, 1), (8, 2),
    (9, 0), (9, 2),
    (10, 0), (10, 1), (10, 2);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
