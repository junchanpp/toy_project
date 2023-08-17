package com.example.toy_project.controller;

import com.example.toy_project.dto.UserDto;
import com.example.toy_project.entity.User;
import com.example.toy_project.service.UserService;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  @Resource
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<User> signup(
      @Valid @RequestBody UserDto userDto
  ) {
    return ResponseEntity.ok(userService.signup(userDto));
  }

  @GetMapping("")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ResponseEntity<User> getMyUserInfo() {
    return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
  }

  @GetMapping("/{username}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<User> getUserInfo(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
  }
}