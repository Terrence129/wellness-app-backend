package com.wellness.wellnessappbackend.summary;

import com.wellness.wellnessappbackend.common.DateRangeValidator;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.wellness.WellnessLog;
import com.wellness.wellnessappbackend.wellness.WellnessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Service
@RequiredArgsConstructor
public class WellnessSummaryService {

    private final WellnessLogRepository wellnessLogRepository;

    @Transactional(readOnly = true)
    public WeeklySummaryDto weeklySummary(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDate resolvedEnd = endDate;
        LocalDate resolvedStart = startDate;

        if (resolvedStart == null && resolvedEnd == null) {
            resolvedEnd = LocalDate.now();
            resolvedStart = resolvedEnd.minusDays(6);
        } else if (resolvedStart == null || resolvedEnd == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATE_RANGE, "Both startDate and endDate are required");
        }

        DateRangeValidator.validateRequiredRange(resolvedStart, resolvedEnd);

        List<WellnessLog> logs = wellnessLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(
                userId,
                resolvedStart,
                resolvedEnd
        );

        double averageSleep = average(logs.stream().map(WellnessLog::getSleepHours).filter(Objects::nonNull).toList());
        double averageMood = averageInteger(logs.stream().map(WellnessLog::getMoodScore).filter(Objects::nonNull).toList());
        double averageWater = averageInteger(logs.stream().map(WellnessLog::getWaterCups).filter(Objects::nonNull).toList());
        long totalSteps = logs.stream().map(WellnessLog::getSteps).filter(Objects::nonNull).mapToLong(Integer::longValue).sum();
        long totalExercise = logs.stream().map(WellnessLog::getExerciseMinutes).filter(Objects::nonNull).mapToLong(Integer::longValue).sum();

        return new WeeklySummaryDto(
                resolvedStart,
                resolvedEnd,
                logs.size(),
                averageSleep,
                averageMood,
                averageWater,
                totalSteps,
                totalExercise,
                buildSummary(logs.size(), averageSleep, averageMood, totalExercise)
        );
    }

    private double average(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return 0.0;
        }
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP).doubleValue();
    }

    private double averageInteger(List<Integer> values) {
        if (values.isEmpty()) {
            return 0.0;
        }
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        return BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private String buildSummary(int daysWithLogs, double averageSleep, double averageMood, long totalExercise) {
        if (daysWithLogs == 0) {
            return "No wellness logs were recorded for this period.";
        }
        if (averageSleep < 6.0) {
            return "Your sleep was lower than ideal this week. Try protecting a consistent bedtime.";
        }
        if (totalExercise < 60) {
            return "Your logs show limited exercise time. A few short walks could help build consistency.";
        }
        if (averageMood >= 4.0) {
            return "Your mood and routine look stable this week. Keep the pattern going.";
        }
        return "Your week has some mixed signals. Focus on steady sleep, hydration, and light movement.";
    }
}
