package com.example.toy_project.jwt;


import com.example.toy_project.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

  private static final String AUTHORITIES_KEY = "auth";
  private final String accessTokenSecret;
  private final String refreshTokenSecret;
  private final Long accessTokenExpiredMilliseconds;
  private final Long refreshTokenExpiredMilliseconds;
  private Key accessKey;
  private Key refreshKey;

  public TokenProvider(JwtProperty jwtProperty) {
    this.accessTokenSecret = jwtProperty.getAccessTokenSecret();
    this.refreshTokenSecret = jwtProperty.getRefreshTokenSecret();
    this.accessTokenExpiredMilliseconds = 1000L * jwtProperty.getAccessTokenExpiredSeconds();
    this.refreshTokenExpiredMilliseconds =
        1000L * jwtProperty.getRefreshTokenExpiredSeconds();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    byte[] accessKeyBytes = Decoders.BASE64.decode(accessTokenSecret);
    this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
    byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshTokenSecret);
    this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
  }

  public TokenDto createTokenDto(Authentication authentication) {
    String accessToken = createAccessToken(authentication);
    String refreshToken = createRefreshToken();

    return TokenDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public String createAccessToken(Authentication authentication) {
    String authorities = authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.accessTokenExpiredMilliseconds);

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(accessKey, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact();
  }

  public String createRefreshToken() {

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.refreshTokenExpiredMilliseconds);

    return Jwts.builder()
        .signWith(refreshKey, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(accessKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateAccessToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    }

    return false;
  }

  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    }

    return false;
  }
}
