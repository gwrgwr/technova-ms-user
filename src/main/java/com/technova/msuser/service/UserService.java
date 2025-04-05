package com.technova.msuser.service;

import com.technova.dto.user.UserLoginRequest;
import com.technova.dto.user.UserLoginResponse;
import com.technova.dto.user.UserResponseDTO;
import com.technova.msuser.domain.UserEntity;
import com.technova.msuser.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @RabbitListener(queues = "user-save-request")
    public UserResponseDTO save(UserEntity user) {
        UserEntity userExists = findUserByEmail(user.getEmail());
        if (userExists != null) {
            return new UserResponseDTO(false, "Email already exists");
        }
        user.setRole("USER");
        userRepository.save(user);
        return new UserResponseDTO(true, "User saved successfully");
    }

    @RabbitListener(queues = "user-login-request")
    public UserLoginResponse getUserByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        if (user != null) {
            return new UserLoginResponse(true, user.getEmail(), user.getPassword(), user.getRole());
        }
        return new UserLoginResponse(false, null, null, null);
    }
}
