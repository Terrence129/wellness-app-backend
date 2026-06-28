package com.wellness.wellnessappbackend.ai.advice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GenerateAiAdviceRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
