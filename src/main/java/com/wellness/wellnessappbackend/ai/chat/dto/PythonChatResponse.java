package com.wellness.wellnessappbackend.ai.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PythonChatResponse(
        String reply,
        String requestId,
        String modelName
) {
}
