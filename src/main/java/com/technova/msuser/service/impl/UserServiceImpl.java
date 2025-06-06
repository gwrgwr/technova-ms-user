package com.technova.msuser.service.impl;

import com.technova.Result;
import com.technova.exceptions.BaseException;
import com.technova.msuser.domain.UserEntity;
import com.technova.msuser.mapper.UserMapper;
import com.technova.msuser.repository.UserRepository;
import com.technova.msuser.service.UserService;
import com.technova.user.constants.RabbitUserConstants;
import com.technova.user.dto.*;
import com.technova.user.enums.UserStatus;
import com.technova.user.exceptions.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserByPhoneNumber(PhoneNumber phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    @RabbitListener(queues = RabbitUserConstants.USER_SAVE_REQUEST_QUEUE)
    public Result<?> save(UserCreateDTO userDTO) {
        if (findUserByCpf(userDTO.getCpf()) != null) {
            return Result.error(new UserCPFAlreadyExistsException());
        }
        if (findUserByEmail(userDTO.getEmail()) != null) {
            return Result.error(new UserEmailAlreadyExistsException());
        }
        if (findUserByUsername(userDTO.getUsername()) != null) {
            return Result.error(new UserUsernameAlreadyExists());
        }
        if (findUserByPhoneNumber(userDTO.getPhoneNumber()) != null) {
            return Result.error(new UserPhoneNumberAlreadyExistsException());
        }
        UserEntity userEntity = UserMapper.toUserEntity(userDTO);
        userRepository.save(userEntity);
        return Result.success(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserByCredential(String credential) {

        UserEntity user = null;

        if (credential.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$\n")) {
            return user = findUserByCpf(credential);
        } else if (credential.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return user = findUserByEmail(credential);
        } else if (credential.matches("^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{2,3}\\)?[-.\\s]?\\d{4,5}[-.\\s]?\\d{4}$")) {
            return user = findUserByPhoneNumber(new PhoneNumber(credential));
        }
        return null;
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_LOGIN_REQUEST_QUEUE)
    public Result<UserResponseDTO> loginUser(String credential) {

        UserEntity user = getUserByCredential(credential);

        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException());
    }

    @Override
    @Transactional(readOnly = true)
    @RabbitListener(queues = RabbitUserConstants.USER_FIND_BY_ID_REQUEST_QUEUE)
    public Result<UserResponseDTO> findById(String id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException());
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_DELETE_REQUEST_QUEUE)
    public void deleteUser(String id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.SUSPENDED);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_SOFT_DELETE_REQUEST_QUEUE)
    public void softDeleteUser(String id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_UPDATE_REQUEST_QUEUE)
    public Result<UserResponseDTO> updateUser(UserUpdateDTO userDTO) {
        UserEntity user = this.findUserById(userDTO.getId());
        if (user != null) {
            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
                this.userRepository.save(user);
                return Result.success(UserMapper.toUserResponseDTO(user));
            }
            if (userDTO.getPassword() != null) {
                if (!Objects.equals(userDTO.getPassword(), user.getPassword())) {
                    user.setPassword(userDTO.getPassword());
                    this.userRepository.save(user);
                    return Result.success(UserMapper.toUserResponseDTO(user));
                }
                return Result.error(new PasswordAlreadyRegisteredException());
            }
            if (userDTO.getAddress() != null) {
                user.setAddress(userDTO.getAddress());
                this.userRepository.save(user);
                return Result.success(UserMapper.toUserResponseDTO(user));
            }
            if (userDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(userDTO.getPhoneNumber());
                this.userRepository.save(user);
                return Result.success(UserMapper.toUserResponseDTO(user));
            }
            return Result.error(new BaseException());
        }
        return Result.error(new UserNotFoundException());
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_CONFIRM_EMAIL_QUEUE)
    public void updateUserApprovalStatus(UserConfirmEmailDTO userCreateDTO) {
        UserEntity user = this.findUserByEmail(userCreateDTO.getEmail());
        if (user != null) {
            user.setApproved(userCreateDTO.getApproved());
            this.userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitUserConstants.USER_ACTIVE_REQUEST_QUEUE)
    public Result<UserResponseDTO> activeUser(String id) {
        UserEntity user = this.findUserById(id);
        if (user != null) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                return Result.error(null);
            }
            user.setStatus(UserStatus.ACTIVE);
            this.userRepository.save(user);
            return Result.success(UserMapper.toUserResponseDTO(user));
        }
        return Result.error(new UserNotFoundException());
    }
}
