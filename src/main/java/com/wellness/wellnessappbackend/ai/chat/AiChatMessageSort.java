package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public final class AiChatMessageSort {

    private static final Map<String, String> ALLOWED_FIELDS = Map.of("createdAt", "createdAt");

    private AiChatMessageSort() {
    }

    public static Sort parse(String sort) {
        String value = sort == null || sort.isBlank() ? "createdAt,asc" : sort.trim();
        String[] parts = value.split(",");
        String field = parts[0].trim();
        String property = ALLOWED_FIELDS.get(field);
        if (property == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "Unsupported sort field");
        }

        Sort.Direction direction = Sort.Direction.ASC;
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
