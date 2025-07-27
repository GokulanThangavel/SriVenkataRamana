package com.example.googlesheets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/**", "/css/**", "/js/**", "/", "/add-user.html", "/view-user.html").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()  // enable login form if needed
            .and()
            .csrf().disable(); // Optional, depending on your form setup

        return http.build();
    }
}
