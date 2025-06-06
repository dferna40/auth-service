package com.david.api_tareas.auth_service.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.david.api_tareas.auth_service.model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// ✅ Servicio responsable de generar tokens JWT para usuarios autenticados.
// Utiliza una clave secreta configurada en el archivo de propiedades para firmar los tokens.
// El token incluye el email del usuario (como subject) y su rol, y tiene una validez de 1 hora.
@Service
public class JwtService {

	@Value("${jwt.secret}") // Inyecta el valor de la clave secreta desde application.properties
    private String secretKey;

	// Genera un token JWT para un usuario dado
	public String generateToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // El "subject" del token es el email del usuario
                .claim("role", user.getRole()) // Añade un "claim" personalizado con el rol del usuario
                .setIssuedAt(new Date()) // Fecha en la que se emitió el token
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Fecha de expiración (1 hora)
                .signWith(
                    Keys.hmacShaKeyFor(secretKey.getBytes()), // Clave secreta en formato seguro
                    SignatureAlgorithm.HS256 // Algoritmo de firma
                )
                .compact(); // Genera el token como String
    }
}
