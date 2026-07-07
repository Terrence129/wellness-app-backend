package com.wellness.wellnessappbackend.ai.chat.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record PythonChatRequest(
        @NotNull Long userId,
        @NotBlank @Size(max = 2000) String message,
        @NotNull @Size(max = 50) List<@Valid PythonChatMessage> history
) {
}
