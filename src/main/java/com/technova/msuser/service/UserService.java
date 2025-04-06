package com.technova.msuser.service;

import com.technova.dto.Result;
import com.technova.dto.user.UserResponseDTO;
import com.technova.exceptions.user.UserAlreadyExistsException;
import com.technova.exceptions.user.UserNotFoundException;
import com.technova.msuser.domain.UserEntity;
import com.technova.msuser.mapper.UserMapper;
import com.technova.msuser.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @RabbitListener(queues = "user-save-request")
    public Result<?> save(UserEntity user) {
        UserEntity userExists = findUserByEmail(user.getEmail());
        if (userExists != null) {
            return Result.error(new UserAlreadyExistsException("User already exists"));
        }
        user.setRole("USER");
        userRepository.save(user);
        return Result.success(null);
    }

    @RabbitListener(queues = "user-login-request")
    public Result<UserResponseDTO> getUserByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException("User not found"));
    }

    @RabbitListener(queues = "user-find_by_id-request")
    public Result<UserResponseDTO> findById(String id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException("User not found"));
    }
}
