package main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())                 // ❌ Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()             // ✅ Allow all endpoints
                )
                .httpBasic(Customizer.withDefaults())         // NOT needed but harmless
                .formLogin(form -> form.disable());           // Disable login redirect

        return http.build();
    }
}
