INSERT INTO "user" (email, first_name, is_accept_terms, last_name, password, role)
VALUES ('john@doe.com', 'John', true, 'Doe', '$2a$12$S/VXri58yQkBCEIk9CnRtOSXZLFEa03dd5gJ5YwfqFH8wR6Lbfq8S', 'ROLE_USER');

INSERT INTO sport (name, image_url, number_of_players)
VALUES
    ('Volley', 'https://images.pexels.com/photos/1263426/pexels-photo-1263426.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 6);

INSERT INTO team (name, sport_id, owner_id)
VALUES
    ('Team', 1, 1),
    ('TeamA', 1, 1);

INSERT INTO tournament (created_at, current_number_of_participants, date, image_url, inscription_limit_date, is_generated, max_number_of_teams, name, place, updated_at, sport_id, owner_id, is_canceled)
VALUES ('2024-05-11 09:08:44', 6, '2024-05-11 09:08:44', 'image', '2024-05-11 09:08:44', 0, 6, 'tournament', 'Bordeaux', null, 1, 1, 0),
       ('2024-05-11 09:08:44', 6, '2024-05-11 09:08:44', 'image', '2024-05-11 09:08:44', 1, 6, 'tournament2', 'Bordeaux', null, 1, 1, 0),
       ('2024-05-11 09:08:44', 6, '2025-05-11 09:08:44', 'image', '2025-05-11 09:08:44', 0, 6, 'tournament3', 'Bordeaux', null, 1, 1, 0);
