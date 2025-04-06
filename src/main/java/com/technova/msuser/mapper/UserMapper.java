package com.technova.msuser.mapper;

import com.technova.dto.user.UserResponseDTO;
import com.technova.msuser.domain.UserEntity;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(UserEntity user) {
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getAddress(), user.getPhoneNumber());
    }
}
