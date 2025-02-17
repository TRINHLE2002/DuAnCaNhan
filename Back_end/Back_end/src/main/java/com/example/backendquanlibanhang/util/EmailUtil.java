package com.example.backendquanlibanhang.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Mã OTP của bạn");
        mimeMessageHelper.setText(String.format("""
            <div>
              <p>OTP của bạn là: <b>%s</b></p>
              <p>Vui lòng sử dụng OTP này để xác minh tài khoản của bạn. OTP có giá trị trong 2 phút.</p>
            </div>
            """, otp), true);
        javaMailSender.send(mimeMessage);
    }
}
