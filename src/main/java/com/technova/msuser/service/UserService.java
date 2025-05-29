package com.technova.msuser.service;


import com.technova.Result;
import com.technova.msuser.domain.UserEntity;
import com.technova.user.dto.*;

public interface UserService {
    UserEntity findUserByEmail(String email);

    UserEntity findUserById(String id);

    UserEntity findUserByUsername(String username);

    UserEntity findUserByCpf(String cpf);

    UserEntity findUserByPhoneNumber(PhoneNumber phoneNumber);

    Result<?> save(UserCreateDTO dto);

    UserEntity getUserByCredential(String credential);

    Result<UserResponseDTO> loginUser(String credential);

    Result<UserResponseDTO> findById(String id);

    void deleteUser(String id);

    void softDeleteUser(String id);

    Result<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO);

    void updateUserApprovalStatus(UserConfirmEmailDTO dto);

    Result<UserResponseDTO> activeUser(String id);
}
