package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.ai.advice.dto.AiAdviceDto;
import com.wellness.wellnessappbackend.ai.advice.dto.GenerateAiAdviceRequest;
import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/ai/advice")
@RequiredArgsConstructor
public class AiAdviceController {

    private final AiAdviceService aiAdviceService;
    private final AiAdviceMapper aiAdviceMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<AiAdviceDto>> generate(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody GenerateAiAdviceRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok(
                        "AI advice generated successfully",
                        aiAdviceMapper.toDto(aiAdviceService.generate(principal.getId(), request))
                ));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<AiAdviceDto>> latest(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", aiAdviceMapper.toDto(aiAdviceService.latest(principal.getId()))));
    }
}
