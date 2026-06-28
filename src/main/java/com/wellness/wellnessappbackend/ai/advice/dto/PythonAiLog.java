package com.wellness.wellnessappbackend.ai.advice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PythonAiLog(
        LocalDate logDate,
        BigDecimal sleepHours,
        Integer moodScore,
        Integer waterCups,
        Integer steps,
        Integer exerciseMinutes,
        String note
) {
}
