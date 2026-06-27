package com.wellness.wellnessappbackend.wellness;

import com.wellness.wellnessappbackend.common.DateRangeValidator;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserRepository;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogCreateRequest;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WellnessLogService {

    private final WellnessLogRepository wellnessLogRepository;
    private final UserRepository userRepository;
    private final WellnessLogMapper wellnessLogMapper;

    @Transactional
    public WellnessLog create(Long userId, WellnessLogCreateRequest request) {
        if (wellnessLogRepository.existsByUserIdAndLogDate(userId, request.logDate())) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    ErrorCode.WELLNESS_LOG_ALREADY_EXISTS,
                    "Wellness log already exists for this date"
            );
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        WellnessLog log = new WellnessLog();
        log.setUser(user);
        wellnessLogMapper.applyCreateRequest(log, request);
        return wellnessLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<WellnessLog> list(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        DateRangeValidator.validateOptionalRange(startDate, endDate);
        return wellnessLogRepository.findByUserAndOptionalDateRange(userId, startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public WellnessLog getByDate(Long userId, LocalDate logDate) {
        return wellnessLogRepository.findByUserIdAndLogDate(userId, logDate)
                .orElseThrow(this::notFound);
    }

    @Transactional
    public WellnessLog update(Long userId, Long id, WellnessLogUpdateRequest request) {
        WellnessLog log = wellnessLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(this::notFound);
        wellnessLogMapper.applyUpdateRequest(log, request);
        return wellnessLogRepository.save(log);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        WellnessLog log = wellnessLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(this::notFound);
        wellnessLogRepository.delete(log);
    }

    private ApiException notFound() {
        return new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "Wellness log not found");
    }
}
