CREATE TABLE user_personal_info (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    height_cm DECIMAL(5,1) NOT NULL,
    weight_kg DECIMAL(5,1) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    activity_level VARCHAR(30) NOT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    CONSTRAINT pk_user_personal_info PRIMARY KEY (id),
    CONSTRAINT uq_user_personal_info_user UNIQUE (user_id),
    CONSTRAINT fk_user_personal_info_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT chk_user_personal_info_height_cm
        CHECK (height_cm >= 50.0 AND height_cm <= 250.0),
    CONSTRAINT chk_user_personal_info_weight_kg
        CHECK (weight_kg >= 2.0 AND weight_kg <= 500.0),
    CONSTRAINT chk_user_personal_info_gender
        CHECK (gender IN ('MALE', 'FEMALE', 'NON_BINARY', 'PREFER_NOT_TO_SAY')),
    CONSTRAINT chk_user_personal_info_activity_level
        CHECK (activity_level IN ('SEDENTARY', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'VERY_ACTIVE'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
