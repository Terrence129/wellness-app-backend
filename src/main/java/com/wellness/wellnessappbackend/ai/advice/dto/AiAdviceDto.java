package com.wellness.wellnessappbackend.ai.advice.dto;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

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
