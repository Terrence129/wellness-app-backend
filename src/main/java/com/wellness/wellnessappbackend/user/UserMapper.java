package com.wellness.wellnessappbackend.user;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Component
public class UserMapper {

    public UserDto toDto(AppUser user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
