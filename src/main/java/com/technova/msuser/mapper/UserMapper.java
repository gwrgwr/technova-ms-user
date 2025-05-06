package com.technova.msuser.mapper;


import com.technova.msuser.domain.UserEntity;
import com.technova.user.dto.UserCreateDTO;
import com.technova.user.dto.UserResponseDTO;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(UserEntity user) {
        return new UserResponseDTO(user.getId().toHexString(), user.getEmail(), user.getPassword(), user.getRole(), user.getAddress(), user.getPhoneNumber());
    }

    public static UserEntity toUserEntity(UserCreateDTO dto) {
        return new UserEntity(dto.getName(), dto.getEmail(), dto.getCpf(), dto.getUsername(), dto.getPassword(), dto.getPhoneNumber(), dto.getAddress());
    }
}
