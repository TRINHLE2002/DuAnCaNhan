package com.example.backendquanlibanhang.controller;


import com.example.backendquanlibanhang.dto.request.SignInForm;
import com.example.backendquanlibanhang.dto.request.SignUpForm;
import com.example.backendquanlibanhang.dto.response.JwtResponse;
import com.example.backendquanlibanhang.dto.response.ResponMessage;
import com.example.backendquanlibanhang.model.PasswordReset;
import com.example.backendquanlibanhang.model.Role;
import com.example.backendquanlibanhang.model.RoleName;
import com.example.backendquanlibanhang.model.User;
import com.example.backendquanlibanhang.security.jwt.JwtProvider;
import com.example.backendquanlibanhang.security.userprincal.UserPrinciple;
import com.example.backendquanlibanhang.service.IService.IPasswordResetService;
import com.example.backendquanlibanhang.service.impl.RoleServiceImpl;
import com.example.backendquanlibanhang.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequestMapping("/api/auth")
@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IPasswordResetService passwordResetService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponMessage("Người dùng đã tồn tại"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponMessage("Email đã tồn tại"), HttpStatus.OK);
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(
                            ()-> new RuntimeException("Role not found")
                    );
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponMessage("yes"), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.createToken(authentication);
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAuthorities()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponMessage("Tên người dùng hoặc mật khẩu không đúng"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponMessage("Đã xảy ra lỗi. Vui lòng thử lại sau"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String otp) {
        try {
            String response = passwordResetService.verifyAccount(otp);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        if (!userService.existsByEmail(email)) {
            return new ResponseEntity<>("Email không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
        }
        try {
            String response = passwordResetService.regenerateOtp(email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                    @RequestParam("newPassword") String newPassword,
                                                @RequestParam("otp") String otp) {
        try {
            PasswordReset passwordReset = passwordResetService.findByOtp(otp)
                    .orElseThrow(() -> new RuntimeException("OTP không hợp lệ"));

            if (!passwordReset.isVerified()) {
                return new ResponseEntity<>("Mã OTP chưa được xác nhận. Vui lòng xác nhận mã OTP trước khi đặt lại mật khẩu.", HttpStatus.BAD_REQUEST);
            }
            String response = passwordResetService.resetPassword(email, newPassword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(new HashMap<String, Boolean>() {{
            put("exists", exists);
        }});
    }
}

