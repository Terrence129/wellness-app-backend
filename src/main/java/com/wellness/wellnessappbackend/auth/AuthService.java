package com.wellness.wellnessappbackend.auth;

import com.wellness.wellnessappbackend.exception.ApiException;
import com.wellness.wellnessappbackend.exception.ErrorCode;
import com.wellness.wellnessappbackend.security.JwtService;
import com.wellness.wellnessappbackend.security.UserPrincipal;
import com.wellness.wellnessappbackend.user.AppUser;
import com.wellness.wellnessappbackend.user.UserDto;
import com.wellness.wellnessappbackend.user.UserMapper;
import com.wellness.wellnessappbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserDto register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginData login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizeEmail(request.email()), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        AppUser user = userRepository.getReferenceById(principal.getId());
        JwtToken jwt = jwtService.generateToken(user);

        return new LoginData(jwt.token(), "Bearer", jwt.expiresAt(), userMapper.toDto(user));
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
