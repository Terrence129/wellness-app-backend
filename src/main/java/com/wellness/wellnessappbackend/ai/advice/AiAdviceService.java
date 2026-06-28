package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.ai.AiClient;
import com.wellness.wellnessappbackend.ai.advice.dto.GenerateAiAdviceRequest;
import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiRequest;
import com.wellness.wellnessappbackend.ai.advice.dto.PythonAiResponse;
import com.wellness.wellnessappbackend.common.DateRangeValidator;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserRepository;
import com.wellness.wellnessappbackend.wellness.WellnessLog;
import com.wellness.wellnessappbackend.wellness.WellnessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAdviceService {

    private final AiClient aiClient;
    private final AiAdviceRepository aiAdviceRepository;
    private final AiAdviceMapper aiAdviceMapper;
    private final WellnessLogRepository wellnessLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public AiAdvice generate(Long userId, GenerateAiAdviceRequest request) {
        DateRangeValidator.validateRequiredRange(request.startDate(), request.endDate());

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "User not found"));
        List<WellnessLog> logs = wellnessLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(
                userId,
                request.startDate(),
                request.endDate()
        );
        if (logs.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "No wellness logs found for selected date range");
        }

        PythonAiRequest aiRequest = new PythonAiRequest(
                userId,
                logs.stream().map(aiAdviceMapper::toPythonLog).toList()
        );
        PythonAiResponse aiResponse = aiClient.generateAdvice(aiRequest);

        AiAdvice advice = new AiAdvice();
        advice.setUser(user);
        advice.setAdviceDate(request.endDate());
        advice.setSourceStartDate(request.startDate());
        advice.setSourceEndDate(request.endDate());
        advice.setAdviceText(aiResponse.adviceText());
        advice.setModelName(aiResponse.modelName());

        return aiAdviceRepository.save(advice);
    }

    @Transactional(readOnly = true)
    public AiAdvice latest(Long userId) {
        return aiAdviceRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.NO_AI_ADVICE_FOUND, "No AI advice found"));
    }
}
