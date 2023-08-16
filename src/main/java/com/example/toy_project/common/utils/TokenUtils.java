package com.example.toy_project.common.utils;

import com.example.toy_project.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TokenUtils {
  private static final String jwtSecretKey = "exampleSecretKey";

  public static String generateJwtToken(UserDto userDto) {
    JwtBuilder builder = Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(userDto))
        .setSubject(String.valueOf(userDto.getUserId()))
        .signWith(SignatureAlgorithm.HS256, createSignature())
        .setExpiration(createExpireDate());
    return builder.compact();
  }

  public static String parseTokenToUserInfo(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecretKey)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
  public static boolean isValidToken(String token) {
    try {
      Claims claims = getClaimsFormToken(token);
      log.info("expireTime :" + claims.getExpiration());
      log.info("userId :" + claims.get("userId"));
      log.info("userNm :" + claims.get("userNm"));
      return true;
    } catch (ExpiredJwtException e){
      log.error("토큰 만료");
      return false;
    } catch (JwtException e){
      log.error("토큰 변조");
      return false;
    } catch (NullPointerException e){
      log.error("토큰 없음");
      return false;
    }
  }
  public static String getTokenFromHeader(String header){
    return header.split(" ")[1];
  }

  private static Date createExpireDate() {
    return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
  }

  //create header
  private static Map<String, Object> createHeader() {
    return Map.of("typ", "JWT", "alg", "HS256", "regDate", System.currentTimeMillis());
  }

  //create claims
  private static Map<String, Object> createClaims(UserDto userDto) {
    return Map.of("userId", userDto.getUserId(), "userNm", userDto.getUserNm());
  }

  //create signature
  private static Key createSignature() {
    byte[] apiKeySecretBytes = Base64.getDecoder().decode(jwtSecretKey);
    return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
  }

  private static Claims getClaimsFormToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecretKey)
        .parseClaimsJws(token)
        .getBody();
  }

  private static String getUserIdFromToken(String token) {
    return getClaimsFormToken(token).get("userId").toString();
  }
}
