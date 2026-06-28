package com.wellness.wellnessappbackend.ai.advice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PythonAiResponse(
        String adviceText,
        String requestId,
        String modelName
) {
}
