package com.wellness.wellnessappbackend.auth;

import java.time.Instant;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record JwtToken(
        String token,
        Instant expiresAt
) {
}
