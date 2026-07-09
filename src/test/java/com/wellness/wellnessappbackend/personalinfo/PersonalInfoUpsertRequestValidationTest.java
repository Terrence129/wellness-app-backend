package com.wellness.wellnessappbackend.personalinfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoUpsertRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonalInfoUpsertRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void acceptsValidRequest() {
        Set<ConstraintViolation<PersonalInfoUpsertRequest>> violations = validator.validate(validRequest());

        assertThat(violations).isEmpty();
    }

    @Test
    void rejectsInvalidHeight() {
        PersonalInfoUpsertRequest request = new PersonalInfoUpsertRequest(
                new BigDecimal("49.9"),
                new BigDecimal("68.4"),
                Gender.MALE,
                LocalDate.of(1995, 4, 12),
                ActivityLevel.MODERATELY_ACTIVE
        );

        assertThat(fieldNames(validator.validate(request))).contains("heightCm");
    }

    @Test
    void rejectsInvalidWeight() {
        PersonalInfoUpsertRequest request = new PersonalInfoUpsertRequest(
                new BigDecimal("172.5"),
                new BigDecimal("500.1"),
                Gender.MALE,
                LocalDate.of(1995, 4, 12),
                ActivityLevel.MODERATELY_ACTIVE
        );

        assertThat(fieldNames(validator.validate(request))).contains("weightKg");
    }

    @Test
    void rejectsInvalidEnumDuringJsonBinding() {
        String json = """
                {
                  "heightCm": 172.5,
                  "weightKg": 68.4,
                  "gender": "UNKNOWN",
                  "dateOfBirth": "1995-04-12",
                  "activityLevel": "MODERATELY_ACTIVE"
                }
                """;

        assertThatThrownBy(() -> objectMapper.readValue(json, PersonalInfoUpsertRequest.class))
                .isInstanceOf(JsonProcessingException.class);
    }

    @Test
    void rejectsFutureDateOfBirth() {
        PersonalInfoUpsertRequest request = new PersonalInfoUpsertRequest(
                new BigDecimal("172.5"),
                new BigDecimal("68.4"),
                Gender.MALE,
                LocalDate.now().plusDays(1),
                ActivityLevel.MODERATELY_ACTIVE
        );

        assertThat(fieldNames(validator.validate(request))).contains("dateOfBirth");
    }

    private PersonalInfoUpsertRequest validRequest() {
        return new PersonalInfoUpsertRequest(
                new BigDecimal("172.5"),
                new BigDecimal("68.4"),
                Gender.MALE,
                LocalDate.of(1995, 4, 12),
                ActivityLevel.MODERATELY_ACTIVE
        );
    }

    private Set<String> fieldNames(Set<ConstraintViolation<PersonalInfoUpsertRequest>> violations) {
        return violations.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
    }
}
