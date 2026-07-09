package com.wellness.wellnessappbackend.user;

import com.wellness.wellnessappbackend.personalinfo.PersonalInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Component
public class UserMapper {

    public UserDto toDto(AppUser user) {
        return toDto(user, null);
    }

    public UserDto toDto(AppUser user, PersonalInfo personalInfo) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                personalInfo == null ? null : personalInfo.getHeightCm(),
                personalInfo == null ? null : personalInfo.getWeightKg(),
                calculateAge(personalInfo)
        );
    }

    private Integer calculateAge(PersonalInfo personalInfo) {
        if (personalInfo == null || personalInfo.getDateOfBirth() == null) {
            return null;
        }
        return Period.between(personalInfo.getDateOfBirth(), LocalDate.now()).getYears();
    }
}
