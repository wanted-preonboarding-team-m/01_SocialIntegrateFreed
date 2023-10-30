package com.wanted.socialintegratefreed.global.config.security.web;

import com.wanted.socialintegratefreed.domain.user.entity.UserEnable;
import com.wanted.socialintegratefreed.global.config.security.jwt.CustomAccessDeniedHandler;
import com.wanted.socialintegratefreed.global.config.security.jwt.JwtAuthenticationFilter;
import com.wanted.socialintegratefreed.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * web security: 전역으로 security 설정
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

  //토큰 생성 class
  private final JwtTokenProvider jwtTokenProvider;

  //예외를 잡기위해 custom Exception Class
  private final CustomAccessDeniedHandler customAccessDeniedHandler;


  // 전역으로 사용하기위한 passwordEncoder
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  // 전반적인 security 설정
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity
        .httpBasic(basic -> basic.disable())
        .csrf(csrf -> csrf.disable())
        .formLogin(form -> form.disable()) // form login, csrf httpbasic 차단: 토큰사용하고있기때문에
        .sessionManagement(session ->
            // 스프링시큐리티가 세션을 생성 및 존재하지않게 설정
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request ->
            request.requestMatchers("/api/v1/user/sign-up", "/api/v1/user/verify-user-code",
                "/api/v1/user/refresh-user-code/**",
                "/api/v1/user/login").permitAll())
        .authorizeHttpRequests(
            //api/v1/board 는 USER_ENABLED한 사람만 요청이가능함
            request -> request.requestMatchers("/api/v1/feeds/**")
                .hasRole(UserEnable.USER_ENABLED.toString()))
        // 들어오는 요청에 대해서 헤더안에 있는 Jwt를 체크
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler))
        .build();
  }


}
