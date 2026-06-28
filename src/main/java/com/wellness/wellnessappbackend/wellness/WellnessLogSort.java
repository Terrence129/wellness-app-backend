package com.wellness.wellnessappbackend.wellness;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public final class WellnessLogSort {

    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "logDate", "logDate",
            "createdAt", "createdAt",
            "updatedAt", "updatedAt",
            "moodScore", "moodScore",
            "sleepHours", "sleepHours"
    );

    private WellnessLogSort() {
    }

    public static Sort parse(String sort) {
        String value = sort == null || sort.isBlank() ? "logDate,desc" : sort.trim();
        String[] parts = value.split(",");
        String field = parts[0].trim();
        String property = ALLOWED_FIELDS.get(field);
        if (property == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "Unsupported sort field");
        }

        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length > 1) {
            direction = Sort.Direction.fromOptionalString(parts[1].trim())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "Unsupported sort direction"));
        }

        return Sort.by(new Sort.Order(direction, property));
    }

    public static List<String> describe(Sort sort) {
        return sort.stream()
                .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
                .toList();
    }
}
