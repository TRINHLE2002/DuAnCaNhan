package com.example.backendquanlibanhang.dto.request;

import com.example.backendquanlibanhang.model.User;

import java.util.Date;

public class UserProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private Date birthDate;
    private String phoneNumber;
    private String address;
    private String gender;

    public UserProfileDTO() {
    }

    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        if (user.getProfile() != null) {
            this.birthDate = user.getProfile().getBirthDate();
            this.phoneNumber = user.getProfile().getPhoneNumber();
            this.address = user.getProfile().getAddress();
            this.gender = user.getProfile().getGender();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

