package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar una categoría")
public record CategoriaRequest(
        @Schema(description = "Nombre de la categoría", example = "Electrónica")
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre
) {}
