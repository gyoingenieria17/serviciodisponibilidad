package com.example.serviciodisponibilidad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configuración CORS
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF (solo en pruebas)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/disponibilidad/**").permitAll() //.hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/disponibilidad/hoteles-disponibles-por-tipo").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/disponibilidad/rabbitmq/municipio/disponibilidad-por-tipo").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/disponibilidad/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/v1/disponibilidad/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/disponibliad/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});  // Autenticación básica sin configuración extra

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // Permitir credenciales (cookies, etc.)
        config.setAllowedOriginPatterns(Arrays.asList("*"));  // Permitir tu IP pública
        config.addAllowedHeader("*");  // Permitir todos los headers
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Métodos permitidos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
