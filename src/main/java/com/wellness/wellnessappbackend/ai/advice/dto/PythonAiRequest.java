package com.wellness.wellnessappbackend.ai.advice.dto;

import java.time.LocalDate;
import java.util.List;

public record PythonAiRequest(
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        List<PythonAiLog> logs
) {
}
