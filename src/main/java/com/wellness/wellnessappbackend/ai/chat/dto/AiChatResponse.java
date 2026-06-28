package com.wellness.wellnessappbackend.ai.chat.dto;

import java.time.Instant;
import java.util.List;

public record AiChatResponse(
        String conversationId,
        String reply,
        String modelName,
        Instant createdAt,
        List<AiChatMessageDto> messages
) {
}
