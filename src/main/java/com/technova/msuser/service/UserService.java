package com.technova.msuser.service;

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

    @RabbitListener(queues = "user-save-request")
    public UserResponseDTO save(UserEntity user) {
        UserEntity userExists = userRepository.findByEmail(user.getEmail());
        if (userExists != null) {
            return new UserResponseDTO(false, "Email already exists");
        }
        userRepository.save(user);
        return new UserResponseDTO(true, "User saved successfully");
    }

    @RabbitListener(queues = "user-login-request")
    public UserLoginResponse getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            Map<String, String> map = new HashMap<>();
            map.put("email", user.getEmail());
            map.put("password", user.getPassword());
            return new UserLoginResponse(true, map);
        }
        return new UserLoginResponse(false, null);
    }
}
