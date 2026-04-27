package com.bci.tareas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir el registro (Sign-up)
                        .requestMatchers("/registro/usuario/sign-up/**").permitAll()
                        .requestMatchers("/api/canciones/**").permitAll()
                        // 2. Permitir el Login (Asegúrate que el prefijo sea /registro/usuario)
                        .requestMatchers(HttpMethod.POST, "/consulta/usuarios/login-auth").permitAll()

                        // 3. Todo lo demás bloqueado
                        .anyRequest().authenticated()
                )// ESTA LÍNEA REGISTRA EL FILTRO:

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}