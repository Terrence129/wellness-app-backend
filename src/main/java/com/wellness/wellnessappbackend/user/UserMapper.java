package com.wellness.wellnessappbackend.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(AppUser user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
