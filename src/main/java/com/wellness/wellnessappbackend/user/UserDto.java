package com.wellness.wellnessappbackend.user;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record UserDto(
        Long id,
        String username,
        String email
) {
}
