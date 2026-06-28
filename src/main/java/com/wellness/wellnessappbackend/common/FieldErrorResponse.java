package com.wellness.wellnessappbackend.common;

public record FieldErrorResponse(
        String field,
        String message
) {
}
