package com.wellness.wellnessappbackend.common;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public final class DateRangeValidator {

    private DateRangeValidator() {
    }

    public static void validateOptionalRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw invalidRange();
        }
    }

    public static void validateRequiredRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw invalidRange();
        }
    }

    private static ApiException invalidRange() {
        return new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATE_RANGE, "Invalid date range");
    }
}
