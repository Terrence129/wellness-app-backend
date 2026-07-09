package com.wellness.wellnessappbackend.personalinfo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PersonalInfoBmiCalculatorTest {

    private final PersonalInfoBmiCalculator calculator = new PersonalInfoBmiCalculator();

    @Test
    void calculatesBmiRoundedToOneDecimal() {
        BigDecimal bmi = calculator.calculate(new BigDecimal("172.5"), new BigDecimal("68.4"));

        assertThat(bmi).isEqualByComparingTo(new BigDecimal("23.0"));
        assertThat(bmi.scale()).isEqualTo(1);
    }

    @Test
    void roundsHalfUp() {
        BigDecimal bmi = calculator.calculate(new BigDecimal("200.0"), new BigDecimal("90.2"));

        assertThat(bmi).isEqualByComparingTo(new BigDecimal("22.6"));
    }
}
