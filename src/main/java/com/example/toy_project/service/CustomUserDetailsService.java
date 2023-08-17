package com.example.toy_project.service;

import com.example.toy_project.entity.User;
import com.example.toy_project.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findOneWithAuthoritiesByUsername(username)
        .map(user -> createUser(username, user))
        .orElseThrow(()-> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
  }

  private org.springframework.security.core.userdetails.User createUser(String username, User user){
    if(!user.isActivated()){
      throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
    }
    var grantedAuthorities = user.getAuthorities().stream()
        .map(authority -> new org.springframework.security.core.authority.SimpleGrantedAuthority(authority.getAuthorityName()))
        .toList();
    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),
        grantedAuthorities);
  }
}
