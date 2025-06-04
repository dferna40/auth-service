package com.david.api_tareas.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// ✅ DTO de salida para los endpoints de autenticación (/login y /register).
// Se utiliza para devolver al cliente el token JWT y algunos datos básicos del usuario autenticado.
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@AllArgsConstructor // Lombok: genera constructor con todos los campos
public class AuthResponse {
    private String token;  // Token JWT generado para autenticación
    private String email;  // Email del usuario autenticado
    private String nombre; // Nombre del usuario
    private String role;   // Rol del usuario (ej. ADMIN, USER, etc.)
}
