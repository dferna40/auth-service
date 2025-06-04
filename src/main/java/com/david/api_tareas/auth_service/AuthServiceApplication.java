package com.david.api_tareas.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ✅ Esta clase es el punto de entrada del microservicio de autenticación.
// Al ejecutarse, arranca la aplicación Spring Boot, inicializando todos los componentes
// necesarios para la gestión de autenticación y autorización dentro del sistema.
@SpringBootApplication // Marca esta clase como la principal de una aplicación Spring Boot
public class AuthServiceApplication {

	public static void main(String[] args) {
		// Lanza la aplicación Spring Boot y carga el contexto de Spring
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
