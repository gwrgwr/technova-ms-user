package com.technova.msuser.service;

import com.technova.dto.UserResponseDTO;
import com.technova.msuser.domain.UserEntity;
import com.technova.msuser.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
