package com.wellness.wellnessappbackend.user;

public record UserDto(
        Long id,
        String username,
        String email
) {
}
