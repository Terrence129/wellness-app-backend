package com.wellness.wellnessappbackend.ai.advice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record PythonAiRequest(
        @NotNull Long userId,
        @NotNull @Size(max = 31) List<@Valid PythonAiLog> logs
) {
}
