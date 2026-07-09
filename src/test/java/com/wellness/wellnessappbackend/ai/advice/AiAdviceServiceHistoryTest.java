package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.ai.AiClient;
import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.user.UserRepository;
import com.wellness.wellnessappbackend.wellness.WellnessLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiAdviceServiceHistoryTest {

    @Mock
    private AiClient aiClient;

    @Mock
    private AiAdviceRepository aiAdviceRepository;

    @Mock
    private AiAdviceMapper aiAdviceMapper;

    @Mock
    private WellnessLogRepository wellnessLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AiAdviceService aiAdviceService;

    @Test
    void listUsesCurrentUserAndAdviceDateRange() {
        LocalDate startDate = LocalDate.of(2026, 6, 20);
        LocalDate endDate = LocalDate.of(2026, 6, 26);
        PageRequest pageable = PageRequest.of(0, 20);
        AiAdvice advice = new AiAdvice();
        Page<AiAdvice> page = new PageImpl<>(List.of(advice), pageable, 1);
        when(aiAdviceRepository.findByUserAndOptionalAdviceDateRange(7L, startDate, endDate, pageable))
                .thenReturn(page);

        Page<AiAdvice> result = aiAdviceService.list(7L, startDate, endDate, pageable);

        assertThat(result.getContent()).containsExactly(advice);
        verify(aiAdviceRepository).findByUserAndOptionalAdviceDateRange(7L, startDate, endDate, pageable);
    }

    @Test
    void listRejectsInvalidDateRange() {
        PageRequest pageable = PageRequest.of(0, 20);

        assertThatThrownBy(() -> aiAdviceService.list(
                7L,
                LocalDate.of(2026, 6, 26),
                LocalDate.of(2026, 6, 20),
                pageable
        )).isInstanceOfSatisfying(ApiException.class, ex -> {
            assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_DATE_RANGE);
        });
    }

    @Test
    void getByIdUsesCurrentUser() {
        AiAdvice advice = new AiAdvice();
        when(aiAdviceRepository.findByIdAndUserId(101L, 7L)).thenReturn(Optional.of(advice));

        AiAdvice result = aiAdviceService.getById(7L, 101L);

        assertThat(result).isSameAs(advice);
        verify(aiAdviceRepository).findByIdAndUserId(101L, 7L);
    }

    @Test
    void getByIdReturnsNotFoundForMissingOrOtherUserAdvice() {
        when(aiAdviceRepository.findByIdAndUserId(101L, 7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aiAdviceService.getById(7L, 101L))
                .isInstanceOfSatisfying(ApiException.class, ex -> {
                    assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
                    assertThat(ex.getMessage()).isEqualTo("AI advice not found");
                });
    }
}
