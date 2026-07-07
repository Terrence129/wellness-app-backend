package com.wellness.wellnessappbackend.ai.chat.dto;

import java.time.Instant;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record AiChatMessageDto(
        String role,
        String content,
        String modelName,
        Instant createdAt
) {
}
