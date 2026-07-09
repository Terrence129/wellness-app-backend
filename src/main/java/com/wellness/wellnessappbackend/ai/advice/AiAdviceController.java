package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.ai.advice.dto.AiAdviceDto;
import com.wellness.wellnessappbackend.ai.advice.dto.GenerateAiAdviceRequest;
import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.common.PageResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AiAdviceDto>>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Sort springSort = AiAdviceSort.parse(sort);
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), springSort);
        List<String> sortDescription = AiAdviceSort.describe(springSort);
        PageResponse<AiAdviceDto> data = PageResponse.from(
                aiAdviceService.list(principal.getId(), startDate, endDate, pageable).map(aiAdviceMapper::toDto),
                sortDescription
        );
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AiAdviceDto>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", aiAdviceMapper.toDto(aiAdviceService.getById(principal.getId(), id))));
    }
}
