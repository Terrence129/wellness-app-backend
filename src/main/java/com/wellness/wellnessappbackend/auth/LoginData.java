package com.wellness.wellnessappbackend.auth;

import com.wellness.wellnessappbackend.user.UserDto;

import java.time.Instant;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record LoginData(
        String token,
        String tokenType,
        Instant expiresAt,
        UserDto user
) {
}
