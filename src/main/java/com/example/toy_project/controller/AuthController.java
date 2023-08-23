package com.example.toy_project.controller;

import com.example.toy_project.dto.LoginDto;
import com.example.toy_project.dto.TokenDto;
import com.example.toy_project.entity.RefreshToken;
import com.example.toy_project.jwt.JwtFilter;
import com.example.toy_project.jwt.TokenProvider;
import com.example.toy_project.repository.RefreshTokenRepository;
import com.example.toy_project.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserService userService;

  @PostMapping("signIn")
  public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LoginDto loginDto) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    var user = userService.getMyUserWithAuthorities().get();

    var tokenDto = tokenProvider.createTokenDto(authentication, user.getUserId());

    refreshTokenRepository.save(
        RefreshToken.builder()
            .userId(user.getUserId())//임시
            .refreshToken(tokenDto.getRefreshToken())
            .build());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

    return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
  }

  @PostMapping("refresh")
  public ResponseEntity<TokenDto> refresh(@Valid @RequestBody TokenDto tokenDto) {

    if (!tokenProvider.validateRefreshToken(tokenDto.getRefreshToken())) {
      throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
    }

    Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    var user = userService.getMyUserWithAuthorities().get();

    var newTokenDto = tokenProvider.createTokenDto(authentication, user.getUserId());

    refreshTokenRepository.save(
        RefreshToken.builder()
            .userId(user.getUserId())
            .refreshToken(newTokenDto.getRefreshToken())
            .build());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + newTokenDto.getAccessToken());

    return new ResponseEntity<>(newTokenDto, httpHeaders, HttpStatus.OK);
  }
}