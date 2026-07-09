package com.wellness.wellnessappbackend.personalinfo.dto;

import com.wellness.wellnessappbackend.personalinfo.ActivityLevel;
import com.wellness.wellnessappbackend.personalinfo.Gender;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public record PersonalInfoUpsertRequest(
        @NotNull @DecimalMin("50.0") @DecimalMax("250.0") @Digits(integer = 3, fraction = 1) BigDecimal heightCm,
        @NotNull @DecimalMin("2.0") @DecimalMax("500.0") @Digits(integer = 3, fraction = 1) BigDecimal weightKg,
        @NotNull Gender gender,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull ActivityLevel activityLevel
) {
}
