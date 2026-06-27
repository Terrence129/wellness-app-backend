package com.wellness.wellnessappbackend.wellness;

import com.wellness.wellnessappbackend.wellness.dto.WellnessLogCreateRequest;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogDto;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class WellnessLogMapper {

    public WellnessLogDto toDto(WellnessLog log) {
        return new WellnessLogDto(
                log.getId(),
                log.getLogDate(),
                log.getSleepHours(),
                log.getMoodScore(),
                log.getWaterCups(),
                log.getSteps(),
                log.getExerciseMinutes(),
                log.getNote(),
                log.getCreatedAt(),
                log.getUpdatedAt()
        );
    }

    public void applyCreateRequest(WellnessLog log, WellnessLogCreateRequest request) {
        log.setLogDate(request.logDate());
        log.setSleepHours(request.sleepHours());
        log.setMoodScore(request.moodScore());
        log.setWaterCups(request.waterCups());
        log.setSteps(request.steps());
        log.setExerciseMinutes(request.exerciseMinutes());
        log.setNote(request.note());
    }

    public void applyUpdateRequest(WellnessLog log, WellnessLogUpdateRequest request) {
        log.setSleepHours(request.sleepHours());
        log.setMoodScore(request.moodScore());
        log.setWaterCups(request.waterCups());
        log.setSteps(request.steps());
        log.setExerciseMinutes(request.exerciseMinutes());
        log.setNote(request.note());
    }
}
