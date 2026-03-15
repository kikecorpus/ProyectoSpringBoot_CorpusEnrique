package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import com.Campusland.ProyectoSpringBoot_CorpusEnrique.model.Usuario.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar un usuario")
public record UsuarioRequest(
        @Schema(description = "Nombre de usuario único", example = "jperez")
        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede superar 50 caracteres")
        String username,

        @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "Segura123!")
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
        String contrasena,

        @Schema(description = "Estado del usuario", example = "ACTIVO")
        @NotNull(message = "El estado es obligatorio")
        Estado estado,

        @Schema(description = "ID de la persona asociada al usuario", example = "1")
        @NotNull(message = "El id de persona es obligatorio")
        Long personaId,

        @Schema(description = "ID del rol asignado al usuario", example = "1")
        @NotNull(message = "El id de rol es obligatorio")
        Long rolId
) {}
