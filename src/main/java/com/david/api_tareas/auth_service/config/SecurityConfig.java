package com.david.api_tareas.auth_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ✅ Clase de configuración de seguridad para el microservicio de autenticación.
// Define el encoder de contraseñas, la gestión de autenticación y el control de acceso a rutas.
// Permite el acceso libre a rutas públicas (/auth/** y Swagger) y requiere autenticación para el resto.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Genera constructor con los campos finales (userDetailsService)
public class SecurityConfig {

    private final UserDetailsService userDetailsService; // Servicio que carga usuarios desde base de datos

    // Filtro de seguridad que define qué rutas requieren autenticación
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.cors(Customizer.withDefaults()) // 👈 habilitar CORS
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF porque estamos trabajando con APIs
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas sin autenticación
                        .requestMatchers(
                            "/auth/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**"
                        ).permitAll()
                        // El resto requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .build();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }

    // Bean para codificar contraseñas usando BCrypt (seguro y estándar en Spring Security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Proveedor de autenticación que conecta Spring Security con nuestro UserDetailsService y el encoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Usa nuestro servicio para buscar usuarios
        authProvider.setPasswordEncoder(passwordEncoder()); // Compara las contraseñas cifradas
        return authProvider;
    }

    // Gestiona las peticiones de autenticación, requerido por el flujo de login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Obtiene el manager configurado internamente por Spring
    }
}
