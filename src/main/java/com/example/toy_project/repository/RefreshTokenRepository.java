package com.example.toy_project.repository;

import com.example.toy_project.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  RefreshToken findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);
}
