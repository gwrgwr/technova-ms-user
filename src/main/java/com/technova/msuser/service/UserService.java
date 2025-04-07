package com.technova.msuser.service;

import com.technova.msuser.domain.UserEntity;
import com.technova.msuser.mapper.UserMapper;
import com.technova.msuser.repository.UserRepository;
import com.technova.user.UserCreateDTO;
import com.technova.user.UserResponseDTO;
import com.technova.user.constants.RabbitUserConstants;
import com.technova.user.dto.Result;
import com.technova.user.exceptions.UserAlreadyExistsException;
import com.technova.user.exceptions.UserNotFoundException;
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

    @RabbitListener(queues = RabbitUserConstants.USER_SAVE_REQUEST_QUEUE)
    public Result<?> save(UserCreateDTO userDTO) {
        UserEntity userExists = findUserByEmail(userDTO.getEmail());
        if (userExists != null) {
            return Result.error(new UserAlreadyExistsException("User already exists"));
        }
        UserEntity userEntity = UserMapper.toUserEntity(userDTO);
        userRepository.save(userEntity);
        return Result.success(null);
    }

    @RabbitListener(queues = RabbitUserConstants.USER_LOGIN_REQUEST_QUEUE)
    public Result<UserResponseDTO> getUserByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException("User not found"));
    }

    @RabbitListener(queues = RabbitUserConstants.USER_FIND_BY_ID_REQUEST_QUEUE)
    public Result<UserResponseDTO> findById(String id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException("User not found"));
    }
}
