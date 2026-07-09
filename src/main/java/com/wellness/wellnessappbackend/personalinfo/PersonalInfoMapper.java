package com.wellness.wellnessappbackend.personalinfo;

import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoDto;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

@Component
@RequiredArgsConstructor
public class PersonalInfoMapper {

    private final PersonalInfoBmiCalculator bmiCalculator;

    public PersonalInfoDto toDto(PersonalInfo personalInfo) {
        return new PersonalInfoDto(
                personalInfo.getId(),
                personalInfo.getHeightCm(),
                personalInfo.getWeightKg(),
                personalInfo.getGender(),
                personalInfo.getDateOfBirth(),
                personalInfo.getActivityLevel(),
                bmiCalculator.calculate(personalInfo.getHeightCm(), personalInfo.getWeightKg()),
                personalInfo.getCreatedAt(),
                personalInfo.getUpdatedAt()
        );
    }

    public void applyRequest(PersonalInfo personalInfo, PersonalInfoUpsertRequest request) {
        personalInfo.setHeightCm(request.heightCm());
        personalInfo.setWeightKg(request.weightKg());
        personalInfo.setGender(request.gender());
        personalInfo.setDateOfBirth(request.dateOfBirth());
        personalInfo.setActivityLevel(request.activityLevel());
    }
}
