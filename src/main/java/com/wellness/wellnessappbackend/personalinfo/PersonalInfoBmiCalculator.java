package com.wellness.wellnessappbackend.personalinfo;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

@Component
public class PersonalInfoBmiCalculator {

    public BigDecimal calculate(BigDecimal heightCm, BigDecimal weightKg) {
        BigDecimal heightMeters = heightCm.divide(BigDecimal.valueOf(100));
        BigDecimal heightMetersSquared = heightMeters.multiply(heightMeters);
        return weightKg.divide(heightMetersSquared, 1, RoundingMode.HALF_UP);
    }
}
