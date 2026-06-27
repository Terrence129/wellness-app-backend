package com.wellness.wellnessappbackend.user;

import com.wellness.wellnessappbackend.common.ApiResponse;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.ok("Success", userService.getCurrentUser(principal.getId())));
    }
}
