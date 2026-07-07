package com.wellness.wellnessappbackend.ai.advice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record GenerateAiAdviceRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
