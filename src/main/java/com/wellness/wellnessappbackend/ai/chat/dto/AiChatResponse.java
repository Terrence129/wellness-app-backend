package com.wellness.wellnessappbackend.ai.chat.dto;

import java.time.Instant;
import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record AiChatResponse(
        String conversationId,
        String reply,
        String requestId,
        String modelName,
        Instant createdAt,
        List<AiChatMessageDto> messages
) {
}
