package com.example.toy_project.config.filter;

import com.example.toy_project.common.codes.AuthConstants;
import com.example.toy_project.common.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    List<String> notUseTokenlist = Arrays.asList(
        "/api/v1/user/login",
        "/api/v1/test/generateToken",
        "api/v1/code/codeList"
    );

    if(notUseTokenlist.contains(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
      filterChain.doFilter(request, response);
      return;
    }

    String header = request.getHeader(AuthConstants.AUTH_HEADER);
    logger.debug("[+] header check : " + header);

    try{
      if(header == null || header.equalsIgnoreCase("")){
        throw new RuntimeException("[-] header is null or empty");
      }
      String token = TokenUtils.getTokenFromHeader(header);
    }catch (Exception e) {
      logger.error("[-] header check error : " + e.getMessage());
    }
  }
}
