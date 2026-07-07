package com.wellness.wellnessappbackend.ai.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record PythonChatMessage(
        @NotBlank @Pattern(regexp = "user|assistant|system") String role,
        @NotBlank @Size(max = 5000) String content
) {
}
