CREATE TABLE ai_chat_messages (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    conversation_id VARCHAR(36) NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    model_name VARCHAR(100) NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    CONSTRAINT pk_ai_chat_messages PRIMARY KEY (id),
    CONSTRAINT fk_ai_chat_messages_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT chk_ai_chat_messages_role
        CHECK (role IN ('USER', 'ASSISTANT')),
    CONSTRAINT chk_ai_chat_messages_content_len
        CHECK (CHAR_LENGTH(content) BETWEEN 1 AND 5000),
    CONSTRAINT chk_ai_chat_messages_conversation_id_len
        CHECK (CHAR_LENGTH(conversation_id) = 36),
    CONSTRAINT chk_ai_chat_messages_model_name_len
        CHECK (model_name IS NULL OR CHAR_LENGTH(model_name) BETWEEN 1 AND 100)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_ai_chat_messages_user_conversation_created_at
    ON ai_chat_messages (user_id, conversation_id, created_at);

CREATE INDEX idx_ai_chat_messages_user_created_at
    ON ai_chat_messages (user_id, created_at);
