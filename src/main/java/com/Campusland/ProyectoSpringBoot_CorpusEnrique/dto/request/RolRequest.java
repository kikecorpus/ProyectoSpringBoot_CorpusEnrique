package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar un rol")
public record RolRequest(
        @Schema(description = "Nombre del rol", example = "ADMIN")
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
        String nombre,

        @Schema(description = "Descripción del rol", example = "Administrador con acceso total al sistema")
        @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
        String descripcion
) {}
