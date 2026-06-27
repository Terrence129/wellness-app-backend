package com.wellness.wellnessappbackend.summary;

import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/wellness-summary")
@RequiredArgsConstructor
public class WellnessSummaryController {

    private final WellnessSummaryService wellnessSummaryService;

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<WeeklySummaryDto>> weekly(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", wellnessSummaryService.weeklySummary(principal.getId(), startDate, endDate)));
    }
}
