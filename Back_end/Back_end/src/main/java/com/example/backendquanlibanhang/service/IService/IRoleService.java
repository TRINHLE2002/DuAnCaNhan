package com.example.backendquanlibanhang.service.IService;

import com.example.backendquanlibanhang.model.Role;
import com.example.backendquanlibanhang.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
