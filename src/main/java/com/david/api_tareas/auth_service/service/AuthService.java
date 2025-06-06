package com.david.api_tareas.auth_service.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.api_tareas.auth_service.dto.AuthRequest;
import com.david.api_tareas.auth_service.dto.AuthResponse;
import com.david.api_tareas.auth_service.dto.LoginRequest;
import com.david.api_tareas.auth_service.model.PasswordResetToken;
import com.david.api_tareas.auth_service.model.Usuario;
import com.david.api_tareas.auth_service.repository.PasswordResetTokenRepository;
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
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailService emailService;

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
				//.puesto(authRequest.getPuesto())
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
	
	public void enviarEnlaceRecuperacion(String email) {
	    // 1. Buscar el usuario
	    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();

	        // 2. Generar token único con expiración
	        String token = UUID.randomUUID().toString();

	        // 3. Guardar el token (debes tener una tabla Token o campo en Usuario)
	        PasswordResetToken resetToken = PasswordResetToken.builder()
	        	    .token(token)
	        	    .usuario(usuario)
	        	    .expiracion(LocalDateTime.now().plusHours(1))
	        	    .build();
	        tokenRepository.save(resetToken);

	        // 4. Construir enlace
	        String enlace = "http://localhost:3000/restablecer?token=" + token;

	        // 5. Enviar correo (puedes usar JavaMailSender)
	        emailService.enviarCorreo(usuario.getEmail(), "Recuperación de contraseña", "Haz clic en este enlace para restablecer tu contraseña:\n" + enlace);
	    }

	    // ⚠️ No informar si el email no existe, por seguridad
	}
	
	public void restablecerPassword(String token, String nuevaPassword) {
	    PasswordResetToken resetToken = tokenRepository.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Token inválido"));

	    if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("Token expirado");
	    }

	    Usuario usuario = resetToken.getUsuario();
	    usuario.setPassword(passwordEncoder.encode(nuevaPassword));
	    usuario.setFechaModificacion(LocalDateTime.now());
	    usuarioRepository.save(usuario);

	    // Eliminar el token usado
	    tokenRepository.delete(resetToken);
	}

}
