package com.wellness.wellnessappbackend.personalinfo;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoUpsertRequest;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalInfoServiceTest {

    @Mock
    private PersonalInfoRepository personalInfoRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PersonalInfoMapper personalInfoMapper = new PersonalInfoMapper(new PersonalInfoBmiCalculator());

    @InjectMocks
    private PersonalInfoService personalInfoService;

    @Test
    void getCurrentUsesCurrentUserId() {
        PersonalInfo personalInfo = personalInfoFor(user(7L));
        when(personalInfoRepository.findByUserId(7L)).thenReturn(Optional.of(personalInfo));

        PersonalInfo result = personalInfoService.getCurrent(7L);

        assertThat(result).isSameAs(personalInfo);
        verify(personalInfoRepository).findByUserId(7L);
    }

    @Test
    void getCurrentThrowsNotFoundWhenMissing() {
        when(personalInfoRepository.findByUserId(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalInfoService.getCurrent(7L))
                .isInstanceOfSatisfying(ApiException.class, ex -> {
                    assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
                    assertThat(ex.getMessage()).isEqualTo("Personal info not found");
                });
    }

    @Test
    void upsertCreatesFirstPersonalInfoForCurrentUser() {
        AppUser user = user(7L);
        PersonalInfoUpsertRequest request = request(new BigDecimal("172.5"), new BigDecimal("68.4"));
        when(personalInfoRepository.findByUserId(7L)).thenReturn(Optional.empty());
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(personalInfoRepository.save(any(PersonalInfo.class))).thenAnswer(invocation -> {
            PersonalInfo saved = invocation.getArgument(0);
            saved.setId(101L);
            return saved;
        });

        PersonalInfoUpsertResult result = personalInfoService.upsert(7L, request);

        assertThat(result.created()).isTrue();
        assertThat(result.personalInfo().getId()).isEqualTo(101L);
        assertThat(result.personalInfo().getUser()).isSameAs(user);
        assertThat(result.personalInfo().getHeightCm()).isEqualByComparingTo("172.5");
        assertThat(result.personalInfo().getWeightKg()).isEqualByComparingTo("68.4");
        assertThat(result.personalInfo().getGender()).isEqualTo(Gender.MALE);
        assertThat(result.personalInfo().getActivityLevel()).isEqualTo(ActivityLevel.MODERATELY_ACTIVE);
    }

    @Test
    void upsertUpdatesExistingPersonalInfoForCurrentUser() {
        PersonalInfo existing = personalInfoFor(user(7L));
        PersonalInfoUpsertRequest request = request(new BigDecimal("180.0"), new BigDecimal("75.0"));
        when(personalInfoRepository.findByUserId(7L)).thenReturn(Optional.of(existing));
        when(personalInfoRepository.save(any(PersonalInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PersonalInfoUpsertResult result = personalInfoService.upsert(7L, request);

        assertThat(result.created()).isFalse();
        assertThat(result.personalInfo()).isSameAs(existing);
        assertThat(existing.getHeightCm()).isEqualByComparingTo("180.0");
        assertThat(existing.getWeightKg()).isEqualByComparingTo("75.0");

        ArgumentCaptor<PersonalInfo> captor = ArgumentCaptor.forClass(PersonalInfo.class);
        verify(personalInfoRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(existing);
    }

    private PersonalInfoUpsertRequest request(BigDecimal heightCm, BigDecimal weightKg) {
        return new PersonalInfoUpsertRequest(
                heightCm,
                weightKg,
                Gender.MALE,
                LocalDate.of(1995, 4, 12),
                ActivityLevel.MODERATELY_ACTIVE
        );
    }

    private PersonalInfo personalInfoFor(AppUser user) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setId(100L);
        personalInfo.setUser(user);
        personalInfo.setHeightCm(new BigDecimal("172.5"));
        personalInfo.setWeightKg(new BigDecimal("68.4"));
        personalInfo.setGender(Gender.MALE);
        personalInfo.setDateOfBirth(LocalDate.of(1995, 4, 12));
        personalInfo.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        return personalInfo;
    }

    private AppUser user(Long id) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setUsername("Dadao");
        user.setEmail("dadao@example.com");
        user.setPasswordHash("{bcrypt}hash");
        return user;
    }
}
