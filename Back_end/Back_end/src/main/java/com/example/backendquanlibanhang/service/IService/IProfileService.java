package com.example.backendquanlibanhang.service.IService;

import com.example.backendquanlibanhang.model.Profile;

public interface IProfileService {
    Profile findByUserId(Long userId);
    Profile save(Profile profile);
}
