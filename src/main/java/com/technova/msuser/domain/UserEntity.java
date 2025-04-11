package com.technova.msuser.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.technova.user.dto.Address;
import com.technova.user.dto.PhoneNumber;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class UserEntity {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String name;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String cpf;

    private String password;

    private String role = "USER";

    private PhoneNumber phoneNumber;

    @Indexed(unique = true)
    private Address address;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public UserEntity(ObjectId id, String name, String email, String password, String role, PhoneNumber phoneNumber, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserEntity(String name, String email, String password, PhoneNumber phoneNumber, Address address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "USER";
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserEntity() {
    }
}
