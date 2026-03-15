package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar un departamento")
public record DepartamentoRequest(
        @Schema(description = "Nombre del departamento", example = "Antioquia")
        @NotBlank(message = "El nombre del departamento es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre
) {}
