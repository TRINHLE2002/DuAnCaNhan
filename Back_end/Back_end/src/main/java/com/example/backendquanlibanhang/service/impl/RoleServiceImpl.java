package com.example.backendquanlibanhang.service.impl;

import com.example.backendquanlibanhang.model.Role;
import com.example.backendquanlibanhang.model.RoleName;
import com.example.backendquanlibanhang.repository.IRoleRepository;
import com.example.backendquanlibanhang.service.IService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
