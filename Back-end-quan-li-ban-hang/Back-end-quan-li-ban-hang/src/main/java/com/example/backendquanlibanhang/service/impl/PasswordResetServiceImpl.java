package com.example.backendquanlibanhang.service.impl;

import com.example.backendquanlibanhang.model.PasswordReset;
import com.example.backendquanlibanhang.model.User;
import com.example.backendquanlibanhang.repository.IPasswordResetRepository;
import com.example.backendquanlibanhang.repository.IUserRepository;
import com.example.backendquanlibanhang.service.IService.IPasswordResetService;
import com.example.backendquanlibanhang.util.EmailUtil;
import com.example.backendquanlibanhang.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PasswordResetServiceImpl implements IPasswordResetService {
    @Autowired
    private IPasswordResetRepository passwordResetRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    public String verifyAccount(String otp) {
        PasswordReset passwordReset = passwordResetRepository.findByOtp(otp)
                .orElseThrow(() -> new RuntimeException("OTP không hợp lệ"));

        if (Duration.between(passwordReset.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (2 * 60)) {
            passwordReset.setVerified(true);
            passwordResetRepository.save(passwordReset);
            return "OTP đã được xác nhận. Bạn có thể đặt lại mật khẩu.";
        }
        return "Vui lòng tạo mã OTP mới và thử lại.";
    }

    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email này:" + email));

        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi mã OTP. Vui lòng thử lại.");
        }

        PasswordReset passwordReset = passwordResetRepository.findByUser(user).orElse(new PasswordReset());
        passwordReset.setOtp(otp);
        passwordReset.setOtpGeneratedTime(LocalDateTime.now());
        passwordReset.setVerified(false);
        passwordReset.setUser(user);
        passwordResetRepository.save(passwordReset);

        return "Email đã được gửi. Vui lòng xác nhận tài khoản trong vòng 2 phút.";
    }

    @Override
    public String resetPassword(String email, String newPassword) {
        PasswordReset passwordReset = passwordResetRepository.findByUserAndVerifiedTrue(userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Email không hợp lệ")))
                .orElseThrow(() -> new RuntimeException("OTP chưa được xác nhận hoặc đã hết hạn"));

        User user = passwordReset.getUser();
        // Kiểm tra tính hợp lệ của mật khẩu mới
        if (!Pattern.matches(PASSWORD_REGEX, newPassword)) {
            throw new RuntimeException("Mật khẩu không hợp lệ. Mật khẩu phải chứa ít nhất một chữ cái viết hoa, " +
                    "một chữ cái viết thường, một chữ số và một ký tự đặc biệt. Chiều dài mật khẩu phải ít nhất là 8 ký tự.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
        return "Đặt lại mật khẩu thành công.";
    }

    @Override
    public Optional<PasswordReset> findByOtp(String otp) {
        return passwordResetRepository.findByOtp(otp);
    }
}
