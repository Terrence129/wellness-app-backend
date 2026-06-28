package com.wellness.wellnessappbackend.summary;

import java.time.LocalDate;

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
