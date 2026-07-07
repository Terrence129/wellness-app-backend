package com.wellness.wellnessappbackend.common;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record FieldErrorResponse(
        String field,
        String message
) {
}
