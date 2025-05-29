package com.technova.msuser.domain;

import com.technova.user.dto.Address;
import com.technova.user.dto.PhoneNumber;
import com.technova.user.enums.UserStatus;
import jakarta.persistence.*;


@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private Boolean isApproved = false;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String password;

    private String role = "USER";

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Embedded
    @Column(unique = true, nullable = false)
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public UserEntity(String name, String email, String cpf, String username, String password, PhoneNumber phoneNumber, Address address) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.username = username;
        this.password = password;
        this.status = UserStatus.ACTIVE;
        this.role = "USER";
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserEntity() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
