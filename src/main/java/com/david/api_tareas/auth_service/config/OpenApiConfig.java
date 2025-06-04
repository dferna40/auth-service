package com.david.api_tareas.auth_service.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.*;

// ✅ Clase de configuración de Swagger/OpenAPI para documentar los endpoints del Auth Service.
// Define el esquema de seguridad con JWT y agrupa los endpoints bajo el prefijo "/auth/**".
@Configuration // Indica que esta clase contiene beans de configuración
public class OpenApiConfig {

	// Bean principal de configuración de OpenAPI
	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				// Información general que se mostrará en Swagger UI
				.info(new Info()
						.title("Auth Service API")
						.version("1.0")
						.description("Endpoints para login y registro con JWT"))
				// Aplica el esquema de seguridad a la documentación
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new Components().addSecuritySchemes("bearerAuth",
						new SecurityScheme()
							.name("bearerAuth")
							.type(SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")));
	}

	// Agrupa los endpoints bajo el grupo "auth" para Swagger
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("auth") // Nombre del grupo
				.pathsToMatch("/auth/**") // Incluye solo endpoints que empiezan por /auth
				.build();
	}
}
