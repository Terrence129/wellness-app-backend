package com.wellness.wellnessappbackend.wellness;

import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.common.PageResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogCreateRequest;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogDto;
import com.wellness.wellnessappbackend.wellness.dto.WellnessLogUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/wellness-logs")
@RequiredArgsConstructor
public class WellnessLogController {

    private final WellnessLogService wellnessLogService;
    private final WellnessLogMapper wellnessLogMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<WellnessLogDto>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WellnessLogCreateRequest request
    ) {
        WellnessLogDto data = wellnessLogMapper.toDto(wellnessLogService.create(principal.getId(), request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Wellness log created successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WellnessLogDto>>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "logDate,desc") String sort
    ) {
        Sort springSort = WellnessLogSort.parse(sort);
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), springSort);
        List<String> sortDescription = WellnessLogSort.describe(springSort);
        PageResponse<WellnessLogDto> data = PageResponse.from(
                wellnessLogService.list(principal.getId(), startDate, endDate, pageable).map(wellnessLogMapper::toDto),
                sortDescription
        );
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", data));
    }

    @GetMapping("/date/{logDate}")
    public ResponseEntity<ApiResponse<WellnessLogDto>> getByDate(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate logDate
    ) {
        WellnessLogDto data = wellnessLogMapper.toDto(wellnessLogService.getByDate(principal.getId(), logDate));
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WellnessLogDto>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody WellnessLogUpdateRequest request
    ) {
        WellnessLogDto data = wellnessLogMapper.toDto(wellnessLogService.update(principal.getId(), id, request));
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Wellness log updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        wellnessLogService.delete(principal.getId(), id);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Wellness log deleted successfully", null));
    }
}
