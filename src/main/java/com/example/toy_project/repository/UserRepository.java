package com.example.toy_project.repository;

import com.example.toy_project.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = "authorities")
  Optional<User> findOneWithAuthoritiesByUsername(String username);

}
