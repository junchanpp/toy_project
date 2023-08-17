package com.example.toy_project.config;

import com.example.toy_project.jwt.JwtAccessDeniedHandler;
import com.example.toy_project.jwt.JwtAuthenticationEntryPoint;
import com.example.toy_project.jwt.JwtSecurityConfig;
import com.example.toy_project.jwt.TokenProvider;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private final TokenProvider tokenProvider;
  @Resource
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @Resource
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @Resource
  private final CorsFilter corsFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/h2-console/**", "/favicon.ico", "/error");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)

        .and().headers().frameOptions().sameOrigin()

        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        .antMatchers("/api/hello").permitAll()
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers("/api/user/signup").permitAll()

        .anyRequest().authenticated()

        .and()
        .apply(new JwtSecurityConfig(tokenProvider));
  }
}
