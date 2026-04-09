package com.bhowmikbikash.SpringBootPractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Component
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFiler jwtFiler;

    public SecurityConfig(JwtFiler jwtFiler) {
        this.jwtFiler = jwtFiler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/employee").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/employee/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFiler, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
