package com.wellness.wellnessappbackend.auth;

import com.wellness.wellnessappbackend.user.UserDto;

import java.time.Instant;

public record LoginData(
        String token,
        String tokenType,
        Instant expiresAt,
        UserDto user
) {
}
