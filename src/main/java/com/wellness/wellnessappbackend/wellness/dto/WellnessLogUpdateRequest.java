package com.wellness.wellnessappbackend.wellness.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record WellnessLogUpdateRequest(
        LocalDate logDate,
        @DecimalMin("0.0") @DecimalMax("24.0") BigDecimal sleepHours,
        @Min(1) @Max(5) Integer moodScore,
        @Min(0) @Max(100) Integer waterCups,
        @Min(0) @Max(1000000) Integer steps,
        @Min(0) @Max(1440) Integer exerciseMinutes,
        @Size(max = 1000) String note
) {
}
