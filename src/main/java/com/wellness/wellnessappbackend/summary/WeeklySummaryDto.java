package com.wellness.wellnessappbackend.summary;

import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record WeeklySummaryDto(
        LocalDate startDate,
        LocalDate endDate,
        int daysWithLogs,
        double averageSleepHours,
        double averageMoodScore,
        double averageWaterCups,
        long totalSteps,
        long totalExerciseMinutes,
        String summary
) {
}
