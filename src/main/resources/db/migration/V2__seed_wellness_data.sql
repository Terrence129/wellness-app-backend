INSERT INTO users (id, username, email, password_hash, created_at, updated_at) VALUES
(1, 'Dadao', 'dadao@example.com', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '2026-06-20 09:00:00.000', '2026-06-20 09:00:00.000'),
(2, 'Alice', 'alice@example.com', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '2026-06-20 09:10:00.000', '2026-06-20 09:10:00.000');

INSERT INTO wellness_logs (
    id, user_id, log_date, sleep_hours, mood_score, water_cups, steps, exercise_minutes, note, created_at, updated_at
) VALUES
(101, 1, '2026-06-20', 7.50, 4, 6, 8200, 30, 'Felt good overall.', '2026-06-20 20:00:00.000', '2026-06-20 20:00:00.000'),
(102, 1, '2026-06-21', 6.75, 3, 5, 6000, 20, 'A bit tired in the afternoon.', '2026-06-21 20:00:00.000', '2026-06-21 20:00:00.000'),
(103, 1, '2026-06-22', 7.25, 4, 7, 9100, 35, 'Good focus today.', '2026-06-22 20:00:00.000', '2026-06-22 20:00:00.000'),
(104, 1, '2026-06-23', 5.80, 2, 4, 4200, 10, 'Poor sleep and low energy.', '2026-06-23 20:00:00.000', '2026-06-23 20:00:00.000'),
(105, 1, '2026-06-24', 7.90, 5, 8, 10050, 40, 'Best day this week.', '2026-06-24 20:00:00.000', '2026-06-24 20:00:00.000'),
(106, 1, '2026-06-25', 7.10, 4, 6, 8400, 25, 'Stable routine.', '2026-06-25 20:00:00.000', '2026-06-25 20:00:00.000'),
(107, 1, '2026-06-26', 6.90, 4, 6, 7600, 30, 'Felt okay.', '2026-06-26 20:00:00.000', '2026-06-26 20:00:00.000'),
(201, 2, '2026-06-24', 8.10, 4, 7, 7200, 20, 'Pretty balanced day.', '2026-06-24 18:00:00.000', '2026-06-24 18:00:00.000'),
(202, 2, '2026-06-25', 7.80, 5, 8, 8500, 45, 'Very productive.', '2026-06-25 18:00:00.000', '2026-06-25 18:00:00.000');

INSERT INTO ai_advice (
    id, user_id, advice_date, source_start_date, source_end_date, advice_text, model_name, created_at
) VALUES
(1001, 1, '2026-06-26', '2026-06-20', '2026-06-26',
 'Your overall routine looks stable, but your low-sleep day on 2026-06-23 appears to have affected mood and activity. Try protecting sleep consistency and increasing water intake on busy days.',
 'simplewell-rule-based-v1', '2026-06-26 21:00:00.000'),
(1002, 2, '2026-06-25', '2026-06-24', '2026-06-25',
 'Your recent pattern looks healthy and active. Keep your sleep schedule regular and continue moderate exercise.',
 'simplewell-rule-based-v1', '2026-06-25 19:00:00.000');
