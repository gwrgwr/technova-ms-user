package com.technova.msuser.service;


import com.technova.Result;
import com.technova.msuser.domain.UserEntity;
import com.technova.user.dto.UserConfirmEmailDTO;
import com.technova.user.dto.UserCreateDTO;
import com.technova.user.dto.UserResponseDTO;
import com.technova.user.dto.UserUpdateDTO;

public interface UserService {
    UserEntity findUserByEmail(String email);

    UserEntity findUserById(String id);

    UserEntity findUserByUsername(String username);

    UserEntity findUserByCpf(String cpf);

    Result<?> save(UserCreateDTO dto);

    Result<UserResponseDTO> getUserByCredential(String credential);

    Result<UserResponseDTO> findById(String id);

    void deleteUser(String id);

    Result<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO);

    void updateUserApprovalStatus(UserConfirmEmailDTO dto);
}
