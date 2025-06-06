package com.david.api_tareas.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestablecerPasswordRequest {
    private String token;
    private String nuevaPassword;
}