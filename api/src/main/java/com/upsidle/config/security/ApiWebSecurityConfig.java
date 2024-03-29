package com.upsidle.config.security;

import com.upsidle.config.security.jwt.JwtAuthTokenFilter;
import com.upsidle.config.security.jwt.JwtAuthenticationEntryPoint;
import com.upsidle.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * This configuration handles api web requests with stateless session.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ApiWebSecurityConfig {

  private final JwtAuthTokenFilter jwtAuthTokenFilter;
  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final AuthenticationManager authenticationManager;

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically, subclasses should not
   * call super as it may override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

    http.cors();

    http.exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    http.antMatcher(SecurityConstants.API_ROOT_URL_MAPPING)
        .authorizeRequests()
        .antMatchers(SecurityConstants.getPublicMatchers().toArray(new String[0]))
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.API_V1_USERS_ROOT_URL)
        .permitAll()
        .antMatchers(SecurityConstants.API_V1_AUTH_URL_MAPPING)
        .permitAll()
        .anyRequest()
        .authenticated();

    http.authenticationManager(authenticationManager);

    http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
