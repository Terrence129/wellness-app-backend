package com.wellness.wellnessappbackend.ai.chat.dto;

import java.time.Instant;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public record AiChatConversationDto(
        String conversationId,
        Instant startedAt,
        Instant lastMessageAt,
        long messageCount,
        String lastRole,
        String lastMessagePreview
) {
}
