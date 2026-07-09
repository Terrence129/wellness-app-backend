package com.wellness.wellnessappbackend.user;

import java.math.BigDecimal;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record UserDto(
        Long id,
        String username,
        String email,
        BigDecimal heightCm,
        BigDecimal weightKg,
        Integer age
) {
}
