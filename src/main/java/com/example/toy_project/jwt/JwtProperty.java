package com.example.toy_project.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperty {

  private String accessTokenSecret;
  private Long accessTokenExpiredSeconds;
  private String refreshTokenSecret;
  private Long refreshTokenExpiredSeconds;

}
