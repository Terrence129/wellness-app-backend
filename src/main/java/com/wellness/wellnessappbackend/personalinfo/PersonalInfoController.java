package com.wellness.wellnessappbackend.personalinfo;

import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoDto;
import com.wellness.wellnessappbackend.personalinfo.dto.PersonalInfoUpsertRequest;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

@RestController
@RequestMapping("/api/users/me/personal-info")
@RequiredArgsConstructor
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;
    private final PersonalInfoMapper personalInfoMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PersonalInfoDto>> get(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", personalInfoMapper.toDto(personalInfoService.getCurrent(principal.getId()))));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PersonalInfoDto>> upsert(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PersonalInfoUpsertRequest request
    ) {
        PersonalInfoUpsertResult result = personalInfoService.upsert(principal.getId(), request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        String message = result.created()
                ? "Personal info created successfully"
                : "Personal info updated successfully";

        return ResponseEntity
                .status(status)
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok(message, personalInfoMapper.toDto(result.personalInfo())));
    }
}
