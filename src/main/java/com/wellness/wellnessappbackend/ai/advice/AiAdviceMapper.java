package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.ai.advice.dto.AiAdviceDto;
import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiLog;
import com.wellness.wellnessappbackend.wellness.WellnessLog;
import org.springframework.stereotype.Component;

@Component
public class AiAdviceMapper {

    public AiAdviceDto toDto(AiAdvice advice) {
        return new AiAdviceDto(
                advice.getId(),
                advice.getAdviceDate(),
                advice.getSourceStartDate(),
                advice.getSourceEndDate(),
                advice.getAdviceText(),
                advice.getModelName(),
                advice.getCreatedAt()
        );
    }

    public PythonAiLog toPythonLog(WellnessLog log) {
        return new PythonAiLog(
                log.getLogDate(),
                log.getSleepHours(),
                log.getMoodScore(),
                log.getWaterCups(),
                log.getSteps(),
                log.getExerciseMinutes(),
                log.getNote()
        );
    }
}
