package com.david.api_tareas.auth_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

// ✅ Entidad JPA que representa a un usuario dentro del sistema de autenticación.
// Esta clase se mapea a la tabla `usuarios` en la base de datos y almacena credenciales y datos personales.
@Entity
@Table(name = "usuarios") // Especifica el nombre de la tabla
@Data // Lombok: genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Lombok: constructor sin argumentos
@AllArgsConstructor // Lombok: constructor con todos los campos
@Builder // Lombok: permite crear objetos con patrón builder
public class Usuario {

    @Id // Indica la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    private String password; // Contraseña encriptada del usuario

    private String role; // Rol del usuario (ej. USER, ADMIN)

    private String nombre; // Nombre del usuario

    @Column(unique = true) // Email debe ser único en la base de datos
    private String email; // Identificador único y usado para autenticación

    //private String puesto; // Descripción del cargo o posición del usuario

    @Column(name = "fecha_creacion") // Fecha en que se creó el usuario
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion") // Fecha de la última modificación del usuario
    private LocalDateTime fechaModificacion;
}
