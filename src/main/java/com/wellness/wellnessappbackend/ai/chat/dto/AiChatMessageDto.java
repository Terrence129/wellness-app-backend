package com.wellness.wellnessappbackend.ai.chat.dto;

import java.time.Instant;

public record AiChatMessageDto(
        String role,
        String content,
        String modelName,
        Instant createdAt
) {
}
