package com.example.toy_project.repository;

import com.example.toy_project.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  Optional<RefreshToken> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

  void deleteByRefreshToken(String refreshToken);
}
