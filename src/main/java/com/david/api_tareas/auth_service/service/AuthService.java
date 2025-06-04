package com.david.api_tareas.auth_service.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.api_tareas.auth_service.dto.AuthRequest;
import com.david.api_tareas.auth_service.dto.AuthResponse;
import com.david.api_tareas.auth_service.dto.LoginRequest;
import com.david.api_tareas.auth_service.model.Usuario;
import com.david.api_tareas.auth_service.repository.UsuarioRepository;

// ✅ Servicio encargado de manejar la lógica de autenticación.
// Proporciona métodos para login y registro, emitiendo tokens JWT en ambos casos.
// Usa el repositorio para acceder a los usuarios y un encoder para manejar contraseñas seguras.
@Service
@RequiredArgsConstructor // Lombok: genera constructor con los campos final
public class AuthService {

	private final UsuarioRepository usuarioRepository; // Acceso a usuarios registrados
	private final JwtService jwtService; // Servicio que genera tokens JWT
	private final PasswordEncoder passwordEncoder; // Codificador para encriptar y verificar contraseñas

	// 🔐 Login de usuario: valida credenciales y devuelve token
	public AuthResponse login(LoginRequest loginRequest) {
	    // Busca al usuario por email
	    Usuario user = usuarioRepository.findByEmail(loginRequest.getEmail())
	            .orElseThrow(() -> new RuntimeException("Email no encontrado"));

	    // Verifica si la contraseña coincide (encriptada)
	    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Credenciales inválidas");
	    }

	    // Genera token JWT
	    String token = jwtService.generateToken(user);

	    // Devuelve token y datos del usuario
	    return new AuthResponse(token, user.getEmail(), user.getNombre(), user.getRole());
	}

	// 👤 Registro de nuevo usuario: guarda en base de datos y retorna token
	public AuthResponse register(AuthRequest authRequest) {
		// Verifica si el email ya está en uso
		if (usuarioRepository.findByEmail(authRequest.getEmail()).isPresent()) {
			throw new RuntimeException("El email ya está registrado");
		}

		// Crea un nuevo objeto Usuario a partir del AuthRequest
		Usuario nuevoUsuario = Usuario.builder()
				.email(authRequest.getEmail())
				.password(passwordEncoder.encode(authRequest.getPassword())) // Encripta la contraseña
				.role("USER") // Rol por defecto
				.nombre(authRequest.getNombre())
				.puesto(authRequest.getPuesto())
				.fechaCreacion(LocalDateTime.now())
				.fechaModificacion(LocalDateTime.now())
				.build();

		// Guarda el nuevo usuario en la base de datos
		usuarioRepository.save(nuevoUsuario);

		// Genera token JWT para el usuario recién registrado
		String token = jwtService.generateToken(nuevoUsuario);

		// Retorna respuesta con token y datos del nuevo usuario
		return new AuthResponse(token, nuevoUsuario.getEmail(), nuevoUsuario.getNombre(), nuevoUsuario.getRole());
	}
}
