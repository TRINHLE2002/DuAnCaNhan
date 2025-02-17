package com.example.backendquanlibanhang.service.impl;

import com.example.backendquanlibanhang.model.Profile;
import com.example.backendquanlibanhang.repository.IProfileRepository;
import com.example.backendquanlibanhang.service.IService.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService {
    @Autowired
    private IProfileRepository profileRepository;
    @Override
    public Profile findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }
}
