package com.example.backendquanlibanhang.service.IService;

import com.example.backendquanlibanhang.model.PasswordReset;

import java.util.Optional;

public interface IPasswordResetService {
    String verifyAccount(String otp);
    String regenerateOtp(String email);
    String resetPassword(String otp,String newPassword);
    Optional<PasswordReset> findByOtp(String otp);
}
