package com.david.api_tareas.auth_service.dto;

import lombok.Data;

// ✅ DTO de entrada para las operaciones de autenticación (login y registro).
// Se usa para capturar los datos enviados por el cliente, como email y contraseña.
// También incluye campos adicionales necesarios durante el registro de usuario.
@Data // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
public class AuthRequest {
	private String email;     // Email del usuario (usado como identificador único)
    private String password;  // Contraseña del usuario (sin encriptar en la solicitud)
    private String nombre;    // Solo para registro: nombre completo del usuario
    private String puesto;    // Solo para registro: cargo o rol del usuario en la organización
}
