package com.technova.msuser.repository;

import com.technova.msuser.domain.UserEntity;
import com.technova.user.dto.PhoneNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    UserEntity findByPhoneNumber(PhoneNumber phoneNumber);
    UserEntity findByUsername(String username);
    UserEntity findByCpf(String cpf);
}
