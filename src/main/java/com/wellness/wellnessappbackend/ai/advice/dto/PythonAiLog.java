package com.wellness.wellnessappbackend.ai.advice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PythonAiLog(
        @NotNull LocalDate logDate,
        BigDecimal sleepHours,
        @Min(1) @Max(5) Integer moodScore,
        @Min(0) Integer waterCups,
        @Min(0) Integer steps,
        @Min(0) @Max(1440) Integer exerciseMinutes,
        @Size(max = 1000) String note
) {
}
