package com.wellness.wellnessappbackend.personalinfo;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoUpsertRequest;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

@Service
@RequiredArgsConstructor
public class PersonalInfoService {

    private final PersonalInfoRepository personalInfoRepository;
    private final UserRepository userRepository;
    private final PersonalInfoMapper personalInfoMapper;

    @Transactional(readOnly = true)
    public PersonalInfo getCurrent(Long userId) {
        return personalInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Personal info not found"
                ));
    }

    @Transactional
    public PersonalInfoUpsertResult upsert(Long userId, PersonalInfoUpsertRequest request) {
        return personalInfoRepository.findByUserId(userId)
                .map(existing -> updateExisting(existing, request))
                .orElseGet(() -> createNew(userId, request));
    }

    private PersonalInfoUpsertResult updateExisting(PersonalInfo personalInfo, PersonalInfoUpsertRequest request) {
        personalInfoMapper.applyRequest(personalInfo, request);
        return new PersonalInfoUpsertResult(personalInfoRepository.save(personalInfo), false);
    }

    private PersonalInfoUpsertResult createNew(Long userId, PersonalInfoUpsertRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setUser(user);
        personalInfoMapper.applyRequest(personalInfo, request);
        return new PersonalInfoUpsertResult(personalInfoRepository.save(personalInfo), true);
    }
}
