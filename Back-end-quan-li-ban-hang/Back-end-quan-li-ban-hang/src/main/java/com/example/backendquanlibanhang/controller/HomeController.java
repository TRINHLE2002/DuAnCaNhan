package com.example.backendquanlibanhang.controller;

import com.example.backendquanlibanhang.dto.request.UserProfileDTO;
import com.example.backendquanlibanhang.model.Profile;
import com.example.backendquanlibanhang.model.User;
import com.example.backendquanlibanhang.service.IService.IProfileService;
import com.example.backendquanlibanhang.service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
@RestController
@RequestMapping("/api/home")
@CrossOrigin
public class HomeController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IProfileService profileService;

    @GetMapping("user/dashboard")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> getUserDashboard() {
        return ResponseEntity.ok("Welcome to User Dashboard!");
    }

    @GetMapping("admin/dashboard")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard!");
    }

    @GetMapping("me")
    public ResponseEntity<UserProfileDTO> getCurrentUser(Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserProfileDTO userProfileDTO = new UserProfileDTO(user);
            return ResponseEntity.ok(userProfileDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> ResponseEntity.ok(new UserProfileDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO userProfileDTO) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        user.setName(userProfileDTO.getName());
        user.setEmail(userProfileDTO.getEmail());
        user.setAvatar(userProfileDTO.getAvatar());

        Profile profile = profileService.findByUserId(id);
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }
        profile.setBirthDate(userProfileDTO.getBirthDate());
        profile.setPhoneNumber(userProfileDTO.getPhoneNumber());
        profile.setAddress(userProfileDTO.getAddress());
        profile.setGender(userProfileDTO.getGender());
    
        user.setProfile(profile);
        profileService.save(profile);

        userService.save(user);

        return ResponseEntity.ok(new UserProfileDTO(user));
    }
}
