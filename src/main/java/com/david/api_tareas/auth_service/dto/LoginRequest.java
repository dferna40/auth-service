package com.david.api_tareas.auth_service.dto;

import lombok.Data;

@Data // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
public class LoginRequest {
	
	private String email;
    private String password;

}
