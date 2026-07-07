package com.wellness.wellnessappbackend.wellness.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record WellnessLogDto(
        Long id,
        LocalDate logDate,
        BigDecimal sleepHours,
        Integer moodScore,
        Integer waterCups,
        Integer steps,
        Integer exerciseMinutes,
        String note,
        Instant createdAt,
        Instant updatedAt
) {
}
