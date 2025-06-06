package com.david.api_tareas.auth_service.controller;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.david.api_tareas.auth_service.dto.AuthRequest;
import com.david.api_tareas.auth_service.dto.AuthResponse;
import com.david.api_tareas.auth_service.dto.EmailRequest;
import com.david.api_tareas.auth_service.dto.LoginRequest;
import com.david.api_tareas.auth_service.dto.RestablecerPasswordRequest;
import com.david.api_tareas.auth_service.model.PasswordResetToken;
import com.david.api_tareas.auth_service.model.Usuario;
import com.david.api_tareas.auth_service.repository.PasswordResetTokenRepository;
import com.david.api_tareas.auth_service.repository.UsuarioRepository;
import com.david.api_tareas.auth_service.service.AuthService;

// ✅ Controlador REST encargado de gestionar los endpoints de autenticación.
// Expone rutas para registrar nuevos usuarios y hacer login, retornando tokens JWT en ambos casos.
@RestController // Indica que es un controlador REST (retorna datos, no vistas)
@RequestMapping("/auth") // Ruta base para todos los endpoints de autenticación
@RequiredArgsConstructor // Lombok: genera constructor para el campo final `authService`
public class AuthController {

	private final AuthService authService; // Servicio encargado de la lógica de login y registro
	private final PasswordResetTokenRepository tokenRepository;
	private final UsuarioRepository usuarioRepository;

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
	
	@PostMapping("/recuperar-password")
	public ResponseEntity<String> recuperarPassword(@RequestBody EmailRequest request) {
	    // Delegamos en el servicio
	    authService.enviarEnlaceRecuperacion(request.getEmail());
	    return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace.");
	}
	
	public void enviarEnlaceRecuperacion(String email) {
	    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();

	        // Generar token y fecha de expiración (1 hora)
	        String token = UUID.randomUUID().toString();
	        LocalDateTime expiracion = LocalDateTime.now().plusHours(1);

	        PasswordResetToken resetToken = PasswordResetToken.builder()
	                .token(token)
	                .usuario(usuario)
	                .expiracion(expiracion)
	                .build();

	        tokenRepository.save(resetToken);

	        String enlace = "http://localhost:3000/restablecer?token=" + token;

	        // Aquí iría el envío real de correo:
	        System.out.println("Enviar correo a: " + usuario.getEmail());
	        System.out.println("Con enlace: " + enlace);
	    }

	    // Siempre retornar como si fuera correcto
	}
	
	@PostMapping("/restablecer-password")
	public ResponseEntity<String> restablecerPassword(@RequestBody RestablecerPasswordRequest request) {
	    authService.restablecerPassword(request.getToken(), request.getNuevaPassword());
	    return ResponseEntity.ok("Contraseña actualizada correctamente.");
	}
}
