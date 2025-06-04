package com.david.api_tareas.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.david.api_tareas.auth_service.dto.AuthRequest;
import com.david.api_tareas.auth_service.dto.AuthResponse;
import com.david.api_tareas.auth_service.dto.LoginRequest;
import com.david.api_tareas.auth_service.service.AuthService;

// ✅ Controlador REST encargado de gestionar los endpoints de autenticación.
// Expone rutas para registrar nuevos usuarios y hacer login, retornando tokens JWT en ambos casos.
@RestController // Indica que es un controlador REST (retorna datos, no vistas)
@RequestMapping("/auth") // Ruta base para todos los endpoints de autenticación
@RequiredArgsConstructor // Lombok: genera constructor para el campo final `authService`
public class AuthController {

	private final AuthService authService; // Servicio encargado de la lógica de login y registro

	// Endpoint para registrar nuevos usuarios
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
		// Llama al servicio y devuelve el token generado en la respuesta
		return ResponseEntity.ok(authService.register(request));
	}

	// Endpoint para autenticar (login) y obtener un token JWT
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
	    return ResponseEntity.ok(authService.login(request));
	}
}
