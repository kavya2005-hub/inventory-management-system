package com.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/staff/**").permitAll()     // All staff APIs public
                        .requestMatchers("/api/products/**").permitAll()  // Products APIs public
                        .anyRequest().permitAll()                        // Allow everything
                )
                .formLogin(form -> form.disable())   // ❌ disable login redirection
                .httpBasic(httpBasic -> {});         // allow API testing

        return http.build();
    }
}
