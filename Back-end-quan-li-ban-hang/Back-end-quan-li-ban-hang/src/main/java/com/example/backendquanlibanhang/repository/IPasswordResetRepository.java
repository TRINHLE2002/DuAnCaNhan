package com.example.backendquanlibanhang.repository;

import com.example.backendquanlibanhang.model.PasswordReset;
import com.example.backendquanlibanhang.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByUser(User user);
    Optional<PasswordReset> findByOtp(String otp);
    Optional<PasswordReset> findByUserAndVerifiedTrue(User user);

}
