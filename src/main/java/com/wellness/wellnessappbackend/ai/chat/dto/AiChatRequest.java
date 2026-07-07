package com.wellness.wellnessappbackend.ai.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record AiChatRequest(
        @Size(max = 36) String conversationId,
        @NotBlank @Size(max = 2000) String message
) {
}
