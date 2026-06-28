package com.wellness.wellnessappbackend.ai.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiChatRequest(
        @Size(max = 36) String conversationId,
        @NotBlank @Size(max = 2000) String message
) {
}
