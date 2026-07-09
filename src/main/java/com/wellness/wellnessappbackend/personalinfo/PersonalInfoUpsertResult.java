package com.wellness.wellnessappbackend.personalinfo;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public record PersonalInfoUpsertResult(
        PersonalInfo personalInfo,
        boolean created
) {
}
