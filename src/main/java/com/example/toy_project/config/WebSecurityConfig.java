package com.example.toy_project.config;

import com.example.toy_project.config.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  //1. 정적 자원에 대해서 인증된 사용자가 정적 자원의 접근에 대해 '인가'에 대한 설정을 담당하는 메서드이다.
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
    return webSecurity ->
      webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    log.debug("[+] WebSecurityConfig Start !!! ");
    httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .authorizeRequests(authorizeRequests ->
            authorizeRequests.anyRequest().permitAll()
        ).addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

//  3. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미합니다.
//  - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(customAuthenticationProvider());
  }

  /**
   * 4. '인증' 제공자로 사용자의 이름과 비밀번호가 요구됩니다.
   * - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
   *
   * @return CustomAuthenticationProvider
   */
  @Bean
  public CustomAuthenticationProvider customAuthenticationProvider() {
    return new CustomAuthenticationProvider(bCryptPasswordEncoder());
  }

  /**
   * 5. 비밀번호를 암호화하기 위한 BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행합니다.
   *
   * @return BCryptPasswordEncoder
   */
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CustomAuthenticationFilter customAuthenticationFilter() {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
    customAuthenticationFilter.setFilterProcessesUrl("/api/login");
    customAuthenticationFilter.setAutheniticationSuccessHandler(customLoginSuccessHandler());
    customAuthenticationFilter.setAutheniticationFailuteHandler(customLoginFailureHandler());
    customAuthenticationFilter.afterPropertiesSet();
    return customAuthenticationFilter;
  }
  @Bean
  public CustomAuthSuccessHandler customLoginSuccessHandler() {
    return new CustomAuthSuccessHandler();
  }

  @Bean
  public CustomAuthFailureHandler customLoginFailureHandler() {
    return new CustomAuthFailureHandler();
  }


  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter();
  }
}
