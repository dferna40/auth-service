package com.david.api_tareas.auth_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.*;

import java.io.IOException;
import java.util.*;

// Filtro personalizado de Spring Security que se ejecuta una vez por petición HTTP.
// Se encarga de validar el token JWT y establecer la autenticación en el contexto de seguridad.
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Clave secreta usada para validar la firma del JWT (se inyecta desde application.properties)
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 👉 EXCLUSIÓN de rutas públicas
        String path = request.getRequestURI();
        if (path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // Se obtiene la cabecera Authorization de la petición
        final String authHeader = request.getHeader("Authorization");

        // Si no hay cabecera o no comienza con "Bearer ", se continúa la cadena de filtros sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Se extrae el token JWT (quitando el prefijo "Bearer ")
        final String token = authHeader.substring(7);

        try {
            // Se valida y parsea el token JWT usando la clave secreta
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Se obtiene el "subject" del token (normalmente el nombre de usuario)
            String username = claims.getSubject();

            // Se construye un token de autenticación para Spring Security
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());

            // Se añade información adicional del request al token
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Se establece el token como autenticado en el contexto de seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (JwtException e) {
            // Si el token es inválido, se responde con un 401 UNAUTHORIZED
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Continúa la cadena de filtros normalmente
        chain.doFilter(request, response);
    }

}