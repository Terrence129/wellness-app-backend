package com.wellness.wellnessappbackend.ai.advice.dto;

import java.time.Instant;
import java.time.LocalDate;

public record AiAdviceDto(
        Long id,
        LocalDate adviceDate,
        LocalDate startDate,
        LocalDate endDate,
        String adviceText,
        String modelName,
        Instant createdAt
) {
}
