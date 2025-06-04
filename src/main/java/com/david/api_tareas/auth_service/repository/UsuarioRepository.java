package com.david.api_tareas.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.api_tareas.auth_service.model.Usuario;

// ✅ Repositorio JPA para la entidad Usuario.
// Permite realizar operaciones CRUD y define una consulta personalizada para buscar usuarios por email.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Busca un usuario por su email (usado en login y validaciones de existencia)
	Optional<Usuario> findByEmail(String email);
}
