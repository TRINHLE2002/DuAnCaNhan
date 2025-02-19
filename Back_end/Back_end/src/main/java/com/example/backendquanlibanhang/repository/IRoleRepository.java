package com.example.backendquanlibanhang.repository;

import com.example.backendquanlibanhang.model.Role;
import com.example.backendquanlibanhang.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
