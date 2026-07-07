package com.wellness.wellnessappbackend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
