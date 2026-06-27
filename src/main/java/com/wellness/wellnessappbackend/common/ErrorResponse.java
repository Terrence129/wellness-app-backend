package com.wellness.wellnessappbackend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wellness.wellnessappbackend.exception.ErrorCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        boolean success,
        String message,
        ErrorCode errorCode,
        List<FieldErrorResponse> errors
) {
    public static ErrorResponse of(String message, ErrorCode errorCode) {
        return new ErrorResponse(false, message, errorCode, List.of());
    }

    public static ErrorResponse of(String message, ErrorCode errorCode, List<FieldErrorResponse> errors) {
        return new ErrorResponse(false, message, errorCode, errors);
    }
}
