package com.example.backendquanlibanhang.repository;

import com.example.backendquanlibanhang.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserId(Long userId);
}
