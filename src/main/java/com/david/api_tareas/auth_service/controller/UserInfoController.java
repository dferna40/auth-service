package com.david.api_tareas.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.david.api_tareas.auth_service.model.Usuario;
import com.david.api_tareas.auth_service.repository.UsuarioRepository;

// ✅ Controlador REST que permite obtener la información del usuario autenticado.
// Extrae el email desde el token JWT y devuelve sus datos personales (sin exponer la contraseña).
@RestController
@RequestMapping("/auth") // Ruta base para este controlador
@RequiredArgsConstructor // Lombok: genera constructor para el campo final usuarioRepository
public class UserInfoController {

    private final UsuarioRepository usuarioRepository; // Repositorio para buscar el usuario por email

    // Endpoint para obtener los datos del usuario autenticado
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        // El email se extrae automáticamente del token JWT (campo "sub")
        String email = authentication.getName();

        // Buscar usuario en base de datos usando su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Retornar DTO con datos públicos del usuario (sin contraseña)
        return ResponseEntity.ok(new UserInfoDTO(
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getPuesto(),
                usuario.getRole()
        ));
    }

    // DTO interno para devolver datos del usuario autenticado
    private record UserInfoDTO(String email, String nombre, String puesto, String role) {}
}
