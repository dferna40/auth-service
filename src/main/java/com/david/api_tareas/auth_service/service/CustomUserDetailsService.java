package com.david.api_tareas.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.david.api_tareas.auth_service.model.Usuario;
import com.david.api_tareas.auth_service.repository.UsuarioRepository;

// ✅ Implementación personalizada de UserDetailsService para autenticación con Spring Security.
// Esta clase permite a Spring cargar los detalles de un usuario desde la base de datos usando su email.
// Es esencial para que funcione la autenticación con DaoAuthenticationProvider.
@Service
@RequiredArgsConstructor // Lombok: genera constructor para el campo final usuarioRepository
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Método obligatorio: carga un usuario por su nombre de usuario (en este caso, el email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario por email en la base de datos
        Usuario user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Devuelve una instancia de User (de Spring Security) con su email, contraseña y roles
        return User.builder()
                .username(user.getEmail()) // se usará como identificador principal
                .password(user.getPassword()) // contraseña encriptada
                .roles(user.getRole()) // rol asignado (ej. USER, ADMIN)
                .build();
    }
}
