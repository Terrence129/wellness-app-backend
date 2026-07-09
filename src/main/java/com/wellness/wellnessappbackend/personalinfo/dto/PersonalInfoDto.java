package com.wellness.wellnessappbackend.personalinfo.dto;

import com.wellness.wellnessappbackend.personalinfo.ActivityLevel;
import com.wellness.wellnessappbackend.personalinfo.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public record PersonalInfoDto(
        Long id,
        BigDecimal heightCm,
        BigDecimal weightKg,
        Gender gender,
        LocalDate dateOfBirth,
        ActivityLevel activityLevel,
        BigDecimal bmi,
        Instant createdAt,
        Instant updatedAt
) {
}
