package com.projectmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/actuator/**").permitAll()
            // Users endpoints
            .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("PMO", "PROJECT_MANAGER")
            .requestMatchers(HttpMethod.POST, "/users/**").hasRole("PMO")
            .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("PMO")
            .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("PMO")
            // Projects endpoints
            .requestMatchers(HttpMethod.GET, "/projects/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/projects/**").hasAnyRole("PMO", "PROJECT_MANAGER")
            .requestMatchers(HttpMethod.PUT, "/projects/**").hasAnyRole("PMO", "PROJECT_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/projects/**").hasAnyRole("PMO", "PROJECT_MANAGER")
            // Tasks endpoints
            .requestMatchers(HttpMethod.GET, "/tasks/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/tasks/**").hasAnyRole("PMO", "PROJECT_MANAGER", "DEVELOPER")
            .requestMatchers(HttpMethod.PUT, "/tasks/**").hasAnyRole("PMO", "PROJECT_MANAGER", "DEVELOPER")
            .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasAnyRole("PMO", "PROJECT_MANAGER")
            .anyRequest().authenticated();
        
        return http.build();
    }
} 