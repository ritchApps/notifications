INSERT INTO categories (id, name) VALUES
    (1, 'Sports'),
    (2, 'Finance'),
    (3, 'Movies');

INSERT INTO channel_types (id, name) VALUES
    (1, 'SMS'),
    (2, 'Email'),
    (3, 'Push Notification');

INSERT INTO users (id, name, email, phone) VALUES
    (1, 'Alice Johnson',  'alice@example.com',  '+1-555-0101'),
    (2, 'Bob Martinez',   'bob@example.com',    '+1-555-0102'),
    (3, 'Carol Williams', 'carol@example.com',  '+1-555-0103'),
    (4, 'David Chen',     'david@example.com',  '+1-555-0104'),
    (5, 'Eva Rodriguez',  'eva@example.com',    '+1-555-0105'),
    (6, 'Frank Kim',      'frank@example.com',  '+1-555-0106');

INSERT INTO user_category_subscriptions (user_id, category_id) VALUES
    (1, 1), -- Alice   → Sports
    (1, 2), -- Alice   → Finance
    (2, 1), -- Bob     → Sports
    (3, 2), -- Carol   → Finance
    (3, 3), -- Carol   → Movies
    (4, 1), -- David   → Sports
    (4, 3), -- David   → Movies
    (5, 2), -- Eva     → Finance
    (5, 3), -- Eva     → Movies
    (6, 1), -- Frank   → Sports
    (6, 2), -- Frank   → Finance
    (6, 3); -- Frank   → todas

INSERT INTO user_channels (user_id, channel_type_id) VALUES
    (1, 1), -- Alice   → SMS
    (1, 2), -- Alice   → Email
    (2, 2), -- Bob     → Email
    (3, 1), -- Carol   → SMS
    (3, 3), -- Carol   → Push
    (4, 2), -- David   → Email
    (4, 3), -- David   → Push
    (5, 1), -- Eva     → SMS
    (6, 1), -- Frank   → SMS
    (6, 2), -- Frank   → Email
    (6, 3); -- Frank   → Push (todos los canales)

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
















