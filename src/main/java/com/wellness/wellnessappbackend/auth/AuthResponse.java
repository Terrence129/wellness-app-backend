package com.wellness.wellnessappbackend.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInMillis
) {
}
