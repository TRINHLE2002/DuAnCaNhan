package com.example.backendquanlibanhang.service.IService;

import com.example.backendquanlibanhang.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String name); //Tim kiem User co ton tai trong DB khong?
    Boolean existsByUsername(String username); //username da co trong DB chua, khi tao du lieu
    Boolean existsByEmail(String email); //email da co trong DB chua
    User save(User user);
    Optional<User> findById(Long id);
}
