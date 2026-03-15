package com.Campusland.ProyectoSpringBoot_CorpusEnrique.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para crear o actualizar una ciudad")
public record CiudadRequest(
        @Schema(description = "Nombre de la ciudad", example = "Medellín")
        @NotBlank(message = "El nombre de la ciudad es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(description = "ID del departamento al que pertenece la ciudad", example = "1")
        @NotNull(message = "El id de departamento es obligatorio")
        Long departamentoId
) {}
