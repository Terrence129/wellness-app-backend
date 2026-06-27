CREATE TABLE users (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_username_len CHECK (CHAR_LENGTH(username) BETWEEN 3 AND 50),
    CONSTRAINT chk_users_email_len CHECK (CHAR_LENGTH(email) BETWEEN 5 AND 254),
    CONSTRAINT chk_users_password_hash_len CHECK (CHAR_LENGTH(password_hash) BETWEEN 20 AND 255)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE wellness_logs (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    log_date DATE NOT NULL,
    sleep_hours DECIMAL(4,2) NULL,
    mood_score TINYINT UNSIGNED NULL,
    water_cups SMALLINT UNSIGNED NULL,
    steps INT UNSIGNED NULL,
    exercise_minutes SMALLINT UNSIGNED NULL,
    note VARCHAR(1000) NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    CONSTRAINT pk_wellness_logs PRIMARY KEY (id),
    CONSTRAINT uq_wellness_logs_user_date UNIQUE (user_id, log_date),
    CONSTRAINT fk_wellness_logs_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT chk_wellness_sleep_hours
        CHECK (sleep_hours IS NULL OR (sleep_hours >= 0 AND sleep_hours <= 24)),
    CONSTRAINT chk_wellness_mood_score
        CHECK (mood_score IS NULL OR mood_score BETWEEN 1 AND 5),
    CONSTRAINT chk_wellness_water_cups
        CHECK (water_cups IS NULL OR water_cups <= 100),
    CONSTRAINT chk_wellness_steps
        CHECK (steps IS NULL OR steps <= 1000000),
    CONSTRAINT chk_wellness_exercise_minutes
        CHECK (exercise_minutes IS NULL OR exercise_minutes <= 1440)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_wellness_logs_user_date
    ON wellness_logs (user_id, log_date);

CREATE INDEX idx_wellness_logs_log_date
    ON wellness_logs (log_date);

CREATE TABLE ai_advice (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    advice_date DATE NOT NULL,
    source_start_date DATE NULL,
    source_end_date DATE NULL,
    advice_text TEXT NOT NULL,
    model_name VARCHAR(100) NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    CONSTRAINT pk_ai_advice PRIMARY KEY (id),
    CONSTRAINT fk_ai_advice_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT chk_ai_advice_text_len
        CHECK (CHAR_LENGTH(advice_text) BETWEEN 1 AND 5000),
    CONSTRAINT chk_ai_advice_window
        CHECK (
            (source_start_date IS NULL AND source_end_date IS NULL)
            OR
            (source_start_date IS NOT NULL AND source_end_date IS NOT NULL AND source_start_date <= source_end_date)
        ),
    CONSTRAINT chk_ai_advice_model_name_len
        CHECK (model_name IS NULL OR CHAR_LENGTH(model_name) BETWEEN 1 AND 100)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_ai_advice_user_created_at
    ON ai_advice (user_id, created_at);

CREATE INDEX idx_ai_advice_user_advice_date
    ON ai_advice (user_id, advice_date);
