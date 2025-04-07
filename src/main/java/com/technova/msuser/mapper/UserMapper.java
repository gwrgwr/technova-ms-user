package com.technova.msuser.mapper;


import com.technova.msuser.domain.UserEntity;
import com.technova.user.UserCreateDTO;
import com.technova.user.UserResponseDTO;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(UserEntity user) {
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getAddress(), user.getPhoneNumber());
    }

    public static UserEntity toUserEntity(com.technova.user.UserCreateDTO dto) {
        return new UserEntity(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getPhoneNumber(), dto.getAddress());
    }
}
