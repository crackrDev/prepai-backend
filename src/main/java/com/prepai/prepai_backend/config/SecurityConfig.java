package com.prepai.prepai_backend.config;

import com.prepai.prepai_backend.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable for REST APIs. csrf sometimes block the post request so, we have to disable it to prevent the blocking of testing API
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no token needed
                        .requestMatchers("/api/questions").permitAll()
                        .requestMatchers("/api/questions/**").permitAll()
                        .requestMatchers("/api/resume/parse").permitAll()
                        .requestMatchers("/api/sessions/start").permitAll()
                        .requestMatchers("/api/score/**").permitAll()
                        .requestMatchers("/api/results/**").permitAll()
                        .requestMatchers("/api/sessions/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        //For temporary purpose
                        .requestMatchers("/**").permitAll()

                        // Everything else needs JWT protected endpoints JWT needed
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
